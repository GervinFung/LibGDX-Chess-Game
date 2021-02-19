package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.MyGdxGame;

public final class GamePreference extends TextButton {

    public GamePreference(final MyGdxGame myGdxGame) {
        super("Game Preference", GUI_UTILS.UI_SKIN);
        final GamePreferenceDialog gamePreferenceDialog = new GamePreferenceDialog(myGdxGame);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                myGdxGame.getGameTimerPanel().continueTimer(false);
                gamePreferenceDialog.show(myGdxGame.getStage());
            }});
    }

    private static final class GamePreferenceDialog extends Dialog {

        private GamePreferenceDialog(final MyGdxGame myGdxGame) {
            super("Game Preference", GUI_UTILS.UI_SKIN);
            final Table table = new Table();
            final int WIDTH = 300;
            table.add(new FlipBoardButton(myGdxGame, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new BoardColorButton(myGdxGame, this)).width(WIDTH).padBottom(20).row();
            table.add(new ExportFEN(myGdxGame, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new ImportFEN(myGdxGame, this)).width(WIDTH).padBottom(20).row();
            table.add(new UndoButton(myGdxGame, this)).width(WIDTH).padRight(20);
            table.add(new CancelButton(myGdxGame, this)).width(WIDTH);
            this.add(table);
        }
    }

    private final static class ImportFEN extends TextButton {

        private ImportFEN(final MyGdxGame myGdxGame, final GamePreferenceDialog gamePreferenceDialog) {
            super("Game from FEN Format", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    final TextField fenTextFiled = new TextField(null, GUI_UTILS.UI_SKIN);
                    fenTextFiled.setMessageText("FEN Format");
                    fenTextFiled.setWidth(300);
                    final Dialog dialog = new Dialog("FEN Format", GUI_UTILS.UI_SKIN) {
                        @Override
                        protected void result(final Object object) {
                            try {
                                myGdxGame.updateChessBoard(FenUtilities.createGameFromFEN(fenTextFiled.getText()));
                                myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                                myGdxGame.getMoveHistory().getMoveLog().clear();
                                myGdxGame.getMoveHistory().updateMoveHistory();
                                myGdxGame.getGameTimerPanel().continueTimer(true);
                            } catch (final RuntimeException ignored) {
                                final Label label = new Label("Invalid FEN File Format.\nPlease try again", GUI_UTILS.UI_SKIN);
                                label.setColor(Color.BLACK);
                                new Dialog("Warning", GUI_UTILS.UI_SKIN) {
                                    @Override
                                    protected void result(final Object object) {
                                        myGdxGame.getGameTimerPanel().continueTimer(true);
                                    }
                                }.button("Ok").text(label).show(myGdxGame.getStage());
                            }
                        }
                    };
                    dialog.add(fenTextFiled).width(800);
                    dialog.button("Ok").show(myGdxGame.getStage());
                }
            });
        }
    }

    private final static class ExportFEN extends TextButton {

        private ExportFEN(final MyGdxGame myGdxGame, final GamePreferenceDialog gamePreferenceDialog) {
            super("Game to FEN Format", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    final Label fenLabel = new Label(FenUtilities.createFENFromGame(myGdxGame.getChessBoard()), GUI_UTILS.UI_SKIN);
                    fenLabel.setColor(Color.BLACK);
                    new Dialog("FEN Format", GUI_UTILS.UI_SKIN) {
                        @Override
                        protected void result(final Object object) {
                            this.remove();
                            myGdxGame.getGameTimerPanel().continueTimer(true);
                        }
                    }.button("Ok").text(fenLabel).show(myGdxGame.getStage());
                }
            });
        }
    }

    private final static class UndoButton extends TextButton {

        private UndoButton(final MyGdxGame myGdxGame, final GamePreferenceDialog gamePreferenceDialog) {
            super("Undo Move", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    if (myGdxGame.getMoveHistory().getMoveLog().size() > 0 && !myGdxGame.getGameBoard().isGameEnd()) {
                        final Move lastMove = myGdxGame.getMoveHistory().getMoveLog().removeMove();
                        myGdxGame.updateChessBoard(myGdxGame.getChessBoard().currentPlayer().undoMove(lastMove).getPreviousBoard());
                        myGdxGame.getGameBoard().setHumanMove(null);
                        myGdxGame.getGameBoard().setAiMove(null);
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        myGdxGame.getMoveHistory().updateMoveHistory();
                    }
                    myGdxGame.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }


    private static final class BoardColorButton extends TextButton {
        private BoardColorButton(final MyGdxGame myGdxGame, final GamePreferenceDialog gamePreferenceDialog) {
            super("Board Color", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Choose a Board Color", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Board Color", GUI_UTILS.UI_SKIN).text(label);
            dialog.getButtonTable().add(boardStyle(myGdxGame, dialog));
            dialog.getContentTable().row();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }
            });
        }

        private Button[] boardStyle(final MyGdxGame myGdxGame, final Dialog promoteDialog) {
            final Button[] buttons = new Button[6];
            for (int i = 0; i < 6; i++) {
                buttons[i] = new Button(new TextureRegionDrawable(GUI_UTILS.GET_TILE_TEXTURE_REGION(GUI_UTILS.BOARD_COLORS.get(i).toString())));
                final int finalI = i;
                buttons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        myGdxGame.getDisplayOnlyBoard().setTileColor(GUI_UTILS.BOARD_COLORS.get(finalI));
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        promoteDialog.remove();
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                    }
                });
            }
            return buttons;
        }
    }

    private final static class FlipBoardButton extends TextButton {
        private FlipBoardButton(final MyGdxGame myGdxGame, final GamePreferenceDialog gamePreferenceDialog) {
            super("Flip Board", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);

                    myGdxGame.getGameBoard().changeBoardDirection();
                    myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());

                    myGdxGame.getGameTimerPanel().changeTimerPanelDirection();
                    myGdxGame.getGameTimerPanel().update(myGdxGame);

                    myGdxGame.getMoveHistory().changeMoveHistoryDirection();
                    myGdxGame.getMoveHistory().updateMoveHistory();

                    myGdxGame.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }
}
