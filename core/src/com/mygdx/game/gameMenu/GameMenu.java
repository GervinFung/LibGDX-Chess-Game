package com.mygdx.game.gameMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.chess.engine.FEN.FenUtilities;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.mygdx.game.GUI_UTILS;
import com.mygdx.game.MyGdxGame;

import java.util.List;

public enum GameMenu {;

    public final static class NewGameButton extends TextButton {

        public NewGameButton(final MyGdxGame myGdxGame) {
            super("New Game", GUI_UTILS.UTILS.UI_SKIN);
            final SetupTimer setupTimer = new SetupTimer(myGdxGame);
            final Label label = new Label("Request confirmation to start a new game and save current one", GUI_UTILS.UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("New Game Confirmation", GUI_UTILS.UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    setupTimer.show(myGdxGame.getStage());
                    if ((Boolean) object) {
                        GUI_UTILS.UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.UTILS.MOVE_LOG_PREF.flush();
                    }
                    this.remove();
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel").text(label);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }});
        }

        private static final class SetupTimer extends Dialog{

            private final SelectBox<TIMER_MINUTE> timer;
            private int minute;
            private final MyGdxGame myGdxGame;

            private enum TIMER_MINUTE {
                DEFAULT {
                    @Override
                    int getMinute() { return 30; }
                    @Override
                    public String toString() { return "Default (30 minutes)"; }
                },
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

            public SetupTimer(final MyGdxGame myGdxGame) {
                super("Setup Timer", GUI_UTILS.UTILS.UI_SKIN);
                this.myGdxGame = myGdxGame;
                this.timer = new SelectBox<>(GUI_UTILS.UTILS.UI_SKIN);
                this.timer.setItems(TIMER_MINUTE.DEFAULT, TIMER_MINUTE.FIVE, TIMER_MINUTE.TEN, TIMER_MINUTE.FIFTEEN, TIMER_MINUTE.THIRTY, TIMER_MINUTE.FORTY_FIVE, TIMER_MINUTE.SIXTY);
                this.add(this.timer);
                this.minute = BoardUtils.UTILS.DEFAULT_TIMER_MINUTE;
                this.button("Ok", true).button("Cancel", null);
                this.addListener(new ChangeListener() {
                    @Override
                    public void changed(final ChangeEvent event, final Actor actor) { minute = timer.getSelected().getMinute(); }
                });
            }

            @Override
            protected void result(final Object object) {
                if (object == null) {
                    restartGame(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
                    this.remove();
                    return;
                }
                this.restartGame(this.minute, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
                this.remove();
            }

            private void restartGame(final int minute, final int second, final int millisecond) {
                this.myGdxGame.updateChessBoard(Board.createStandardBoard(minute, second, millisecond));
                this.myGdxGame.getMoveHistory().getMoveLog().clear();
                this.myGdxGame.getGameBoard().setAiMove(null);
                this.myGdxGame.getGameBoard().setHumanMove(null);
                this.myGdxGame.getGameBoard().drawBoard(this.myGdxGame, this.myGdxGame.getGameBoard(), this.myGdxGame.getChessBoard(), this.myGdxGame.getDisplayOnlyBoard());
                this.myGdxGame.getGameBoard().setGameEnd(false);
                this.myGdxGame.getMoveHistory().updateMoveHistory();
                this.myGdxGame.getGameTimerPanel().resetTimer(this.myGdxGame.getChessBoard().whitePlayer(), this.myGdxGame.getChessBoard().blackPlayer());
                this.myGdxGame.getGameTimerPanel().continueTimer(true);
            }
        }
    }

    public final static class ExitGameButton extends TextButton {
        public ExitGameButton(final MyGdxGame myGdxGame) {
            super("Exit Game", GUI_UTILS.UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to exit game and save current one", GUI_UTILS.UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Exit Game Confirmation", GUI_UTILS.UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {
                        GUI_UTILS.UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.UTILS.MOVE_LOG_PREF.flush();
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
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage()); }
            });
        }
    }

    public final static class SaveGameButton extends TextButton {
        public SaveGameButton(final MyGdxGame myGdxGame) {
            super("Save Game", GUI_UTILS.UTILS.UI_SKIN);
            final Label gameSavedLabel = new Label("Game Saved!", GUI_UTILS.UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to save game", GUI_UTILS.UTILS.UI_SKIN);
            gameSavedLabel.setColor(Color.BLACK);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Save Game Confirmation", GUI_UTILS.UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {

                        GUI_UTILS.UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                        GUI_UTILS.UTILS.MOVE_LOG_PREF.flush();

                        new Dialog("Saved Game Message", GUI_UTILS.UTILS.UI_SKIN) {
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
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }
            });

        }
    }

    public final static class LoadGameButton extends TextButton {
        public LoadGameButton(final MyGdxGame myGdxGame) {
            super("Load Game", GUI_UTILS.UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to load saved game and save current one", GUI_UTILS.UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Load Saved Game Confirmation", GUI_UTILS.UTILS.UI_SKIN) {
                        @Override
                        protected void result(final Object object) {
                            if (object == null) {
                                myGdxGame.getGameTimerPanel().continueTimer(true);
                                return;
                            }
                            try {
                                final String moveHistory = GUI_UTILS.UTILS.MOVE_LOG_PREF.getString(GUI_UTILS.UTILS.MOVE_LOG_STATE);
                                if ((Boolean) object) {
                                    GUI_UTILS.UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
                                    GUI_UTILS.UTILS.MOVE_LOG_PREF.flush();
                                    //myGdxGame.getMoveHistoryRetrieval().writeMoveToFiles(myGdxGame.getMoveHistory().getMoveLog());
                                }
                                myGdxGame.updateChessBoard(FenUtilities.createGameFromSavedData(moveHistory, myGdxGame.getMoveHistory().getMoveLog()));
                                myGdxGame.getGameBoard().setAiMove(null);
                                myGdxGame.getGameBoard().setHumanMove(null);
                                myGdxGame.getMoveHistory().updateMoveHistory();
                                myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getGameBoard(), myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                                myGdxGame.getGameBoard().setGameEnd(false);
                                myGdxGame.getGameTimerPanel().resetTimer(myGdxGame.getChessBoard().whitePlayer(), myGdxGame.getChessBoard().blackPlayer());
                                myGdxGame.getGameTimerPanel().continueTimer(true);
                            } catch (final RuntimeException e) {
                                e.printStackTrace();
                                final Label label = new Label("Unfortunately, there is no game to load", GUI_UTILS.UTILS.UI_SKIN);
                                label.setColor(Color.BLACK);
                                new Dialog("Load Game", GUI_UTILS.UTILS.UI_SKIN) {
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
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }
            });
        }
    }

    public static final class BoardColorButton extends TextButton {
        public BoardColorButton(final MyGdxGame myGdxGame) {
            super("Board Color", GUI_UTILS.UTILS.UI_SKIN);
            final Label label = new Label("Choose a Board Color", GUI_UTILS.UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Board Color", GUI_UTILS.UTILS.UI_SKIN).text(label);
            dialog.getButtonTable().add(boardStyle(myGdxGame, GUI_UTILS.UTILS.BOARD_COLORS, dialog));
            dialog.getContentTable().row();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    dialog.show(myGdxGame.getStage());
                }
            });
        }

        private Button[] boardStyle (final MyGdxGame myGdxGame, final List<GUI_UTILS.TILE_COLOR> boardColors, final Dialog promoteDialog) {
            final Button[] buttons = new Button[6];
            for (int i = 0; i < 6; i++) {
                buttons[i] = new Button(new TextureRegionDrawable(GUI_UTILS.UTILS.GET_TILE_TEXTURE_REGION(boardColors.get(i).toString())));
                final int finalI = i;
                buttons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        myGdxGame.getDisplayOnlyBoard().setTileColor(boardColors.get(finalI));
                        promoteDialog.remove();
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                    }
                });
            }
            return buttons;
        }
    }

    public final static class FlipBoardButton extends TextButton {
        public FlipBoardButton(final MyGdxGame myGdxGame) {
            super("Flip Board", GUI_UTILS.UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameTimerPanel().continueTimer(false);

                    myGdxGame.getGameBoard().changeBoardDirection();
                    myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getGameBoard(), myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());

                    myGdxGame.getGameTimerPanel().changeTimerPanelDirection();
                    myGdxGame.getGameTimerPanel().update(myGdxGame);

                    myGdxGame.getMoveHistory().changeMoveHistoryDirection();
                    myGdxGame.getMoveHistory().updateMoveHistory();

                    myGdxGame.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }

    public final static class UndoButton extends TextButton {

        public UndoButton(final MyGdxGame myGdxGame) {
            super("Undo Move", GUI_UTILS.UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    if (myGdxGame.getMoveHistory().getMoveLog().size() > 0 && !myGdxGame.getGameBoard().isGameEnd()) {
                        final Move lastMove = myGdxGame.getMoveHistory().getMoveLog().removeMove();
                        myGdxGame.updateChessBoard(myGdxGame.getChessBoard().currentPlayer().undoMove(lastMove).getPreviousBoard());
                        myGdxGame.getGameBoard().setHumanMove(null);
                        myGdxGame.getGameBoard().setAiMove(null);
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getGameBoard(), myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        myGdxGame.getMoveHistory().updateMoveHistory();
                    }
                }
            });
        }
    }

    public final static class AIButton extends TextButton {

        private final AIDialog aiDialog;

        public AIButton(final MyGdxGame myGdxGame) {
            super("Setup AI", GUI_UTILS.UTILS.UI_SKIN);
            this.aiDialog = new AIDialog(myGdxGame);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameTimerPanel().continueTimer(false);
                    aiDialog.show(myGdxGame.getStage());
                }
            });
        }

        public int getLevel() { return this.aiDialog.getLevel(); }

        private static final class AIDialog extends Dialog {

            private final MyGdxGame myGdxGame;
            private final SelectBox<Integer> level;

            public AIDialog(final MyGdxGame myGdxGame) {
                super("Setup AI", GUI_UTILS.UTILS.UI_SKIN);
                this.myGdxGame = myGdxGame;
                this.button("Ok", true).button("Cancel").row();
                this.add(new PlayerCheckBox("White as AI", myGdxGame));
                this.add(new PlayerCheckBox("Black as AI", myGdxGame)).row();
                this.add("Select Level");
                this.level = new SelectBox<>(GUI_UTILS.UTILS.UI_SKIN);
                level.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                this.add(level);
            }

            public int getLevel() { return this.level.getSelected(); }

            @Override
            protected void result(final Object object) {
                if (object == null) {
                    myGdxGame.getGameTimerPanel().continueTimer(true);
                    return;
                }
                if ((Boolean)object) { myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport(); }
            }

            private final static class PlayerCheckBox extends CheckBox {

                public PlayerCheckBox(final String text, final MyGdxGame myGdxGame) {
                    super(text, GUI_UTILS.UTILS.UI_SKIN);
                    this.setChecked(false);

                    this.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            if ("White as AI".equals(text)) {
                                myGdxGame.getGameBoard().changeWhitePlayerType();
                            } else if ("Black as AI".equals(text)) {
                                myGdxGame.getGameBoard().changeBlackPlayerType();
                            }
                        }
                    });
                }
            }
        }
    }
}