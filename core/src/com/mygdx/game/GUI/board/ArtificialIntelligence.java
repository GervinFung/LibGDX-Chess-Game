package com.mygdx.game.GUI.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.player.ArtificialIntelligence.MiniMax;

public final class ArtificialIntelligence{

    private final SelectBox<Integer> level;
    private final ProgressBar progressBar;
    private MiniMax miniMax;

    public ArtificialIntelligence() {
        this.level = new SelectBox<>(GUI_UTILS.UI_SKIN);
        level.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        progressBar = new ProgressBar(0, 1, 1, false, GUI_UTILS.UI_SKIN);
        progressBar.setColor(new Color(50/255f, 205/255f, 50/255f, 1));
        miniMax = new MiniMax(0);
    }

    public void setStopAI(final boolean stopAI) { this.miniMax.setTerminateProcess(stopAI); }

    public SelectBox<Integer> getLevelSelector() { return this.level; }
    public ProgressBar getProgressBar() { return this.progressBar; }
    public int getMoveCount() { return this.miniMax.getMoveCount(); }

    private Dialog showProgressBar(final MyGdxGame myGdxGame) {
        final Table table = new Table();
        progressBar.setRange(0, myGdxGame.getChessBoard().currentPlayer().getLegalMoves().size());
        table.add(progressBar).width(400).padBottom(20).row();

        final Dialog dialog = new Dialog("Give me some time to think...", GUI_UTILS.UI_SKIN);

        final TextButton textButton = new TextButton("Ok",GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                dialog.remove();
            }
        });

        table.add(textButton).align(Align.bottomLeft);

        dialog.add(table);
        dialog.show(myGdxGame.getStage());

        return dialog;
    }

    public void startAI(final MyGdxGame myGdxGame) {
        final Dialog dialog = this.showProgressBar(myGdxGame);
        new Thread(new Runnable() {
            @Override
            public void run() {
                miniMax = new MiniMax(level.getSelected());
                final Move bestMove = miniMax.execute(myGdxGame.getChessBoard());
                progressBar.setValue(miniMax.getMoveCount());
                if (!miniMax.getTerminateProcess()) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            if (myGdxGame.getGameBoard().isHighlightPreviousMove()) {
                                myGdxGame.getGameBoard().setAiMove(bestMove);
                            }
                            myGdxGame.getGameBoard().setHumanMove(null);
                            myGdxGame.updateChessBoard(myGdxGame.getChessBoard().currentPlayer().makeMove(bestMove).getLatestBoard());
                            myGdxGame.getMoveHistory().getMoveLog().addMove(bestMove);
                            myGdxGame.getMoveHistory().updateMoveHistory();
                            myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                            myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport();
                        }
                    });
                }
                miniMax.setTerminateProcess(false);
                myGdxGame.getGameBoard().setAIThinking(false);
                dialog.remove();
            }
        }).start();
    }
}