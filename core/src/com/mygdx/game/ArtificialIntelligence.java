package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.chess.engine.board.Move;
import com.chess.engine.player.ArtificialIntelligence.MiniMax;

public final class ArtificialIntelligence{

    private final MyGdxGame myGdxGame;
    private final int level;

    public ArtificialIntelligence(final MyGdxGame myGdxGame, final int level) {
        this.myGdxGame = myGdxGame;
        this.myGdxGame.getGameBoard().setAIThinking(true);
        this.level = level;
    }

    public void startAI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something important here, asynchronously to the rendering thread
                // post a Runnable to the rendering thread that processes the result
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        final Move bestMove = new MiniMax(level).execute(myGdxGame.getChessBoard());
                        myGdxGame.getGameBoard().setAiMove(bestMove);
                        myGdxGame.getGameBoard().setHumanMove(null);
                        myGdxGame.updateChessBoard(myGdxGame.getChessBoard().currentPlayer().makeMove(bestMove).getLatestBoard());
                        myGdxGame.getMoveHistory().getMoveLog().addMove(bestMove);
                        myGdxGame.getMoveHistory().updateMoveHistory();
                        myGdxGame.getGameBoard().drawBoard(myGdxGame, myGdxGame.getGameBoard(), myGdxGame.getChessBoard(), myGdxGame.getDisplayOnlyBoard());
                        myGdxGame.getGameBoard().setAIThinking(false);
                        myGdxGame.getGameBoard().fireGameSetupPropertyChangeSupport();
                    }
                });
            }
        }) {

        }.start();
    }
}