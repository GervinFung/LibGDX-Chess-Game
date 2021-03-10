package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.GUI.board.gameScreen.GameScreen;

public final class GameOption extends TextButton {

    public GameOption(final GameScreen gameScreen) {
        super("Game Option", GUI_UTILS.UI_SKIN);
        final GameOptionDialog gameMenuDialog = new GameOptionDialog(gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(gameScreen.getStage());
            }});
    }

    private static final class GameOptionDialog extends Dialog {

        private GameOptionDialog(final GameScreen gameScreen) {
            super("Game Option", GUI_UTILS.UI_SKIN);
            final Table table = new Table();
            table.add(new HighlightLegalMove(gameScreen)).align(Align.left).padBottom(20).row();
            table.add(new ShowPreviousMove(gameScreen)).align(Align.left).padBottom(20).row();
            table.add(new PauseTimer(gameScreen)).align(Align.left).padBottom(20).row();
            table.add(new OKButton(gameScreen, this)).align(Align.left);
            table.add(new CancelButton(gameScreen, this)).align(Align.right);
            this.add(table);
        }
    }

    private final static class OKButton extends TextButton {

        public OKButton(final GameScreen gameScreen, final GameOptionDialog gameOptionDialog) {
            super("Ok", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameOptionDialog.remove();
                    gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                    gameScreen.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }

    private final static class PauseTimer extends CheckBox {

        public PauseTimer(final GameScreen gameScreen) {
            super("Pause Timer", GUI_UTILS.UI_SKIN);
            this.setChecked(false);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameScreen.getGameTimerPanel().setPauseTimerOption(isChecked());
                }
            });
        }
    }

    private final static class HighlightLegalMove extends CheckBox {

        private HighlightLegalMove(final GameScreen gameScreen) {
            super("Highlight Legal Move", GUI_UTILS.UI_SKIN);
            this.setChecked(true);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameScreen.getGameBoard().setHighlightMove(isChecked());
                }
            });
        }
    }

    private final static class ShowPreviousMove extends CheckBox {

        private ShowPreviousMove (final GameScreen gameScreen) {
            super("Highlight Previous Move", GUI_UTILS.UI_SKIN);
            this.setChecked(true);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameScreen.getGameBoard().setHighlightPreviousMove(isChecked());
                }
            });
        }
    }
}