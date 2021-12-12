package com.mygdx.game.chess.engine.board;

public final class MoveTransition {

    private final Board latestBoard, previousBoard;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board latestBoard, final Board previousBoard, final MoveStatus moveStatus) {
        this.latestBoard = latestBoard;
        this.previousBoard = previousBoard;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getPreviousBoard() {
        return this.previousBoard;
    }

    public Board getLatestBoard() {
        return this.latestBoard;
    }
}