package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.MyGdxGame;

public final class CancelButton extends TextButton {

    protected CancelButton(final MyGdxGame myGdxGame, final Dialog dialog) {
        super("Cancel", GUI_UTILS.UI_SKIN);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
                myGdxGame.getGameTimerPanel().continueTimer(true);
            }
        });
    }
}
