package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.mygdx.game.GUI.board.gameScreen.GameScreen;

public final class GamePreference extends TextButton {

    private final GamePreferenceDialog gamePreferenceDialog;

    public GamePreference(final GameScreen gameScreen) {
        super("Game Preference", GUI_UTILS.UI_SKIN);
        this.gamePreferenceDialog = new GamePreferenceDialog(gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                gamePreferenceDialog.show(gameScreen.getStage());
            }});
    }

    public void detectUndoMoveKeyPressed(final GameScreen gameScreen) { this.gamePreferenceDialog.detectUndoMoveKeyPressed(gameScreen); }

    private static final class GamePreferenceDialog extends Dialog {

        private final UndoButton undoButton;

        private GamePreferenceDialog(final GameScreen gameScreen) {
            super("Game Preference", GUI_UTILS.UI_SKIN);
            final Table table = new Table();
            final int WIDTH = 300;
            table.add(new FlipBoardButton(gameScreen, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new BoardColorButton(gameScreen, this)).width(WIDTH).padBottom(20).row();
            table.add(new ExportFEN(gameScreen, this)).width(WIDTH).padBottom(20).padRight(20);
            table.add(new ImportFEN(gameScreen, this)).width(WIDTH).padBottom(20).row();
            this.undoButton = new UndoButton(gameScreen, this);
            table.add(this.undoButton).width(WIDTH).padRight(20);
            table.add(new CancelButton(gameScreen, this)).width(WIDTH);
            this.add(table);
        }

        private void detectUndoMoveKeyPressed(final GameScreen gameScreen) { this.undoButton.detectUndoMoveKeyPressed(gameScreen); }
    }

    private final static class ImportFEN extends TextButton {

        private ImportFEN(final GameScreen gameScreen, final GamePreferenceDialog gamePreferenceDialog) {
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
                                gameScreen.updateChessBoard(FenUtilities.createGameFromFEN(fenTextFiled.getText()));
                                gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                                gameScreen.getMoveHistory().getMoveLog().clear();
                                gameScreen.getMoveHistory().updateMoveHistory();
                                gameScreen.getGameTimerPanel().continueTimer(true);
                            } catch (final RuntimeException ignored) {
                                final Label label = new Label("Invalid FEN File Format.\nPlease try again", GUI_UTILS.UI_SKIN);
                                label.setColor(Color.BLACK);
                                new Dialog("Warning", GUI_UTILS.UI_SKIN) {
                                    @Override
                                    protected void result(final Object object) {
                                        gameScreen.getGameTimerPanel().continueTimer(true);
                                    }
                                }.button("Ok").text(label).show(gameScreen.getStage());
                            }
                        }
                    };
                    dialog.add(fenTextFiled).width(800);
                    dialog.button("Ok").show(gameScreen.getStage());
                }
            });
        }
    }

    private final static class ExportFEN extends TextButton {

        private ExportFEN(final GameScreen gameScreen, final GamePreferenceDialog gamePreferenceDialog) {
            super("Game to FEN Format", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    final Label fenLabel = new Label(FenUtilities.createFENFromGame(gameScreen.getChessBoard()), GUI_UTILS.UI_SKIN);
                    fenLabel.setColor(Color.BLACK);
                    new Dialog("FEN Format", GUI_UTILS.UI_SKIN) {
                        @Override
                        protected void result(final Object object) {
                            this.remove();
                            gameScreen.getGameTimerPanel().continueTimer(true);
                        }
                    }.button("Ok").text(fenLabel).show(gameScreen.getStage());
                }
            });
        }
    }

    private final static class UndoButton extends TextButton {

        private UndoButton(final GameScreen gameScreen, final GamePreferenceDialog gamePreferenceDialog) {
            super("Undo Move (CTRL + Z)", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    if (gameScreen.getMoveHistory().getMoveLog().size() > 0 && !gameScreen.getGameBoard().isGameEnd()) {
                        undoPlayerMove(gameScreen);
                    }
                    gameScreen.getGameTimerPanel().continueTimer(true);
                }
            });
        }

        private void detectUndoMoveKeyPressed(final GameScreen gameScreen) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.Z)
                    || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyPressed(Input.Keys.Z)) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                if (gameScreen.getMoveHistory().getMoveLog().size() > 0 && !gameScreen.getGameBoard().isGameEnd()) {
                    undoPlayerMove(gameScreen);
                }
                gameScreen.getGameTimerPanel().continueTimer(true);
            }
        }

        //If AI and player made move, undo both player move
        //If player made move but AI is thinking, undo player move, terminate AI
        //Otherwise, both player is human, undo that player move only
        private void undoPlayerMove(final GameScreen gameScreen) {
            if (gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer())
                && !gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer().getOpponent())) {
                gameScreen.getGameBoard().getArtificialIntelligence().setStopAI(true);
                this.undoMove(gameScreen);
            } else if (!gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer())
                    && gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer().getOpponent())) {
                gameScreen.getMoveHistory().getMoveLog().removeMove();
                this.undoMove(gameScreen);
            } else if (!gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer())
                    && !gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer().getOpponent())) {
                this.undoMove(gameScreen);
            }
        }

        private void undoMove(final GameScreen gameScreen) {
            final Move lastMove = gameScreen.getMoveHistory().getMoveLog().removeMove();
            gameScreen.updateChessBoard(gameScreen.getChessBoard().currentPlayer().undoMove(lastMove).getPreviousBoard());
            gameScreen.getGameBoard().setHumanMove(null);
            gameScreen.getGameBoard().setAiMove(null);
            gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
            gameScreen.getMoveHistory().updateMoveHistory();
        }
    }


    private static final class BoardColorButton extends TextButton {
        private BoardColorButton(final GameScreen gameScreen, final GamePreferenceDialog gamePreferenceDialog) {
            super("Board Color", GUI_UTILS.UI_SKIN);
            final Label label = new Label("Choose a Board Color", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            final Dialog dialog = new Dialog("Board Color", GUI_UTILS.UI_SKIN).text(label);
            dialog.getButtonTable().add(boardStyle(gameScreen, dialog));
            dialog.getContentTable().row();

            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);
                    dialog.show(gameScreen.getStage());
                }
            });
        }

        private Button[] boardStyle(final GameScreen gameScreen, final Dialog promoteDialog) {
            final Button[] buttons = new Button[6];
            for (int i = 0; i < 6; i++) {
                buttons[i] = new Button(new TextureRegionDrawable(GUI_UTILS.GET_TILE_TEXTURE_REGION(GUI_UTILS.BOARD_COLORS.get(i).toString())));
                final int finalI = i;
                buttons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        gameScreen.getDisplayOnlyBoard().setTileColor(GUI_UTILS.BOARD_COLORS.get(finalI));
                        gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                        promoteDialog.remove();
                        gameScreen.getGameTimerPanel().continueTimer(true);
                    }
                });
            }
            return buttons;
        }
    }

    private final static class FlipBoardButton extends TextButton {
        private FlipBoardButton(final GameScreen gameScreen, final GamePreferenceDialog gamePreferenceDialog) {
            super("Flip Board", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gamePreferenceDialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(false);

                    gameScreen.getGameBoard().changeBoardDirection();
                    gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());

                    gameScreen.getGameTimerPanel().changeTimerPanelDirection();
                    gameScreen.getGameTimerPanel().update(gameScreen);

                    gameScreen.getMoveHistory().changeMoveHistoryDirection();
                    gameScreen.getMoveHistory().updateMoveHistory();

                    gameScreen.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }
}