package com.mygdx.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.player.ArtificialIntelligence.MiniMax;
import com.mygdx.game.gui.board.GameProps;
import com.mygdx.game.gui.gameScreen.GameScreen;

import java.util.concurrent.Executors;

public final class ArtificialIntelligence {

    private final SelectBox<Integer> level;
    private final ProgressBar progressBar;
    private MiniMax miniMax;

    public ArtificialIntelligence() {
        this.level = new SelectBox<>(GuiUtils.UI_SKIN);
        this.level.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        this.progressBar = new ProgressBar(0, 1, 1, false, GuiUtils.UI_SKIN);
        this.progressBar.setColor(new Color(50 / 255f, 205 / 255f, 50 / 255f, 1));
        this.miniMax = new MiniMax(0);
    }

    public void setStopAI(final boolean stopAI) {
        this.miniMax.setTerminateProcess(stopAI);
    }

    public SelectBox<Integer> getLevelSelector() {
        return this.level;
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public int getMoveCount() {
        return this.miniMax.getMoveCount();
    }

    private Dialog showProgressBar(final GameScreen gameScreen) {
        final Table table = new Table();
        this.progressBar.setRange(0, gameScreen.getChessBoard().currentPlayer().getLegalMoves().size());
        table.add(this.progressBar).width(400).padBottom(20).row();

        final Dialog dialog = new Dialog("Give me some time to think...", GuiUtils.UI_SKIN);

        final TextButton textButton = new TextButton("Remove Progress Bar", GuiUtils.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                dialog.remove();
            }
        });

        table.add(textButton);

        dialog.add(table);
        dialog.show(gameScreen.getStage());

        return dialog;
    }

    public void startAI(final GameScreen gameScreen) {
        if (this.level.getSelected() < 0 || this.level.getSelected() > 10) {
            throw new IllegalStateException("AI range from 1 to 10 ONLY");
        }
        final Dialog dialog;
        if (gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer()) &&
                gameScreen.getGameBoard().isAIPlayer(gameScreen.getChessBoard().currentPlayer().getOpponent()) &&
                this.level.getSelected() < 3) {
            dialog = null;
        } else {
            dialog = this.showProgressBar(gameScreen);
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            this.miniMax = new MiniMax(this.level.getSelected());
            final Move bestMove = miniMax.execute(gameScreen.getChessBoard());
            gameScreen.getGameBoard().updateAiMove(bestMove);
            gameScreen.getGameBoard().updateHumanMove(null);
            if (!bestMove.equals(Move.MoveFactory.getNullMove())) {
                gameScreen.updateChessBoard(gameScreen.getChessBoard().currentPlayer().makeMove(bestMove).getLatestBoard());
            }
            this.progressBar.setValue(this.miniMax.getMoveCount());
            if (!this.miniMax.getTerminateProcess()) {
                Gdx.app.postRunnable(() -> {
                    gameScreen.getMoveHistory().getMoveLog().addMove(bestMove);
                    gameScreen.getMoveHistory().updateMoveHistory();
                    gameScreen.getGameBoard().drawBoard(gameScreen, gameScreen.getChessBoard(), gameScreen.getDisplayOnlyBoard());
                    gameScreen.getGameBoard().fireGameSetupPropertyChangeSupport();
                    if (dialog != null) {
                        dialog.remove();
                    }
                });
            }
            this.setStopAI(false);
            gameScreen.getGameBoard().updateArtificialIntelligenceWorking(GameProps.ArtificialIntelligenceWorking.RESTING);
        });
    }
}