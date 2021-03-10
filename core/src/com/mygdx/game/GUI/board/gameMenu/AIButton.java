package com.mygdx.game.GUI.board.gameMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.GUI.board.gameScreen.GameScreen;

public final class AIButton extends TextButton {

    public AIButton(final GameScreen gameScreen) {
        super("Setup AI", GUI_UTILS.UI_SKIN);
        final AIDialog aiDialog = new AIDialog(gameScreen);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                gameScreen.getGameTimerPanel().continueTimer(false);
                aiDialog.show(gameScreen.getStage());
            }
        });
    }

    private static final class AIDialog extends Dialog {

        private final CheckBox whitePlayerCheckBox, blackPlayerCheckBox;

        private AIDialog(final GameScreen gameScreen) {
            super("Setup AI", GUI_UTILS.UI_SKIN);
            final Table table = new Table();

            this.whitePlayerCheckBox = new CheckBox("White as AI", GUI_UTILS.UI_SKIN);
            this.blackPlayerCheckBox = new CheckBox("Black as AI", GUI_UTILS.UI_SKIN);

            table.add(this.whitePlayerCheckBox).align(Align.left).row();
            table.add(this.blackPlayerCheckBox).align(Align.left).row();

            final Label label = new Label("Select Level", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            table.add(label);
            table.add(gameScreen.getGameBoard().getArtificialIntelligence().getLevelSelector()).row();

            table.add(new OKButton(gameScreen, this)).align(Align.left);
            table.add(new CancelButton(gameScreen, this)).align(Align.right);

            this.add(table);
        }

        private final static class OKButton extends TextButton {

            public OKButton(final GameScreen gameScreen, final AIDialog aiDialog) {
                super("Ok", GUI_UTILS.UI_SKIN);
                this.addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        aiDialog.remove();
                        gameScreen.getGameBoard().changeWhitePlayerType(aiDialog.whitePlayerCheckBox.isChecked());
                        gameScreen.getGameBoard().changeBlackPlayerType(aiDialog.blackPlayerCheckBox.isChecked());
                        if (!gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer())) {
                            gameScreen.getGameBoard().getArtificialIntelligence().setStopAI(true);
                        }
                        gameScreen.getGameBoard().fireGameSetupPropertyChangeSupport();
                        gameScreen.getGameTimerPanel().continueTimer(true);
                    }
                });
            }
        }
    }
}