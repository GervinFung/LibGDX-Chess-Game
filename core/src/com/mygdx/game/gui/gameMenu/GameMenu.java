package com.mygdx.game.gui.gameMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.gui.ChessGame;
import com.mygdx.game.gui.GuiUtils;
import com.mygdx.game.gui.board.GameProps;
import com.mygdx.game.gui.gameScreen.GameScreen;

import java.util.Arrays;
import java.util.List;

public final class GameMenu extends TextButton {

    private final GameMenuDialog gameMenuDialog;

    public GameMenu(final ChessGame chessGame, final GameScreen gameScreen) {
        super("Game Menu", GuiUtils.UI_SKIN);
        this.gameMenuDialog = new GameMenuDialog(chessGame, gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(gameScreen.getStage());
            }
        });
    }

    public void detectKeyPressed(final GameScreen gameScreen) {
        this.gameMenuDialog.detectKeyPressed(gameScreen);
    }

    private static final class GameMenuDialog extends Dialog {

        private final List<GameButtonAbstract> gameButtonAbstractList;

        private GameMenuDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            super("Game Menu", GuiUtils.UI_SKIN);

            this.gameButtonAbstractList = Arrays.asList(
                    new NewGameButton(chessGame, gameScreen, this),
                    new SaveGameButton(chessGame, gameScreen, this),
                    new LoadGameButton(chessGame, gameScreen, this),
                    new ExitGameButton(chessGame, gameScreen, this)
            );

            this.getContentTable().padTop(10);

            for (int i = 0; i < this.gameButtonAbstractList.size(); i++) {
                this.getContentTable().add(this.gameButtonAbstractList.get(i)).width(GuiUtils.WIDTH).padBottom(GuiUtils.PAD).padRight(GuiUtils.PAD);
                if (i % 2 != 0) {
                    this.getContentTable().row();
                }
            }

            this.getContentTable().add(new CancelButton(gameScreen, this)).padRight(-GuiUtils.WIDTH).width(GuiUtils.WIDTH);
        }

        private void detectKeyPressed(final GameScreen gameScreen) {
            for (final GameButtonAbstract gameButton : this.gameButtonAbstractList) {
                gameButton.detectKeyBoard(gameScreen);
            }
        }
    }

    private static abstract class GameButtonAbstract extends TextButton {

        private final Dialog dialog;

        private GameButtonAbstract(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog, final String text) {
            super(text, GuiUtils.UI_SKIN);
            this.dialog = this.generateDialog(chessGame, gameScreen);
        }

        protected abstract void detectKeyBoard(final GameScreen gameScreen);

        private boolean specialKeyPressed(final int specialKey) {
            final boolean leftKeyPressed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(specialKey);
            final boolean rightKeyPressed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(specialKey);
            return leftKeyPressed || rightKeyPressed;
        }

        protected abstract Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen);
    }

    private static final class NewGameButton extends GameButtonAbstract {

        private NewGameButton(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(chessGame, gameScreen, gameMenuDialog, GuiUtils.IS_SMARTPHONE ? "New Game" : "New Game (CTRL + N)");
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    NewGameButton.super.dialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        protected void detectKeyBoard(final GameScreen gameScreen) {
            if (super.specialKeyPressed(Input.Keys.N)) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    super.dialog.show(gameScreen.getStage());
                }
            }
        }

        @Override
        protected Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            return this.generateDialog(gameScreen);
        }

        private Dialog generateDialog(final GameScreen gameScreen) {
            final NewGameButton.SetupTimer setupTimer = new NewGameButton.SetupTimer(gameScreen);
            final Label label = new Label("Request confirmation to start a new game and save current one", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("New Game Confirmation", GuiUtils.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    setupTimer.show(gameScreen.getStage());
                    if ((Boolean) object) {
                        GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GuiUtils.MOVE_LOG_PREF.flush();
                    }
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel").text(label);
        }


        private static final class SetupTimer extends Dialog {

            private final SelectBox<TimerMinute> timer;
            private int minute;

            private enum TimerMinute {
                FIVE {
                    @Override
                    int getMinute() {
                        return 5;
                    }

                    @Override
                    public String toString() {
                        return "5 minutes";
                    }
                }, TEN {
                    @Override
                    int getMinute() {
                        return 10;
                    }

                    @Override
                    public String toString() {
                        return "10 minutes";
                    }
                }, FIFTEEN {
                    @Override
                    int getMinute() {
                        return 15;
                    }

                    @Override
                    public String toString() {
                        return "15 minutes";
                    }
                }, THIRTY {
                    @Override
                    int getMinute() {
                        return 30;
                    }

                    @Override
                    public String toString() {
                        return "30 minutes";
                    }
                }, FORTY_FIVE {
                    @Override
                    int getMinute() {
                        return 45;
                    }

                    @Override
                    public String toString() {
                        return "45 minutes";
                    }
                }, SIXTY {
                    @Override
                    int getMinute() {
                        return 60;
                    }

                    @Override
                    public String toString() {
                        return "60 minutes";
                    }
                }, NO_TIMER {
                    @Override
                    int getMinute() {
                        return -1;
                    }

                    @Override
                    public String toString() {
                        return "No Timer";
                    }
                };

                abstract int getMinute();
            }

            private SetupTimer(final GameScreen gameScreen) {
                super("Setup Timer", GuiUtils.UI_SKIN);
                this.timer = new SelectBox<>(GuiUtils.UI_SKIN);
                this.timer.setItems(TimerMinute.FIVE, TimerMinute.TEN, TimerMinute.FIFTEEN, TimerMinute.THIRTY, TimerMinute.FORTY_FIVE, TimerMinute.SIXTY, TimerMinute.NO_TIMER);
                this.getContentTable().padTop(10);
                this.getContentTable().add(this.timer).padBottom(20).row();
                this.minute = BoardUtils.DEFAULT_TIMER_MINUTE;
                this.getContentTable().add(new SetupButton(gameScreen, this, "Ok")).align(Align.bottomLeft);
                this.getContentTable().add(new SetupButton(gameScreen, this, "Cancel")).align(Align.bottomRight);
                this.addListener(new ChangeListener() {
                    @Override
                    public void changed(final ChangeEvent event, final Actor actor) {
                        minute = timer.getSelected().getMinute();
                    }
                });
            }

            private static final class SetupButton extends TextButton {

                private SetupButton(final GameScreen gameScreen, final SetupTimer setupTimer, final String text) {
                    super(text, GuiUtils.UI_SKIN);
                    this.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            setupTimer.remove();
                            gameScreen.getGameBoard().getArtificialIntelligence().setStopAI(true);
                            setupTimer.restartGame(gameScreen, setupTimer.minute);
                        }
                    });
                }
            }

            private void restartGame(final GameScreen gameScreen, final int minute) {
                gameScreen.updateChessBoard(Board.createStandardBoard(minute, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND));
                gameScreen.getMoveHistory().getMoveLog().clear();
                gameScreen.getGameBoard().updateHumanPiece(null);
                gameScreen.getGameBoard().updateAiMove(null);
                gameScreen.getGameBoard().updateHumanMove(null);
                gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                gameScreen.getGameBoard().updateGameEnd(GameProps.GameEnd.ONGOING);
                gameScreen.getMoveHistory().updateMoveHistory();
                gameScreen.getGameTimerPanel().resetTimer(gameScreen.getChessBoard().whitePlayer(), gameScreen.getChessBoard().blackPlayer());
                gameScreen.getGameTimerPanel().continueTimer(true);
            }
        }
    }

    private static final class ExitGameButton extends GameButtonAbstract {

        private ExitGameButton(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(chessGame, gameScreen, gameMenuDialog, GuiUtils.IS_SMARTPHONE ? "Exit Game" : "Exit Game (CTRL + X)");
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    ExitGameButton.super.dialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        protected void detectKeyBoard(final GameScreen gameScreen) {
            if (super.specialKeyPressed(Input.Keys.X)) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    super.dialog.show(gameScreen.getStage());
                }
            }
        }

        @Override
        public Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            final Label label = new Label("Request confirmation to exit game and save current one", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("Exit Game Confirmation", GuiUtils.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {
                        GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GuiUtils.MOVE_LOG_PREF.flush();
                    }
                    this.remove();
                    Gdx.input.setInputProcessor(chessGame.getWelcomeScreen().getStage());
                    chessGame.setScreen(chessGame.getWelcomeScreen());
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel")
                    .text(label);
        }
    }

    private static final class SaveGameButton extends GameButtonAbstract {

        private SaveGameButton(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(chessGame, gameScreen, gameMenuDialog, GuiUtils.IS_SMARTPHONE ? "Save Game" : "Save Game (CTRL + S)");
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    SaveGameButton.super.dialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        protected void detectKeyBoard(final GameScreen gameScreen) {
            if (super.specialKeyPressed(Input.Keys.S)) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    super.dialog.show(gameScreen.getStage());
                }
            }
        }

        @Override
        protected Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            return this.generateDialog(gameScreen);
        }

        private Dialog generateDialog(final GameScreen gameScreen) {
            final Label gameSavedLabel = new Label("Game Saved!", GuiUtils.UI_SKIN);
            final Label label = new Label("Request confirmation to save game", GuiUtils.UI_SKIN);
            gameSavedLabel.setColor(Color.BLACK);
            label.setColor(Color.BLACK);
            return new Dialog("Save Game Confirmation", GuiUtils.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {

                        GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GuiUtils.MOVE_LOG_PREF.flush();

                        new Dialog("Saved Game Message", GuiUtils.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                gameScreen.getGameTimerPanel().continueTimer(true);
                                this.remove();
                            }
                        }.text(gameSavedLabel).button("Ok").show(gameScreen.getStage());
                    }
                }
            }.button("Yes", true)
                    .button("No")
                    .text(label);
        }
    }

    private static final class LoadGameButton extends GameButtonAbstract {

        private LoadGameButton(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(chessGame, gameScreen, gameMenuDialog, GuiUtils.IS_SMARTPHONE ? "Load Game" : "Load Game (CTRL + L)");
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    gameScreen.getGameBoard().getArtificialIntelligence().setStopAI(true);
                    LoadGameButton.super.dialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        protected void detectKeyBoard(final GameScreen gameScreen) {
            if (super.specialKeyPressed(Input.Keys.L)) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    super.dialog.show(gameScreen.getStage());
                }
            }
        }

        @Override
        protected Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            return this.generateDialog(gameScreen);
        }

        private Dialog generateDialog(final GameScreen gameScreen) {
            final Label label = new Label("Request confirmation to load saved game and save current one", GuiUtils.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("Load Saved Game Confirmation", GuiUtils.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    try {
                        final String moveHistory = GuiUtils.MOVE_LOG_PREF.getString(GuiUtils.MOVE_LOG_STATE);
                        if ((Boolean) object) {
                            GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                            GuiUtils.MOVE_LOG_PREF.flush();
                        }
                        gameScreen.updateChessBoard(FenUtilities.createGameFromSavedData(moveHistory, gameScreen.getMoveHistory().getMoveLog()));
                        gameScreen.getGameBoard().updateAiMove(null);
                        gameScreen.getGameBoard().updateHumanMove(null);
                        gameScreen.getMoveHistory().updateMoveHistory();
                        gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                        gameScreen.getGameBoard().updateGameEnd(GameProps.GameEnd.ONGOING);
                        gameScreen.getGameTimerPanel().resetTimer(gameScreen.getChessBoard().whitePlayer(), gameScreen.getChessBoard().blackPlayer());
                        final Label gameLoadedLabel = new Label("Game Loaded!", GuiUtils.UI_SKIN);
                        gameLoadedLabel.setColor(Color.BLACK);
                        new Dialog("Game Loaded Message", GuiUtils.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                this.remove();
                                gameScreen.getGameTimerPanel().continueTimer(true);
                            }
                        }.text(gameLoadedLabel).button("Ok").show(gameScreen.getStage());
                    } catch (final RuntimeException e) {
                        e.printStackTrace();
                        final Label label = new Label("No game to load", GuiUtils.UI_SKIN);
                        label.setColor(Color.BLACK);
                        new Dialog("Load Game", GuiUtils.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                gameScreen.getGameTimerPanel().continueTimer(true);
                            }
                        }.text(label).button("Ok").show(gameScreen.getStage());
                    }
                }
            }.button("Yes", true).button("No", false)
                    .button("Cancel")
                    .text(label);
        }
    }
}