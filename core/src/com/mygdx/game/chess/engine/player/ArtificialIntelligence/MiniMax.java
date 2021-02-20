package com.mygdx.game.chess.engine.player.ArtificialIntelligence;

import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.player.Player;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Collection;
import java.util.Comparator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;

public final class MiniMax {

    private final StandardBoardEvaluation evaluator;
    private final int searchDepth, nThreads;
    private int quiescenceCount;
    private static final int MAX_QUIESCENCE = 5000 * 5;
    private volatile boolean terminateProcess;
    private final AtomicInteger moveCount;

    private enum MoveSorter {

        STANDARD_SORT {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from(new Comparator<Move>() {
                    @Override
                    public int compare(final Move move1, final Move move2) {
                        return ComparisonChain.start()
                                .compareTrueFirst(move1.isCastlingMove(), move2.isCastlingMove())
                                .compare(BoardUtils.mostValuableVictimLeastValuableAggressor(move2), BoardUtils.mostValuableVictimLeastValuableAggressor(move1))
                                .result();
                    }
                }).immutableSortedCopy(moves);
            }
        },

        EXPENSIVE_SORT {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from(new Comparator<Move>() {
                    @Override
                    public int compare(final Move move1, final Move move2) {
                        return ComparisonChain.start()
                                .compareTrueFirst(BoardUtils.kingThreat(move1), BoardUtils.kingThreat(move2))
                                .compareTrueFirst(move1.isCastlingMove(), move2.isCastlingMove())
                                .compare(BoardUtils.mostValuableVictimLeastValuableAggressor(move2), BoardUtils.mostValuableVictimLeastValuableAggressor(move1))
                                .result();
                    }
                }).immutableSortedCopy(moves);
            }
        };

        abstract Collection<Move> sort(final Collection<Move> moves);
    }


    public MiniMax(final int searchDepth) {
        this.evaluator = new StandardBoardEvaluation();
        this.nThreads = Runtime.getRuntime().availableProcessors();
        if (this.nThreads > 4) {
            this.searchDepth = searchDepth + 1;
        } else {
            this.searchDepth = searchDepth;
        }
        this.quiescenceCount = 0;
        this.moveCount = new AtomicInteger(0);
        this.terminateProcess = false;
    }

    public Move execute(final Board board) {
        final Player currentPlayer = board.currentPlayer();
        final AtomicInteger highestSeenValue = new AtomicInteger(Integer.MIN_VALUE);
        final AtomicInteger lowestSeenValue = new AtomicInteger(Integer.MAX_VALUE);
        final AtomicInteger currentValue = new AtomicInteger(0);

        final AtomicBoolean isCheckMate = new AtomicBoolean(false);

        final AtomicReference<Move> bestMove = new AtomicReference<>();

        final ExecutorService executorService = Executors.newFixedThreadPool(this.nThreads);

        for (final Move move : MoveSorter.EXPENSIVE_SORT.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            this.quiescenceCount = 0;

            if (isCheckMate.get()) {
                break;
            }
            if (moveTransition.getMoveStatus().isDone()) {
                executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            final int currentVal = currentPlayer.getLeague().isWhite() ?
                                    min(moveTransition.getLatestBoard(), MiniMax.this.searchDepth - 1, highestSeenValue.get(), lowestSeenValue.get()) :
                                    max(moveTransition.getLatestBoard(), MiniMax.this.searchDepth - 1, highestSeenValue.get(), lowestSeenValue.get());

                            currentValue.set(currentVal);
                            if (MiniMax.this.terminateProcess) {
                                //immediately set move to null after time out for AI
                                bestMove.set(Move.MoveFactory.getNullMove());
                            }
                            if (currentPlayer.getLeague().isWhite() && currentValue.get() > highestSeenValue.get()) {
                                highestSeenValue.set(currentValue.get());
                                bestMove.set(move);
                                if(moveTransition.getLatestBoard().blackPlayer().isInCheckmate()) {
                                    isCheckMate.set(true);
                                }
                            }
                            else if (currentPlayer.getLeague().isBlack() && currentValue.get() < lowestSeenValue.get()) {
                                lowestSeenValue.set(currentValue.get());
                                bestMove.set(move);
                                if(moveTransition.getLatestBoard().whitePlayer().isInCheckmate()) {
                                    isCheckMate.set(true);
                                }
                            }
                            moveCount.set(moveCount.get() + 1);
                        }
                    }
                );
            }

        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return bestMove.get();
    }

    //setter
    public void setTerminateProcess(final boolean terminateProcess) { this.terminateProcess = terminateProcess; }
    //getter
    public boolean getTerminateProcess() { return this.terminateProcess; }
    public int getMoveCount() { return this.moveCount.get(); }

    private int max(final Board board, final int depth, final int highest, final int lowest) {
        if (this.terminateProcess) {
            return highest;
        }
        if (depth == 0 || BoardUtils.isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : MoveSorter.STANDARD_SORT.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getLatestBoard();
                currentHighest = Math.max(currentHighest, min(toBoard,
                        calculateQuiescenceDepth(toBoard, depth), currentHighest, lowest));
                if (currentHighest >= lowest) {
                    return lowest;
                }
            }
        }
        return currentHighest;
    }

    private int min(final Board board, final int depth, final int highest, final int lowest) {
        if (this.terminateProcess) {
            return lowest;
        }
        if (depth == 0 || BoardUtils.isEndGameScenario(board)) {
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : MoveSorter.STANDARD_SORT.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getLatestBoard();
                currentLowest = Math.min(currentLowest, max(toBoard,
                        calculateQuiescenceDepth(toBoard, depth), highest, currentLowest));
                if (currentLowest <= highest) {
                    return highest;
                }
            }
        }
        return currentLowest;
    }

    private int calculateQuiescenceDepth(final Board toBoard, final int depth) {
        if(depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.currentPlayer().isInCheck()) {
                activityMeasure += 1;
            }
            for(final Move move: BoardUtils.lastNMoves(toBoard, 2)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }
}