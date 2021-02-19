package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.MyGdxGame;

public final class GameMenu extends TextButton {

    public GameMenu(final MyGdxGame myGdxGame) {
        super("Game Menu", GUI_UTILS.UI_SKIN);
        final GameMenuDialog gameMenuDialog = new GameMenuDialog(myGdxGame);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                myGdxGame.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(myGdxGame.getStage());
            }});
    }

    private static final class GameMenuDialog extends Dialog {

        private GameMenuDialog(final MyGdxGame myGdxGame) {
            super("Game Menu", GUI_UTILS.UI_SKIN);
            final Table table = new Table();
            final int WIDTH = 150;
            table.add(new NewGameButton(myGdxGame, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new SaveGameButton(myGdxGame, this)).width(WIDTH).padBottom(20).row();
            table.add(new LoadGameButton(myGdxGame, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new ExitGameButton(myGdxGame, this)).width(WIDTH).padBottom(20).row();
            table.add(new CancelButton(myGdxGame, this)).padRight(-150).width(WIDTH);
            this.add(table);
        }
    }

    private final static class NewGameButton extends TextButton {

        private NewGameButton(final MyGdxGame myGdxGame, final GameMenuDialog gameMenuDialog) {
            super("New Game", GUI_UTILS.UI_SKIN);
            final NewGameButton.SetupTimer setupTimer = new NewGameButton.SetupTimer(myGdxGame);
            final Label label = new Label("Request confirmation to start a new game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("New Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    setupTimer.show(myGdxGame.getStage());
                    if ((Boolean) object) {
                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();
                    }
                    this.remove();
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel").text(label);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }});
        }

        private static final class SetupTimer extends Dialog{

            private final SelectBox<NewGameButton.SetupTimer.TIMER_MINUTE> timer;
            private int minute;
            private final MyGdxGame myGdxGame;

            private enum TIMER_MINUTE {
                FIVE {
                    @Override
                    int getMinute() { return 5; }
                    @Override
                    public String toString() { return "5 minutes"; }
                }, TEN {
                    @Override
                    int getMinute() { return 10; }
                    @Override
                    public String toString() { return "10 minutes"; }
                }, FIFTEEN {
                    @Override
                    int getMinute() { return 15; }
                    @Override
                    public String toString() { return "15 minutes"; }
                }, THIRTY {
                    @Override
                    int getMinute() { return 30; }
                    @Override
                    public String toString() { return "30 minutes"; }
                }, FORTY_FIVE {
                    @Override
                    int getMinute() { return 45; }
                    @Override
                    public String toString() { return "45 minutes"; }
                }, SIXTY {
                    @Override
                    int getMinute() { return 60; }
                    @Override
                    public String toString() { return "60 minutes"; }
                };
                abstract int getMinute();
            }

            private SetupTimer(final MyGdxGame myGdxGame) {
                super("Setup Timer", GUI_UTILS.UI_SKIN);
                final Table table = new Table();
                this.myGdxGame = myGdxGame;
                this.timer = new SelectBox<>(GUI_UTILS.UI_SKIN);
                this.timer.setItems(NewGameButton.SetupTimer.TIMER_MINUTE.FIVE, NewGameButton.SetupTimer.TIMER_MINUTE.TEN, NewGameButton.SetupTimer.TIMER_MINUTE.FIFTEEN, NewGameButton.SetupTimer.TIMER_MINUTE.THIRTY, NewGameButton.SetupTimer.TIMER_MINUTE.FORTY_FIVE, NewGameButton.SetupTimer.TIMER_MINUTE.SIXTY);
                table.add(this.timer).padBottom(20).row();
                this.minute = BoardUtils.DEFAULT_TIMER_MINUTE;
                table.add(new SetupButton(myGdxGame,this, "Ok")).align(Align.bottomLeft);
                table.add(new SetupButton(myGdxGame,this, "Cancel")).align(Align.bottomRight);
                this.add(table);
                this.addListener(new ChangeListener() {
                    @Override
                    public void changed(final ChangeEvent event, final Actor actor) {
                        minute = timer.getSelected().getMinute();
                    }
                });
            }

            private final static class SetupButton extends TextButton {

                private SetupButton(final MyGdxGame myGdxGame, final SetupTimer setupTimer, final String text) {
                    super(text, GUI_UTILS.UI_SKIN);
                    this.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            setupTimer.remove();
                            myGdxGame.getGameBoard().getArtificialIntelligence().setStopAI(true);
                            setupTimer.restartGame(setupTimer.minute);
                        }
                    });
                }

            }

            private void restartGame(final int minute) {
                this.myGdxGame.updateChessBoard(Board.createStandardBoard(minute, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND));
                this.myGdxGame.getMoveHistory().getMoveLog().clear();
                this.myGdxGame.getGameBoard().setAiMove(null);
                this.myGdxGame.getGameBoard().setHumanMove(null);
                this.myGdxGame.getGameBoard().drawBoard(this.myGdxGame, this.myGdxGame.getChessBoard(), this.myGdxGame.getDisplayOnlyBoard());
                this.myGdxGame.getGameBoard().setGameEnd(false);
                this.myGdxGame.getMoveHistory().updateMoveHistory();
                this.myGdxGame.getGameTimerPanel().resetTimer(this.myGdxGame.getChessBoard().whitePlayer(), this.myGdxGame.getChessBoard().blackPlayer());
                this.myGdxGame.getGameTimerPanel().continueTimer(true);
            }
        }
    }

    private final static class ExitGameButton extends TextButton {
        private ExitGameButton(final MyGdxGame myGdxGame, final GameMenuDialog gameMenuDialog) {
            super("Exit Game", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to exit game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Exit Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {
                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();
                    }
                    this.remove();
                    Gdx.app.exit();
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel")
                    .text(label);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage()); }
            });
        }
    }

    private final static class SaveGameButton extends TextButton {
        private SaveGameButton(final MyGdxGame myGdxGame, final GameMenuDialog gameMenuDialog) {
            super("Save Game", GUI_UTILS.UI_SKIN);
            final Label gameSavedLabel = new Label("Game Saved!", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to save game", GUI_UTILS.UI_SKIN);
            gameSavedLabel.setColor(Color.BLACK);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Save Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {

                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();

                        new Dialog("Saved Game Message", GUI_UTILS.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                myGdxGame.getGameTimerPanel().continueTimer(true);
                                this.remove();
                            }
                        }.text(gameSavedLabel).button("Ok").show(myGdxGame.getStage());
                    }
                    this.remove();
                }
            }.button("Yes", true)
                    .button("No", false)
                    .text(label);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }
            });

        }
    }

    private final static class LoadGameButton extends TextButton {
        private LoadGameButton(final MyGdxGame myGdxGame, final GameMenuDialog gameMenuDialog) {
            super("Load Game", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to load saved game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Load Saved Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    try {
                        final String moveHistory = GUI_UTILS.MOVE_LOG_PREF.getString(GUI_UTILS.MOVE_LOG_STATE);
                        if ((Boolean) object) {
                            GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                            GUI_UTILS.MOVE_LOG_PREF.flush();
                        }
                        myGdxGame.updateChessBoard(FenUtilities.createGameFromSavedData(moveHistory, myGdxGame.getMoveHistory().getMoveLog()));
                        myGdxGame.getGameBoard().setAiMove(null);
                        myGdxGame.getGameBoard().setHumanMove(null);
                        myGdxGame.getMoveHistory().updateMoveHistory();
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        myGdxGame.getGameBoard().setGameEnd(false);
                        myGdxGame.getGameTimerPanel().resetTimer(myGdxGame.getChessBoard().whitePlayer(), myGdxGame.getChessBoard().blackPlayer());
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                    } catch (final RuntimeException e) {
                        e.printStackTrace();
                        final Label label = new Label("Unfortunately, there is no game to load", GUI_UTILS.UI_SKIN);
                        label.setColor(Color.BLACK);
                        new Dialog("Load Game", GUI_UTILS.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                myGdxGame.getGameTimerPanel().continueTimer(true);
                            }
                        }.text(label).button("Ok").show(myGdxGame.getStage());
                    }
                }
            }.button("Yes", true).button("No", false)
                    .button("Cancel")
                    .text(label);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    myGdxGame.getGameBoard().getArtificialIntelligence().setStopAI(true);
                    dialog.show(myGdxGame.getStage());
                }
            });
        }
    }
}