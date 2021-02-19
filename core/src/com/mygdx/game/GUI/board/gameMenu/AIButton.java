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
import com.mygdx.game.MyGdxGame;

public final class AIButton extends TextButton {

    public AIButton(final MyGdxGame myGdxGame) {
        super("Setup AI", GUI_UTILS.UI_SKIN);
        final AIDialog aiDialog = new AIDialog(myGdxGame);
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                myGdxGame.getGameTimerPanel().continueTimer(false);
                aiDialog.show(myGdxGame.getStage());
            }
        });
    }

    private static final class AIDialog extends Dialog {

        private AIDialog(final MyGdxGame myGdxGame) {
            super("Setup AI", GUI_UTILS.UI_SKIN);
            final Table table = new Table();

            table.add(new AIDialog.PlayerCheckBox("White as AI", myGdxGame)).align(Align.left).row();
            table.add(new AIDialog.PlayerCheckBox("Black as AI", myGdxGame)).align(Align.left).row();

            final Label label = new Label("Select Level", GUI_UTILS.UI_SKIN);
            label.setColor(Color.BLACK);
            table.add(label);
            table.add(myGdxGame.getGameBoard().getArtificialIntelligence().getLevelSelector()).row();

            table.add(new AIDialog.OKButton(myGdxGame, this)).align(Align.left);
            table.add(new CancelButton(myGdxGame, this)).align(Align.right);

            this.add(table);
        }

        private final static class OKButton extends TextButton {

            public OKButton(final MyGdxGame myGdxGame, final AIDialog aiDialog) {
                super("Ok", GUI_UTILS.UI_SKIN);
                this.addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        aiDialog.remove();
                        myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport();
                        myGdxGame.getGameTimerPanel().continueTimer(true);
                    }
                });
            }
        }

        private final static class PlayerCheckBox extends CheckBox {

            private PlayerCheckBox(final String text, final MyGdxGame myGdxGame) {
                super(text, GUI_UTILS.UI_SKIN);
                this.setChecked(false);

                this.addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        if ("White as AI".equals(text)) {
                            myGdxGame.getGameBoard().changeWhitePlayerType();
                            if (!myGdxGame.getGameBoard().isAIPlayer(myGdxGame.getChessBoard().currentPlayer())) {
                                myGdxGame.getGameBoard().getArtificialIntelligence().setStopAI(true);
                            }
                        } else if ("Black as AI".equals(text)) {
                            myGdxGame.getGameBoard().changeBlackPlayerType();
                            if (!myGdxGame.getGameBoard().isAIPlayer(myGdxGame.getChessBoard().currentPlayer())) {
                                myGdxGame.getGameBoard().getArtificialIntelligence().setStopAI(true);
                            }
                        }
                    }
                });
            }
        }
    }
}
