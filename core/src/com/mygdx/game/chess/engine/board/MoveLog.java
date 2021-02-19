package com.mygdx.game.chess.engine.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class MoveLog implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 3L;

    private final List<Move> moves;

    public MoveLog() { this.moves = new ArrayList<>(); }

    public List<Move> getMoves() { return this.moves; }
    public void addMove(final Move move) { this.moves.add(move); }
    public Move get(final int i) { return this.moves.get(i); }
    public int size() { return this.moves.size(); }
    public void clear() { this.moves.clear(); }
    public Move removeMove() { return this.moves.remove(this.moves.size() - 1); }

    @Override
    public String toString() { return this.moves.toString(); }
}