package com.mygdx.game.GUI.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.player.ArtificialIntelligence.MiniMax;

public final class ArtificialIntelligence{

    private final MyGdxGame myGdxGame;
    private final SelectBox<Integer> level;
    private MiniMax miniMax;

    public ArtificialIntelligence(final MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        this.level = new SelectBox<>(GUI_UTILS.UI_SKIN);
        level.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        miniMax = new MiniMax(0);
    }

    public void setStopAI(final boolean stopAI) { this.miniMax.setTerminateProcess(stopAI); }

    public SelectBox<Integer> getLevelSelector() { return this.level; }

    public void startAI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                miniMax = new MiniMax(level.getSelected());
                final Move bestMove = miniMax.execute(myGdxGame.getChessBoard());
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
                            myGdxGame.getGameBoard().setAIThinking(false);
                            myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport();
                        }
                    });
                }
                miniMax.setTerminateProcess(false);
            }
        }).start();
    }
}