package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.GUI.board.gameScreen.GameScreen;

public final class CancelButton extends TextButton {

    protected CancelButton(final GameScreen gameScreen, final Dialog dialog) {
        super("Cancel", GUI_UTILS.UI_SKIN);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                dialog.remove();
                gameScreen.getGameTimerPanel().continueTimer(true);
            }
        });
    }
}