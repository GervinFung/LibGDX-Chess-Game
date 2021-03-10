package com.mygdx.game.GUI.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GUI.board.gameScreen.GameScreen;
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

    private Dialog showProgressBar(final GameScreen gameScreen) {
        final Table table = new Table();
        progressBar.setRange(0, gameScreen.getChessBoard().currentPlayer().getLegalMoves().size());
        table.add(progressBar).width(400).padBottom(20).row();

        final Dialog dialog = new Dialog("Give me some time to think...", GUI_UTILS.UI_SKIN);

        final TextButton textButton = new TextButton("Remove Progress Bar",GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) { dialog.remove(); }
        });

        table.add(textButton);

        dialog.add(table);
        dialog.show(gameScreen.getStage());

        return dialog;
    }

    public void startAI(final GameScreen gameScreen) {
        final Dialog dialog;
        if (gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer()) &&
            gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer().getOpponent()) &&
            this.level.getSelected() < 3) {
            dialog = null;
        } else {
            dialog = this.showProgressBar(gameScreen);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                miniMax = new MiniMax(level.getSelected());
                final Move bestMove = miniMax.execute(gameScreen.getChessBoard());
                gameScreen.getGameBoard().setAiMove(bestMove);
                gameScreen.getGameBoard().setHumanMove(null);
                gameScreen.updateChessBoard(gameScreen.getChessBoard().currentPlayer().makeMove(bestMove).getLatestBoard());
                progressBar.setValue(miniMax.getMoveCount());
                if (!miniMax.getTerminateProcess()) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            gameScreen.getMoveHistory().getMoveLog().addMove(bestMove);
                            gameScreen.getMoveHistory().updateMoveHistory();
                            gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                            gameScreen.getGameBoard().fireGameSetupPropertyChangeSupport();
                            if (dialog != null) {
                                dialog.remove();
                            }
                        }
                    });
                }
                miniMax.setTerminateProcess(false);
                gameScreen.getGameBoard().setAIThinking(false);
            }
        }).start();
    }
}