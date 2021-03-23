package com.mygdx.game.GUI.gui.gameMenu;

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
import com.mygdx.game.GUI.gui.ChessGame;
import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.GUI.gui.GUI_UTILS;
import com.mygdx.game.GUI.gui.gameScreen.GameScreen;

import static com.mygdx.game.GUI.gui.GUI_UTILS.IS_SMARTPHONE;
import static com.mygdx.game.GUI.gui.GUI_UTILS.PAD;
import static com.mygdx.game.GUI.gui.GUI_UTILS.WIDTH;

public final class GameMenu extends TextButton {

    private final GameMenuDialog gameMenuDialog;

    public GameMenu(final ChessGame chessGame, final GameScreen gameScreen) {
        super("Game Menu", GUI_UTILS.UI_SKIN);
        this.gameMenuDialog = new GameMenuDialog(chessGame, gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(gameScreen.getStage());
            }});
    }
    
    public void detectKeyPressed(final GameScreen gameScreen) { this.gameMenuDialog.detectKeyPressed(gameScreen); }

    private static final class GameMenuDialog extends Dialog {

        private final GameButtonAbstract newGameButton, saveGameButton, loadGameButton, exitGameButton;

        private GameMenuDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            super("Game Menu", GUI_UTILS.UI_SKIN);
            this.newGameButton = new NewGameButton(gameScreen, this);
            this.saveGameButton = new SaveGameButton(gameScreen, this);
            this.loadGameButton = new LoadGameButton(gameScreen, this);
            this.exitGameButton = new ExitGameButton(chessGame, gameScreen, this);
            this.getContentTable().padTop(10);
            this.getContentTable().add(this.newGameButton).width(WIDTH).padBottom(PAD).padRight(PAD);
            this.getContentTable().add(this.saveGameButton).width(WIDTH).padBottom(PAD);
            this.getContentTable().row();
            this.getContentTable().add(this.loadGameButton).width(WIDTH).padBottom(PAD).padRight(PAD);
            this.getContentTable().add(this.exitGameButton).width(WIDTH).padBottom(PAD);
            this.getContentTable().row();
            this.getContentTable().add(new CancelButton(gameScreen, this)).padRight(-WIDTH).width(WIDTH);
        }

        private void detectKeyPressed(final GameScreen gameScreen) {
            this.newGameButton.detectKeyBoard(gameScreen);
            this.saveGameButton.detectKeyBoard(gameScreen);
            this.loadGameButton.detectKeyBoard(gameScreen);
            this.exitGameButton.detectKeyBoard(gameScreen);
        }
    }

    private abstract static class GameButtonAbstract extends TextButton {

        private GameButtonAbstract(final String text) {
            super(text, GUI_UTILS.UI_SKIN);
        }

        protected abstract void detectKeyBoard(final GameScreen gameScreen);
    }

    private final static class NewGameButton extends GameButtonAbstract {

        private final Dialog newGameDialog;

        private NewGameButton(final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(IS_SMARTPHONE ? "New Game" : "New Game (CTRL + N)");
            this.newGameDialog = this.generateDialog(gameScreen);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    newGameDialog.show(gameScreen.getStage());
                }});
        }

        @Override
        public void detectKeyBoard(final GameScreen gameScreen) {
            if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.N))
                    || (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(Input.Keys.N))) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    newGameDialog.show(gameScreen.getStage());
                }
            }
        }

        public Dialog generateDialog(final GameScreen gameScreen) {
            final NewGameButton.SetupTimer setupTimer = new NewGameButton.SetupTimer(gameScreen);
            final Label label = new Label("Request confirmation to start a new game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("New Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    setupTimer.show(gameScreen.getStage());
                    if ((Boolean) object) {
                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();
                    }
                }
            }.button("Yes", true)
                    .button("No", false)
                    .button("Cancel").text(label);
        }

        private static final class SetupTimer extends Dialog{

            private final SelectBox<TIMER_MINUTE> timer;
            private int minute;

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
                }, NO_TIMER {
                    @Override
                    int getMinute() { return -1; }
                    @Override
                    public String toString() { return "No Timer"; }
                };
                abstract int getMinute();
            }

            private SetupTimer(final GameScreen gameScreen) {
                super("Setup Timer", GUI_UTILS.UI_SKIN);
                this.timer = new SelectBox<>(GUI_UTILS.UI_SKIN);
                this.timer.setItems(TIMER_MINUTE.FIVE, TIMER_MINUTE.TEN, TIMER_MINUTE.FIFTEEN, TIMER_MINUTE.THIRTY, TIMER_MINUTE.FORTY_FIVE, TIMER_MINUTE.SIXTY, TIMER_MINUTE.NO_TIMER);
                this.getContentTable().padTop(10);
                this.getContentTable().add(this.timer).padBottom(20).row();
                this.minute = BoardUtils.DEFAULT_TIMER_MINUTE;
                this.getContentTable().add(new SetupButton(gameScreen,this, "Ok")).align(Align.bottomLeft);
                this.getContentTable().add(new SetupButton(gameScreen,this, "Cancel")).align(Align.bottomRight);
                this.addListener(new ChangeListener() {
                    @Override
                    public void changed(final ChangeEvent event, final Actor actor) {
                        minute = timer.getSelected().getMinute();
                    }
                });
            }

            private final static class SetupButton extends TextButton {

                private SetupButton(final GameScreen gameScreen, final SetupTimer setupTimer, final String text) {
                    super(text, GUI_UTILS.UI_SKIN);
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
                gameScreen.getGameBoard().setHumanPiece(null);
                gameScreen.getGameBoard().setAiMove(null);
                gameScreen.getGameBoard().setHumanMove(null);
                gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                gameScreen.getGameBoard().setGameEnd(false);
                gameScreen.getMoveHistory().updateMoveHistory();
                gameScreen.getGameTimerPanel().resetTimer(gameScreen.getChessBoard().whitePlayer(), gameScreen.getChessBoard().blackPlayer());
                gameScreen.getGameTimerPanel().continueTimer(true);
            }
        }
    }

    private final static class ExitGameButton extends GameButtonAbstract {

        private final Dialog exitGameDialog;

        private ExitGameButton(final ChessGame chessGame, final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(IS_SMARTPHONE ? "Exit Game" : "Exit Game (CTRL + X)");
            this.exitGameDialog = this.generateDialog(chessGame, gameScreen);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    exitGameDialog.show(gameScreen.getStage()); }
            });
        }

        @Override
        public void detectKeyBoard(final GameScreen gameScreen) {
            if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.X))
                    || (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(Input.Keys.X))) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    exitGameDialog.show(gameScreen.getStage());
                }
            }
        }

        public Dialog generateDialog(final ChessGame chessGame, final GameScreen gameScreen) {
            final Label label = new Label("Request confirmation to exit game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("Exit Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {
                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();
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

    private final static class SaveGameButton extends GameButtonAbstract {

        private final Dialog saveGameDialog;

        private SaveGameButton(final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(IS_SMARTPHONE ? "Save Game" : "Save Game (CTRL + S)");
            this.saveGameDialog = this.generateDialog(gameScreen);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    saveGameDialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        public void detectKeyBoard(final GameScreen gameScreen) {
            if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.S))
                    || (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(Input.Keys.S))) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    saveGameDialog.show(gameScreen.getStage());
                }
            }
        }

        public Dialog generateDialog(final GameScreen gameScreen) {
            final Label gameSavedLabel = new Label("Game Saved!", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Request confirmation to save game", GUI_UTILS.UI_SKIN);
            gameSavedLabel.setColor(Color.BLACK);
            label.setColor(Color.BLACK);
            return new Dialog("Save Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    if ((Boolean) object) {

                        GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                        GUI_UTILS.MOVE_LOG_PREF.flush();

                        new Dialog("Saved Game Message", GUI_UTILS.UI_SKIN) {
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

    private final static class LoadGameButton extends GameButtonAbstract {
        
        private final Dialog loadGameDialog;
        
        private LoadGameButton(final GameScreen gameScreen, final GameMenuDialog gameMenuDialog) {
            super(IS_SMARTPHONE ? "Load Game" : "Load Game (CTRL + L)");
            this.loadGameDialog = this.generateDialog(gameScreen);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameMenuDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    gameScreen.getGameBoard().getArtificialIntelligence().setStopAI(true);
                    loadGameDialog.show(gameScreen.getStage());
                }
            });
        }

        @Override
        public void detectKeyBoard(final GameScreen gameScreen) {
            if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.L))
                    || (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(Input.Keys.L))) {
                if (gameScreen.getGameTimerPanel().isTimerContinue()) {
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    loadGameDialog.show(gameScreen.getStage());
                }
            }
        }

        public Dialog generateDialog(final GameScreen gameScreen) {
            final Label label = new Label("Request confirmation to load saved game and save current one", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            return new Dialog("Load Saved Game Confirmation", GUI_UTILS.UI_SKIN) {
                @Override
                protected void result(final Object object) {
                    if (object == null) {
                        gameScreen.getGameTimerPanel().continueTimer(true);
                        return;
                    }
                    try {
                        final String moveHistory = GUI_UTILS.MOVE_LOG_PREF.getString(GUI_UTILS.MOVE_LOG_STATE);
                        if ((Boolean) object) {
                            GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(gameScreen.getMoveHistory().getMoveLog(), gameScreen.getChessBoard()));
                            GUI_UTILS.MOVE_LOG_PREF.flush();
                        }
                        gameScreen.updateChessBoard(FenUtilities.createGameFromSavedData(moveHistory, gameScreen.getMoveHistory().getMoveLog()));
                        gameScreen.getGameBoard().setAiMove(null);
                        gameScreen.getGameBoard().setHumanMove(null);
                        gameScreen.getMoveHistory().updateMoveHistory();
                        gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                        gameScreen.getGameBoard().setGameEnd(false);
                        gameScreen.getGameTimerPanel().resetTimer(gameScreen.getChessBoard().whitePlayer(), gameScreen.getChessBoard().blackPlayer());
                        final Label gameLoadedLabel = new Label("Game Loaded!", GUI_UTILS.UI_SKIN);
                        gameLoadedLabel.setColor(Color.BLACK);
                        new Dialog("Game Loaded Message", GUI_UTILS.UI_SKIN) {
                            @Override
                            protected void result(final Object object) {
                                this.remove();
                                gameScreen.getGameTimerPanel().continueTimer(true);
                            }
                        }.text(gameLoadedLabel).button("Ok").show(gameScreen.getStage());
                    } catch (final RuntimeException e) {
                        e.printStackTrace();
                        final Label label = new Label("No game to load", GUI_UTILS.UI_SKIN);
                        label.setColor(Color.BLACK);
                        new Dialog("Load Game", GUI_UTILS.UI_SKIN) {
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