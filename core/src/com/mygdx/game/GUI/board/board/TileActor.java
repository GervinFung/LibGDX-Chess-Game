package com.mygdx.game.GUI.board.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.MyGdxGame;

public final class TileActor extends Image {

    protected TileActor(final MyGdxGame myGdxGame, final TextureRegion region, final int tileID) {
        super(region);
        this.setVisible(true);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                try {
                    super.clicked(event, x, y);
                    myGdxGame.getGameBoard().setAiMove(null);
                    myGdxGame.getGameBoard().setHumanMove(null);
                    if (myGdxGame.getGameBoard().getGameEnd() || myGdxGame.getGameBoard().isAIThinking()) { return ; }

                    if (myGdxGame.getGameBoard().getHumanPiece() ==  null) {
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        if (myGdxGame.getChessBoard().getTile(tileID).getPiece().getLeague() == myGdxGame.getChessBoard().currentPlayer().getLeague()) {
                            myGdxGame.getGameBoard().setHumanPiece(myGdxGame.getChessBoard().getTile(tileID).getPiece());
                            if (myGdxGame.getGameBoard().isHighlightMove()) {
                                myGdxGame.getDisplayOnlyBoard().highlightLegalMove(myGdxGame.getGameBoard(), myGdxGame.getChessBoard());
                            }
                        }

                    } else {
                        if (myGdxGame.getGameBoard().getHumanPiece().getLeague() == myGdxGame.getChessBoard().currentPlayer().getLeague()) {
                            final Move move = Move.MoveFactory.createMove(myGdxGame.getChessBoard(), myGdxGame.getGameBoard().getHumanPiece(), tileID);
                            final MoveTransition transition = myGdxGame.getChessBoard().currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                if (myGdxGame.getGameBoard().isHighlightPreviousMove()) {
                                    myGdxGame.getGameBoard().setHumanMove(move);
                                }
                                myGdxGame.updateChessBoard(transition.getLatestBoard());
                                if (move instanceof Move.PawnPromotion) {
                                    //display pawn promotion interface
                                    ((Move.PawnPromotion)move).startLibGDXPromotion(myGdxGame);
                                } else {
                                    myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                                }
                                myGdxGame.getMoveHistory().getMoveLog().addMove(move);
                                myGdxGame.getMoveHistory().updateMoveHistory();
                                if (myGdxGame.getGameBoard().isAIPlayer(myGdxGame.getChessBoard().currentPlayer())) {
                                    myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport();
                                }
                            }
                        } else {
                            myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                            myGdxGame.getGameBoard().setAiMove(null);
                            myGdxGame.getGameBoard().setHumanMove(null);
                        }
                        myGdxGame.getGameBoard().setHumanPiece(null);
                    }
                } catch (final NullPointerException ignored) {}
            }
        });
    }

    protected final static class DisplayOnlyTile extends Image {

        private final int tileID;

        protected DisplayOnlyTile(final int tileID) {
            super(GUI_UTILS.GET_TILE_TEXTURE_REGION("white"));
            this.tileID = tileID;
            this.setVisible(true);
        }

        private Color getTileColor(final GUI_UTILS.TILE_COLOR TILE_COLOR) {
            if (BoardUtils.FIRST_ROW.get(this.tileID) || BoardUtils.THIRD_ROW.get(this.tileID) || BoardUtils.FIFTH_ROW.get(this.tileID) || BoardUtils.SEVENTH_ROW.get(this.tileID)) {
                return this.tileID % 2 == 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
            }
            return this.tileID % 2 != 0 ? TILE_COLOR.LIGHT_TILE() : TILE_COLOR.DARK_TILE();
        }

        private Color getHumanMoveColor(final GameBoard gameBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
            if (this.tileID == gameBoard.getHumanMove().getCurrentCoordinate()) {
                return GUI_UTILS.HUMAN_PREVIOUS_TILE;
            } else if (this.tileID == gameBoard.getHumanMove().getDestinationCoordinate()) {
                return GUI_UTILS.HUMAN_CURRENT_TILE;
            }
            return this.getTileColor(displayOnlyBoard.getTileColor());
        }

        private Color getAIMoveColor(final GameBoard gameBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
            if (this.tileID == gameBoard.getAiMove().getCurrentCoordinate()) {
                return GUI_UTILS.AI_PREVIOUS_TILE;
            } else if (this.tileID == gameBoard.getAiMove().getDestinationCoordinate()) {
                return GUI_UTILS.AI_CURRENT_TILE;
            }
            return this.getTileColor(displayOnlyBoard.getTileColor());
        }

        public void repaint(final GameBoard gameBoard, final Board chessBoard, final GameBoard.DisplayOnlyBoard displayOnlyBoard) {
            if (chessBoard.currentPlayer().isInCheck() &&  chessBoard.currentPlayer().getPlayerKing().getPiecePosition() == this.tileID) {
                this.setColor(Color.RED);
            } else if (gameBoard.getHumanMove() != null) {
                this.setColor(this.getHumanMoveColor(gameBoard, displayOnlyBoard));
            } else if (gameBoard.getAiMove() != null) {
                this.setColor(this.getAIMoveColor(gameBoard, displayOnlyBoard));
            } else {
                this.setColor(this.getTileColor(displayOnlyBoard.getTileColor()));
            }
        }
    }
}