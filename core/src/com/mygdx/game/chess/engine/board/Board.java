package com.mygdx.game.chess.engine.board;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.player.BlackPlayer;
import com.mygdx.game.chess.engine.player.Player;
import com.mygdx.game.chess.engine.player.WhitePlayer;
import com.mygdx.game.chess.engine.pieces.Bishop;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Knight;
import com.mygdx.game.chess.engine.pieces.Pawn;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.Queen;
import com.mygdx.game.chess.engine.pieces.Rook;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.mygdx.game.chess.engine.board.Move.MoveFactory;

public final class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces, blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;
    private final int moveCount;

    private final Move transitionMove;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(builder, League.WHITE);
        this.blackPieces = calculateActivePieces(builder, League.BLACK);

        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = this.calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = this.calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves, builder.whiteMinute, builder.whiteSecond, builder.whiteMillisecond);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves, builder.blackMinute, builder.blackSecond, builder.blackMillisecond);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

        this.moveCount = builder.moveCount();
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : MoveFactory.getNullMove();
    }

    public int getMoveCount() { return this.moveCount; }
    public Player currentPlayer() {
        return this.currentPlayer;
    }
    public Player whitePlayer() {
        return this.whitePlayer;
    }
    public Player blackPlayer() {
        return this.blackPlayer;
    }
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }
    public Collection<Piece> getBlackPieces() { return this.blackPieces; }
    public Pawn getEnPassantPawn() { return this.enPassantPawn; }
    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }
    public Move getTransitionMove() { return this.transitionMove; }

    public Collection<Piece> getAllPieces() {
        final Collection<Piece> activePieces = new ArrayList<>(this.whitePieces);
        activePieces.addAll(this.blackPieces);
        return Collections.unmodifiableCollection(activePieces);
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final Builder builder, final League league) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Piece piece : builder.boardConfig.values()) {
            if (piece.getLeague() == league) {
                activePieces.add(piece);
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    public static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        //PARSE array as list
        return Collections.unmodifiableList(Arrays.asList(tiles));
    }

    public static Board createStandardBoardForMoveHistory(final String[] whiteTimer, final String[] blackTimer) {
        //white to move
        final Builder builder = new Builder(0, League.WHITE, null)
                .updateWhiteTimer(Integer.parseInt(whiteTimer[0]), Integer.parseInt(whiteTimer[1]), Integer.parseInt(whiteTimer[2]))
                .updateBlackTimer(Integer.parseInt(blackTimer[0]), Integer.parseInt(blackTimer[1]), Integer.parseInt(blackTimer[2]));
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 0))
                .setPiece(new Knight(League.BLACK, 1))
                .setPiece(new Bishop(League.BLACK, 2))
                .setPiece(new Queen(League.BLACK, 3))
                .setPiece(new King(League.BLACK, 4, true, true))
                .setPiece(new Bishop(League.BLACK, 5))
                .setPiece(new Knight(League.BLACK, 6))
                .setPiece(new Rook(League.BLACK, 7));
        for (int i = 8; i < 16; i++) {
            builder.setPiece(new Pawn(League.BLACK, i));
        }
        // White Layout
        for (int i = 48; i < 56; i++) {
            builder.setPiece(new Pawn(League.WHITE, i));
        }
        builder.setPiece(new Rook(League.WHITE, 56))
                .setPiece(new Knight(League.WHITE, 57))
                .setPiece(new Bishop(League.WHITE, 58))
                .setPiece(new Queen(League.WHITE, 59))
                .setPiece(new King(League.WHITE, 60,true, true))
                .setPiece(new Bishop(League.WHITE, 61))
                .setPiece(new Knight(League.WHITE, 62))
                .setPiece(new Rook(League.WHITE, 63));
        //build the board
        return builder.build();
    }

    public static Board createStandardBoard(final int minute, final int second, final int millisecond) {
        //white to move
        final Builder builder = new Builder(0, League.WHITE, null)
                .updateWhiteTimer(minute, second, millisecond)
                .updateBlackTimer(minute, second, millisecond);
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 0))
                .setPiece(new Knight(League.BLACK, 1))
                .setPiece(new Bishop(League.BLACK, 2))
                .setPiece(new Queen(League.BLACK, 3))
                .setPiece(new King(League.BLACK, 4, true, true))
                .setPiece(new Bishop(League.BLACK, 5))
                .setPiece(new Knight(League.BLACK, 6))
                .setPiece(new Rook(League.BLACK, 7));
        for (int i = 8; i < 16; i++) {
            builder.setPiece(new Pawn(League.BLACK, i));
        }
        // White Layout
        for (int i = 48; i < 56; i++) {
            builder.setPiece(new Pawn(League.WHITE, i));
        }
        builder.setPiece(new Rook(League.WHITE, 56))
                .setPiece(new Knight(League.WHITE, 57))
                .setPiece(new Bishop(League.WHITE, 58))
                .setPiece(new Queen(League.WHITE, 59))
                .setPiece(new King(League.WHITE, 60,true, true))
                .setPiece(new Bishop(League.WHITE, 61))
                .setPiece(new Knight(League.WHITE, 62))
                .setPiece(new Rook(League.WHITE, 63));
        //build the board
        return builder.build();
    }

    public static final class Builder {

        private final HashMap<Integer, Piece> boardConfig;
        private final League nextMoveMaker;
        private final Pawn enPassantPawn;
        private final int moveCount;
        private int whiteMinute, whiteSecond, whiteMillisecond;
        private int blackMinute, blackSecond, blackMillisecond;
        private Move transitionMove;

        public Builder(final int moveCount, final League nextMoveMaker, final Pawn enPassantPawn) {
            //set initialCapacity to 32 and loadFactor to 1 to reduce chance of hash collision
            this.boardConfig = new HashMap<>(32, 1);
            this.nextMoveMaker = nextMoveMaker;
            this.moveCount = moveCount;
            this.enPassantPawn = enPassantPawn;
            this.whiteMillisecond = BoardUtils.DEFAULT_TIMER_MILLISECOND;
            this.whiteSecond = BoardUtils.DEFAULT_TIMER_SECOND;
            this.whiteMinute = BoardUtils.DEFAULT_TIMER_MINUTE;
            this.blackMillisecond = BoardUtils.DEFAULT_TIMER_MILLISECOND;
            this.blackSecond = BoardUtils.DEFAULT_TIMER_SECOND;
            this.blackMinute = BoardUtils.DEFAULT_TIMER_MINUTE;
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Board build() { return new Board(this); }

        public void setTransitionMove(final Move transitionMove) { this.transitionMove = transitionMove; }

        public int moveCount() { return this.moveCount; }

        public Builder updateWhiteTimer(final int whiteMinute, final int whiteSecond, final int whiteMillisecond) {
            this.whiteMinute = whiteMinute;
            this.whiteSecond = whiteSecond;
            this.whiteMillisecond = whiteMillisecond;
            return this;
        }

        public Builder updateBlackTimer(final int blackMinute, final int blackSecond, final int blackMillisecond) {
            this.blackMinute = blackMinute;
            this.blackSecond = blackSecond;
            this.blackMillisecond = blackMillisecond;
            return this;
        }
    }
}