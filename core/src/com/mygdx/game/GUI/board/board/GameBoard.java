package com.mygdx.game.GUI.board.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.GUI.board.ArtificialIntelligence;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.player.Player;
import com.mygdx.game.MyGdxGame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;

public final class GameBoard extends Table {

    private Piece humanPiece;
    private boolean gameEnd, AIThinking, highlightMove, highlightPreviousMove;
    private Move humanMove, aiMove;
    private PLAYER_TYPE whitePlayerType, blackPlayerType;
    private BOARD_DIRECTION board_direction;

    private final PropertyChangeSupport gameSetupPropertyChangeSupport;
    private final ArtificialIntelligence artificialIntelligence;

    //setter
    public void setHumanPiece(final Piece humanPiece) { this.humanPiece = humanPiece; }
    public void setHumanMove(final Move humanMove) { this.humanMove = humanMove; }
    public void setAiMove(final Move aiMove) { this.aiMove = aiMove; }
    public void setGameEnd(final boolean gameEnd) { this.gameEnd = gameEnd; }
    public void changeBoardDirection() { this.board_direction = this.board_direction.opposite(); }
    public void changeWhitePlayerType() { this.whitePlayerType = this.whitePlayerType.opposite(); }
    public void changeBlackPlayerType() { this.blackPlayerType = this.blackPlayerType.opposite(); }
    public void setAIThinking(final boolean AIThinking) { this.AIThinking = AIThinking; }
    public void setHighlightMove(final boolean highlightMove) { this.highlightMove = highlightMove; }
    public void setHighlightPreviousMove(final boolean highlightPreviousMove) { this.highlightPreviousMove = highlightPreviousMove; }

    //getter
    public Piece getHumanPiece() { return this.humanPiece; }
    public Move getHumanMove() { return this.humanMove; }
    public Move getAiMove() { return this.aiMove; }
    public boolean getGameEnd() { return this.gameEnd; }
    public void fireGameSetupPropertyChangeSupport() { this.gameSetupPropertyChangeSupport.firePropertyChange(null, null, null); }
    public boolean isAIThinking() { return this.AIThinking; }
    public boolean isGameEnd() { return this.gameEnd; }
    public ArtificialIntelligence getArtificialIntelligence() { return this.artificialIntelligence; }
    public boolean isHighlightMove() { return this.highlightMove; }
    public boolean isHighlightPreviousMove() { return this.highlightPreviousMove; }

    public GameBoard(final MyGdxGame myGdxGame) {
        //mutable
        this.humanPiece = null;
        this.humanMove = null;
        this.gameEnd = false;
        this.AIThinking = false;
        this.whitePlayerType = PLAYER_TYPE.HUMAN;
        this.blackPlayerType = PLAYER_TYPE.HUMAN;
        this.highlightMove = true;
        this.highlightPreviousMove = true;
        //immutable
        this.artificialIntelligence = new ArtificialIntelligence();
        final PropertyChangeListener gameSetupPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
                if (isAIPlayer(myGdxGame.getChessBoard().currentPlayer())
                        && !myGdxGame.getChessBoard().currentPlayer().isInCheckmate()
                        && !myGdxGame.getChessBoard().currentPlayer().isInStalemate()) {
                    setAIThinking(true);
                    artificialIntelligence.startAI(myGdxGame);
                }
                displayEndGameMessage(myGdxGame.getChessBoard(), myGdxGame.getStage());
            }
        };
        this.gameSetupPropertyChangeSupport = new PropertyChangeSupport(gameSetupPropertyChangeListener);
        this.gameSetupPropertyChangeSupport.addPropertyChangeListener(gameSetupPropertyChangeListener);

        this.board_direction = BOARD_DIRECTION.NORMAL_BOARD;
        this.setFillParent(true);
        for (int i = 0; i < BoardUtils.NUM_TILES; i+=1) {
            if (i % 8 == 0) {
                this.row();
            }
            this.add(new TileActor(myGdxGame, this.textureRegion(myGdxGame.getChessBoard(), i), i)).size(GUI_UTILS.TILE_SIZE);
        }
        this.validate();
    }

    private enum BOARD_DIRECTION {
        NORMAL_BOARD {
            @Override
            void drawBoard(final MyGdxGame myGdxGame, final GameBoard gameBoard, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
                gameBoard.clearChildren();
                displayOnlyBoard.clearChildren();
                for (int i = 0; i < BoardUtils.NUM_TILES; i+=1) {
                    if (i % 8 == 0) {
                        gameBoard.row();
                        displayOnlyBoard.row();
                    }
                    gameBoard.add(new TileActor(myGdxGame, gameBoard.textureRegion(chessBoard, i), i)).size(GUI_UTILS.TILE_SIZE);
                    final TileActor.DisplayOnlyTile tile = new TileActor.DisplayOnlyTile(i);
                    tile.repaint(gameBoard, chessBoard, myGdxGame.getDisplayOnlyBoard());
                    displayOnlyBoard.add(tile).size(GUI_UTILS.TILE_SIZE);
                }
                gameBoard.validate();
                displayOnlyBoard.validate();
            }
            @Override
            BOARD_DIRECTION opposite() { return FLIP_BOARD; }
            @Override
            boolean flipped() { return false; }
        },
        FLIP_BOARD {
            @Override
            void drawBoard(final MyGdxGame myGdxGame, final GameBoard gameBoard, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
                gameBoard.clearChildren();
                displayOnlyBoard.clearChildren();
                for (int i = BoardUtils.NUM_TILES - 1; i >= 0; i-=1) {
                    gameBoard.add(new TileActor(myGdxGame, gameBoard.textureRegion(chessBoard, i), i)).size(GUI_UTILS.TILE_SIZE);
                    final TileActor.DisplayOnlyTile tile = new TileActor.DisplayOnlyTile(i);
                    tile.repaint(gameBoard, chessBoard, myGdxGame.getDisplayOnlyBoard());
                    displayOnlyBoard.add(tile).size(GUI_UTILS.TILE_SIZE);
                    if (i % 8 == 0) {
                        gameBoard.row();
                        displayOnlyBoard.row();
                    }
                }
                gameBoard.validate();
                displayOnlyBoard.validate();
            }
            @Override
            BOARD_DIRECTION opposite() { return NORMAL_BOARD; }
            @Override
            boolean flipped() { return true; }
        };
        abstract BOARD_DIRECTION opposite();
        abstract boolean flipped();
        abstract void drawBoard(final MyGdxGame myGdxGame, final GameBoard gameBoard, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard);
    }

    private enum PLAYER_TYPE {
        HUMAN {
            @Override
            PLAYER_TYPE opposite() { return COMPUTER; }
        }, COMPUTER {
            @Override
            PLAYER_TYPE opposite() { return HUMAN; }
        };
        abstract PLAYER_TYPE opposite();
    }

    public boolean isAIPlayer(final Player player) {
        if(player.getLeague() == League.WHITE) {
            return this.whitePlayerType == PLAYER_TYPE.COMPUTER;
        }
        return this.blackPlayerType == PLAYER_TYPE.COMPUTER;
    }

    public void drawBoard(final MyGdxGame myGdxGame, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
        this.board_direction.drawBoard(myGdxGame, this, chessBoard, displayOnlyBoard);
    }

    public void displayTimeOutMessage(final Board chessBoard, final Stage stage) {
        if (chessBoard.currentPlayer().isTimeOut()) {
            final Label label = new Label(chessBoard.currentPlayer() + " player is timed out!", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Time out", GUI_UTILS.UI_SKIN).text(label).button("Ok").show(stage);
            this.setGameEnd(true);
        }
    }

    public void displayEndGameMessage(final Board chessBoard, final Stage stage) {
        final Label label;
        if (chessBoard.currentPlayer().isInCheckmate()) {
            label = new Label(chessBoard.currentPlayer() + " player is in checkmate!", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Checkmate", GUI_UTILS.UI_SKIN).text(label).button("Ok").show(stage);
            this.setGameEnd(true);
        } else if (chessBoard.currentPlayer().isInStalemate()) {
            label = new Label(chessBoard.currentPlayer() + " player is in stalemate!", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Stalemate", GUI_UTILS.UI_SKIN).text(label).button("Ok").show(stage);
            this.setGameEnd(true);
        }
    }

    private TextureRegion textureRegion (final Board board, final int tileID) {
        if (board.getTile(tileID).isTileOccupied()) {
            return GUI_UTILS.GET_PIECE_TEXTURE_REGION(board.getTile(tileID).getPiece());
        }
        return GUI_UTILS.TRANSPARENT_TEXTURE_REGION;
    }

    public static final class DisplayOnlyBoard extends Table {

        private GUI_UTILS.TILE_COLOR TILE_COLOR;

        public DisplayOnlyBoard() {
            this.setFillParent(true);
            this.TILE_COLOR = GUI_UTILS.TILE_COLOR.CLASSIC;
            for (int i = 0; i < BoardUtils.NUM_TILES; i+=1) {
                if (i % 8 == 0) {
                    this.row();
                }
                final TileActor.DisplayOnlyTile displayOnlyTile = new TileActor.DisplayOnlyTile(i);
                displayOnlyTile.setColor(getTileColor(this.TILE_COLOR, i));
                this.add(displayOnlyTile).size(GUI_UTILS.TILE_SIZE);
            }
            this.validate();
        }

        public void setTileColor(final GUI_UTILS.TILE_COLOR tile_color) { this.TILE_COLOR = tile_color; }
        public GUI_UTILS.TILE_COLOR getTileColor() { return this.TILE_COLOR; }

        private static Color getTileColor(final GUI_UTILS.TILE_COLOR TILE_COLOR, final int i) {
            if (BoardUtils.FIRST_ROW.get(i) || BoardUtils.THIRD_ROW.get(i) || BoardUtils.FIFTH_ROW.get(i) || BoardUtils.SEVENTH_ROW.get(i)) {
                return i % 2 == 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
            }
            return i % 2 != 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
        }

        private static Color getHighlightTileColor(final GUI_UTILS.TILE_COLOR TILE_COLOR, final int i) {
            if (BoardUtils.FIRST_ROW.get(i) || BoardUtils.THIRD_ROW.get(i) || BoardUtils.FIFTH_ROW.get(i) || BoardUtils.SEVENTH_ROW.get(i)) {
                return i % 2 == 0 ? TILE_COLOR.HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() : TILE_COLOR.HIGHLIGHT_LEGAL_MOVE_DARK_TILE();
            }
            return i % 2 != 0 ? TILE_COLOR.HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() : TILE_COLOR.HIGHLIGHT_LEGAL_MOVE_DARK_TILE();
        }

        private Collection<Move> pieceLegalMoves(final Piece piece, final Board chessBoard) {
            if (piece != null && piece.getLeague() == chessBoard.currentPlayer().getLeague()){
                return Collections.unmodifiableCollection(piece.calculateLegalMoves(chessBoard));
            }
            return Collections.emptyList();
        }

        public void highlightLegalMove(final GameBoard gameBoard, final Board chessBoard) {
            for (final Move move : this.pieceLegalMoves(gameBoard.getHumanPiece(), chessBoard)) {
                final int tileID = gameBoard.board_direction.flipped() ? 63 - move.getDestinationCoordinate() : move.getDestinationCoordinate();
                if (move.isAttack() || move instanceof Move.PawnPromotion && ((Move.PawnPromotion) move).getDecoratedMove().isAttack()) {
                    this.getChildren().get(tileID).setColor(new Color(204 / 255f, 0 / 255f, 0 / 255f, 1));
                }
                else {
                    this.getChildren().get(tileID).setColor(getHighlightTileColor(getTileColor(), tileID));
                }
            }
        }
    }
}