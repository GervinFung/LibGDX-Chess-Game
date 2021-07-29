package com.mygdx.game.gui.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.gui.ArtificialIntelligence;
import com.mygdx.game.gui.GuiUtils;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.player.Player;
import com.mygdx.game.gui.gameScreen.GameScreen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;

public final class GameBoard extends Table {

    //object
    private Piece humanPiece;
    private Move humanMove, aiMove;

    //enum
    private GameProps.GameEnd gameEnd;
    private GameProps.HighlightPreviousMove highlightPreviousMove;
    private GameProps.HighlightMove highlightMove;
    private GameProps.ArtificialIntelligenceWorking artificialIntelligenceWorking;
    private GameProps.PlayerType whitePlayerType, blackPlayerType;
    private GameProps.BoardDirection boardDirection;

    private final PropertyChangeSupport gameSetupPropertyChangeSupport;
    private final ArtificialIntelligence artificialIntelligence;

    //object updater
    public void updateHumanPiece(final Piece humanPiece) { this.humanPiece = humanPiece; }
    public void updateHumanMove(final Move humanMove) { this.humanMove = humanMove; }
    public void updateAiMove(final Move aiMove) { this.aiMove = aiMove; }

    //enum updater
    public void updateArtificialIntelligenceWorking(final GameProps.ArtificialIntelligenceWorking AIThinking) { this.artificialIntelligenceWorking = AIThinking; }
    public void updateGameEnd(final GameProps.GameEnd gameEnd) { this.gameEnd = gameEnd; }
    public void updateHighlightMove(final GameProps.HighlightMove highlightMove) { this.highlightMove = highlightMove; }
    public void updateHighlightPreviousMove(final GameProps.HighlightPreviousMove highlightPreviousMove) { this.highlightPreviousMove = highlightPreviousMove; }
    public void updateBoardDirection() { this.boardDirection = this.boardDirection.opposite(); }
    public void updateWhitePlayerType(final GameProps.PlayerType playerType) { this.whitePlayerType = playerType; }
    public void updateBlackPlayerType(final GameProps.PlayerType playerType) { this.blackPlayerType = playerType; }

    //getter
    public Piece getHumanPiece() { return this.humanPiece; }
    public Move getHumanMove() { return this.humanMove; }
    public Move getAiMove() { return this.aiMove; }

    public void fireGameSetupPropertyChangeSupport() { this.gameSetupPropertyChangeSupport.firePropertyChange(null, null, null); }
    public boolean getArtificialIntelligenceWorking() { return this.artificialIntelligenceWorking.isArtificialIntelligenceWorking(); }
    public boolean isGameEnd() { return this.gameEnd.isGameEnded(); }
    public boolean isHighlightMove() { return this.highlightMove.isHighlightMove(); }
    public boolean isHighlightPreviousMove() { return this.highlightPreviousMove.isHighlightPreviousMove(); }
    public ArtificialIntelligence getArtificialIntelligence() { return this.artificialIntelligence; }

    public GameBoard(final GameScreen gameScreen) {
        //mutable
        this.humanPiece = null;
        this.humanMove = null;

        this.gameEnd = GameProps.GameEnd.ONGOING;
        this.highlightMove = GameProps.HighlightMove.HIGHLIGHT_MOVE;
        this.highlightPreviousMove = GameProps.HighlightPreviousMove.HIGHLIGHT_PREVIOUS_MOVE;
        this.artificialIntelligenceWorking = GameProps.ArtificialIntelligenceWorking.RESTING;

        this.whitePlayerType = GameProps.PlayerType.HUMAN;
        this.blackPlayerType = GameProps.PlayerType.HUMAN;

        //immutable
        this.artificialIntelligence = new ArtificialIntelligence();
        final PropertyChangeListener gameSetupPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
                if (isAIPlayer(gameScreen.getChessBoard().currentPlayer())
                        && !gameScreen.getChessBoard().currentPlayer().isInCheckmate()
                        && !gameScreen.getChessBoard().currentPlayer().isInStalemate()) {
                    if (!getArtificialIntelligenceWorking()) {
                        updateArtificialIntelligenceWorking(GameProps.ArtificialIntelligenceWorking.WORKING);
                        artificialIntelligence.startAI(gameScreen);
                    }
                }
                displayEndGameMessage(gameScreen.getChessBoard(), gameScreen.getStage());
            }
        };
        this.gameSetupPropertyChangeSupport = new PropertyChangeSupport(gameSetupPropertyChangeListener);
        this.gameSetupPropertyChangeSupport.addPropertyChangeListener(gameSetupPropertyChangeListener);

        this.boardDirection = GameProps.BoardDirection.NORMAL_BOARD;
        this.setFillParent(true);
        for (int i = 0; i < BoardUtils.NUM_TILES; i+=1) {
            if (i % 8 == 0) {
                this.row();
            }
            this.add(new com.mygdx.game.gui.board.TileActor(gameScreen, this.textureRegion(gameScreen.getChessBoard(), i), i)).size(GuiUtils.TILE_SIZE);
        }
        this.validate();
    }

    public boolean isAIPlayer(final Player player) {
        if(player.getLeague() == League.WHITE) {
            return this.whitePlayerType == GameProps.PlayerType.COMPUTER;
        }
        return this.blackPlayerType == GameProps.PlayerType.COMPUTER;
    }

    public void drawBoard(final GameScreen gameScreen, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
        this.boardDirection.drawBoard(gameScreen, this, chessBoard, displayOnlyBoard);
    }

    public void displayTimeOutMessage(final Board chessBoard, final Stage stage) {
        if (chessBoard.currentPlayer().isTimeOut()) {
            final Label label = new Label(chessBoard.currentPlayer() + " player is timed out!", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Time out", GuiUtils.UI_SKIN).text(label).button("Ok").show(stage);
            this.updateGameEnd(GameProps.GameEnd.ENDED);
        }
    }

    public void displayEndGameMessage(final Board chessBoard, final Stage stage) {
        if (chessBoard.currentPlayer().isInCheckmate()) {
            final Label label;
            label = new Label(chessBoard.currentPlayer() + " player is in checkmate!", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Checkmate", GuiUtils.UI_SKIN).text(label).button("Ok").show(stage);
            this.updateGameEnd(GameProps.GameEnd.ENDED);
        } else if (chessBoard.currentPlayer().isInStalemate()) {
            final Label label;
            label = new Label(chessBoard.currentPlayer() + " player is in stalemate!", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            new Dialog("Stalemate", GuiUtils.UI_SKIN).text(label).button("Ok").show(stage);
            this.updateGameEnd(GameProps.GameEnd.ENDED);
        }
    }

    protected TextureRegion textureRegion (final Board board, final int tileID) {
        if (board.getTile(tileID).isTileOccupied()) {
            return GuiUtils.GET_PIECE_TEXTURE_REGION(board.getTile(tileID).getPiece());
        }
        return GuiUtils.TRANSPARENT_TEXTURE_REGION;
    }

    public static final class DisplayOnlyBoard extends Table {

        private GuiUtils.TILE_COLOR TILE_COLOR;

        public DisplayOnlyBoard() {
            this.setFillParent(true);
            this.TILE_COLOR = GuiUtils.TILE_COLOR.CLASSIC;
            for (int i = 0; i < BoardUtils.NUM_TILES; i+=1) {
                if (i % 8 == 0) {
                    this.row();
                }
                final com.mygdx.game.gui.board.TileActor.DisplayOnlyTile displayOnlyTile = new TileActor.DisplayOnlyTile(i);
                displayOnlyTile.setColor(getTileColor(this.TILE_COLOR, i));
                this.add(displayOnlyTile).size(GuiUtils.TILE_SIZE);
            }
            this.validate();
        }

        public void setTileColor(final GuiUtils.TILE_COLOR tile_color) { this.TILE_COLOR = tile_color; }
        public GuiUtils.TILE_COLOR getTileColor() { return this.TILE_COLOR; }

        private static Color getTileColor(final GuiUtils.TILE_COLOR TILE_COLOR, final int i) {
            if (BoardUtils.FIRST_ROW.get(i) || BoardUtils.THIRD_ROW.get(i) || BoardUtils.FIFTH_ROW.get(i) || BoardUtils.SEVENTH_ROW.get(i)) {
                return i % 2 == 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
            }
            return i % 2 != 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
        }

        private static Color getHighlightTileColor(final GuiUtils.TILE_COLOR TILE_COLOR, final int i) {
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
                final int tileID = gameBoard.boardDirection.flipped() ? 63 - move.getDestinationCoordinate() : move.getDestinationCoordinate();
                if (move.isAttack() || move.isPromotionMove() && ((Move.PawnPromotion) move).getDecoratedMove().isAttack()) {
                    this.getChildren().get(tileID).setColor(new Color(204 / 255f, 0 / 255f, 0 / 255f, 1));
                }
                else {
                    this.getChildren().get(tileID).setColor(getHighlightTileColor(getTileColor(), tileID));
                }
            }
        }
    }
}