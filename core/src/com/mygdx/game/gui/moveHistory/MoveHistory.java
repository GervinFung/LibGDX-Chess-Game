package com.mygdx.game.gui.moveHistory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveLog;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public final class MoveHistory extends Table {
    
    private final static int SIZE = GuiUtils.GAME_BOARD_SR_SIZE / 2;

    private final Table table;
    private final TakenPiece whiteTakenPiece, blackTakenPiece;
    private final MoveLog moveLog;
    private TakenPieceDirection takenPieceDirection;

    public MoveHistory() {
        this.setVisible(true);

        this.takenPieceDirection = TakenPieceDirection.NORMAL;

        this.moveLog = new MoveLog();

        this.whiteTakenPiece = new MoveHistory.TakenPiece(League.WHITE, GuiUtils.WHITE_CAPTURED);
        this.blackTakenPiece = new MoveHistory.TakenPiece(League.BLACK, GuiUtils.BLACK_CAPTURED);

        this.table = new Table(GuiUtils.UI_SKIN);
        this.table.align(Align.topLeft);
        this.add(this.whiteTakenPiece).size(SIZE, 75).row();
        final ScrollPane scrollPane = new ScrollPane(this.table);
        scrollPane.setScrollbarsVisible(true);
        this.add(scrollPane).size(SIZE, 450).row();
        this.add(this.blackTakenPiece).size(SIZE, 75);
    }

    private enum TakenPieceDirection {
        NORMAL {
            @Override
            void redo(final MoveHistory moveHistory) {
                moveHistory.clearChildren();
                moveHistory.add(moveHistory.whiteTakenPiece).size(MoveHistory.SIZE, 75).row();
                moveHistory.add(new ScrollPane(moveHistory.table)).size(MoveHistory.SIZE, 450).row();
                moveHistory.add(moveHistory.blackTakenPiece).size(MoveHistory.SIZE, 75);
            }

            @Override
            TakenPieceDirection getOpposite() { return FLIPPED; }
        }, FLIPPED {
            @Override
            void redo(final MoveHistory moveHistory) {
                moveHistory.clearChildren();
                moveHistory.add(moveHistory.blackTakenPiece).size(MoveHistory.SIZE, 75).row();
                moveHistory.add(new ScrollPane(moveHistory.table)).size(MoveHistory.SIZE, 450).row();
                moveHistory.add(moveHistory.whiteTakenPiece).size(MoveHistory.SIZE, 75);
            }

            @Override
            TakenPieceDirection getOpposite() { return NORMAL; }
        };
        abstract void redo(final MoveHistory moveHistory);
        abstract TakenPieceDirection getOpposite();

        private void updateMoveHistory(final MoveHistory moveHistory) {
            moveHistory.table.clearChildren();
            int i = 0, j = 1;
            for (final Move move : moveHistory.moveLog.getMoves()) {
                final Table table = new Table(GuiUtils.UI_SKIN);
                table.add(++i + ") " + move.toString());
                if ((j % 2 != 0 && i % 2 != 0) || (j % 2 == 0 && i % 2 == 0)) {
                    table.setBackground(GuiUtils.MOVE_HISTORY_1);
                } else {
                    table.setBackground(GuiUtils.MOVE_HISTORY_2);
                }
                table.align(Align.left);
                moveHistory.table.add(table).size(MoveHistory.SIZE / 2f, 50);
                if (i % 2 == 0) {
                    moveHistory.table.row();
                    j++;
                }
            }
            moveHistory.whiteTakenPiece.updateTakenPiece(moveHistory.moveLog);
            moveHistory.blackTakenPiece.updateTakenPiece(moveHistory.moveLog);
        }
    }

    public void changeMoveHistoryDirection() {
        this.takenPieceDirection = this.takenPieceDirection.getOpposite();
        this.takenPieceDirection.redo(this);
    }

    public void updateMoveHistory() { this.takenPieceDirection.updateMoveHistory(this); }

    public MoveLog getMoveLog() { return this.moveLog; }

    private static final class TakenPiece extends Table {

        private final League league;

        private TakenPiece(final League league, final NinePatchDrawable ninePatchDrawable) {
            super(GuiUtils.UI_SKIN);
            this.league = league;
            this.setVisible(true);
            this.align(Align.bottomLeft);
            this.setBackground(ninePatchDrawable);
        }

        private void updateTakenPiece(final MoveLog moveLog) {

            final HashMap<Piece, Integer> takenPieces = new HashMap<>();

            for (final Move move: moveLog.getMoves()) {
                if (move.isAttack()) {
                    final Piece takenPiece = move.getAttackedPiece();
                    if (takenPiece.getLeague() == this.league) {
                        final Piece piece = this.searchSamePiece(takenPieces, takenPiece);
                        if (piece == null) {
                            takenPieces.put(takenPiece, 1);
                        } else {
                            final int quantity = takenPieces.get(piece) + 1;
                            takenPieces.remove(piece);
                            takenPieces.put(takenPiece, quantity);
                        }
                    }
                }
            }

            this.addTakenPiece(takenPieces);

            this.validate();
        }

        private Piece searchSamePiece(final HashMap<Piece, Integer> takenPieces, final Piece takenPiece) {
            for (final Piece piece : takenPieces.keySet()) {
                if (takenPiece.toString().equals(piece.toString())) {
                    return piece;
                }
            }
            return null;
        }

        private List<Piece> sortedPieces(final HashMap<Piece, Integer> takenPiecesMap) {
            final List<Piece> unsortedPieces = new ArrayList<>(takenPiecesMap.keySet());
            Collections.sort(unsortedPieces, new Comparator<Piece>() {
                @Override
                public int compare(final Piece piece1, final Piece piece2) {
                    if (piece1.getPieceValue() > piece2.getPieceValue()) { return 1; }
                    else if (piece1.getPieceValue() < piece2.getPieceValue()) { return -1; }
                    return 0;
                }
            });
            return Collections.unmodifiableList(unsortedPieces);
        }

        private void addTakenPiece(final HashMap<Piece, Integer> takenPiecesMap) {
            final List<Piece> takenPieces = this.sortedPieces(takenPiecesMap);
            this.clearChildren();
            for (final Piece takenPiece : takenPieces) {
                this.add(new Image(GuiUtils.GET_PIECE_TEXTURE_REGION(takenPiece))).size(40, 40);
                final Label label = new Label(Integer.toString(takenPiecesMap.get(takenPiece)), GuiUtils.UI_SKIN);
                label.setSize(10, 10);
                this.add(label);
            }
        }
    }
}