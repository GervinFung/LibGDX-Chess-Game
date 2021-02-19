package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.MyGdxGame;

public final class GameOption extends TextButton {

    public GameOption(final MyGdxGame myGdxGame) {
        super("Game Option", GUI_UTILS.UI_SKIN);
        final GameOptionDialog gameMenuDialog = new GameOptionDialog(myGdxGame);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                myGdxGame.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(myGdxGame.getStage());
            }});
    }

    private static final class GameOptionDialog extends Dialog {

        private GameOptionDialog(final MyGdxGame myGdxGame) {
            super("Game Option", GUI_UTILS.UI_SKIN);
            final Table table = new Table();
            table.add(new HighlightLegalMove(myGdxGame)).align(Align.left).padBottom(20).row();
            table.add(new ShowPreviousMove(myGdxGame)).align(Align.left).padBottom(20).row();
            table.add(new PauseTimer(myGdxGame)).align(Align.left).padBottom(20).row();
            table.add(new OKButton(myGdxGame, this)).align(Align.left);
            table.add(new CancelButton(myGdxGame, this)).align(Align.right);
            this.add(table);
        }
    }

    private final static class OKButton extends TextButton {

        public OKButton(final MyGdxGame myGdxGame, final GameOptionDialog gameOptionDialog) {
            super("Ok", GUI_UTILS.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameOptionDialog.remove();
                    myGdxGame.getGameTimerPanel().continueTimer(true);
                }
            });
        }
    }

    private final static class PauseTimer extends CheckBox {

        public PauseTimer(final MyGdxGame myGdxGame) {
            super("Pause Timer", GUI_UTILS.UI_SKIN);
            this.setChecked(false);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameTimerPanel().setPauseTimerOption(isChecked());
                }
            });
        }
    }

    private final static class HighlightLegalMove extends CheckBox {

        private HighlightLegalMove(final MyGdxGame myGdxGame) {
            super("Highlight Legal Move", GUI_UTILS.UI_SKIN);
            this.setChecked(true);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameBoard().setHighlightMove(isChecked());
                }
            });
        }
    }

    private final static class ShowPreviousMove extends CheckBox {

        private ShowPreviousMove (final MyGdxGame myGdxGame) {
            super("Highlight Previous Move", GUI_UTILS.UI_SKIN);
            this.setChecked(true);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    myGdxGame.getGameBoard().setHighlightPreviousMove(isChecked());
                }
            });
        }
    }
}