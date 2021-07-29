package com.mygdx.game.gui.gameMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.gui.GuiUtils;
import com.mygdx.game.gui.board.GameProps;
import com.mygdx.game.gui.gameScreen.GameScreen;

import java.util.Arrays;
import java.util.List;

public final class GameOption extends TextButton {

    public GameOption(final GameScreen gameScreen) {
        super("Game Option", GuiUtils.UI_SKIN);
        final GameOptionDialog gameMenuDialog = new GameOptionDialog(gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                gameMenuDialog.show(gameScreen.getStage());
            }
        });
    }

    private static final class GameOptionDialog extends Dialog {

        private GameOptionDialog(final GameScreen gameScreen) {
            super("Game Option", GuiUtils.UI_SKIN);
            this.getContentTable().padTop(10);
            final List<GameOptionCheckBox> gameOptionCheckBoxList = Arrays.asList(new HighlightLegalMove(gameScreen), new ShowPreviousMove(gameScreen), new PauseTimer(gameScreen));
            for (final GameOptionCheckBox gameOptionCheckBox : gameOptionCheckBoxList) {
                this.getContentTable().add(gameOptionCheckBox).align(Align.left).padBottom(20).row();
            }
            this.getContentTable().add(new OKButton(gameScreen, this, gameOptionCheckBoxList)).align(Align.left);
            this.getContentTable().add(new CancelButton(gameScreen, this)).align(Align.right);
        }
    }

    private static final class OKButton extends TextButton {

        protected OKButton(final GameScreen gameScreen, final Dialog dialog, final List<GameOptionCheckBox> gameOptionCheckBoxList) {
            super("Ok", GuiUtils.UI_SKIN);
            this.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                    dialog.remove();
                    gameScreen.getGameTimerPanel().continueTimer(true);
                    for (final GameOptionCheckBox gameOptionCheckBox : gameOptionCheckBoxList) {
                        gameOptionCheckBox.update();
                    }
                }
            });
        }
    }

    private static abstract class GameOptionCheckBox extends CheckBox {

        private final GameScreen gameScreen;

        protected GameOptionCheckBox(final GameScreen gameScreen, final String text, final boolean commonState) {
            super(text, GuiUtils.UI_SKIN);
            this.gameScreen = gameScreen;
            this.setChecked(commonState);
        }

        protected abstract void update();
    }

    private static final class PauseTimer extends GameOptionCheckBox {

        protected PauseTimer(final GameScreen gameScreen) {
            super(gameScreen, "Pause Timer", false);
        }

        @Override
        protected void update() {
            super.gameScreen.getGameTimerPanel().setPauseTimerOption(isChecked());
        }
    }

    private static final class HighlightLegalMove extends GameOptionCheckBox {

        protected HighlightLegalMove(final GameScreen gameScreen) {
            super(gameScreen, "Highlight Legal Move", true);
        }

        @Override
        protected void update() {
            super.gameScreen.getGameBoard().updateHighlightMove(GameProps.HighlightMove.getHighlightMoveState(isChecked()));
        }
    }

    private static final class ShowPreviousMove extends GameOptionCheckBox {

        protected ShowPreviousMove (final GameScreen gameScreen) {
            super(gameScreen, "Highlight Previous Move", true);
        }

        @Override
        protected void update() {
            super.gameScreen.getGameBoard().updateHighlightPreviousMove(GameProps.HighlightPreviousMove.getHighlightPreviousMoveState(isChecked()));
        }
    }
}