package com.mygdx.game.chess.engine.FEN;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveLog;
import com.mygdx.game.chess.engine.pieces.Bishop;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Knight;
import com.mygdx.game.chess.engine.pieces.Pawn;
import com.mygdx.game.chess.engine.pieces.Queen;
import com.mygdx.game.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.chess.engine.board.Board.*;

public final class FenUtilities {

    private FenUtilities() {
        throw new RuntimeException("Non instantiable");
    }

    private static List<Integer> saveMoveLogCoordinates(final MoveLog moveLog) {
        final List<Integer> coordinates = new ArrayList<>();
        for (final Move move: moveLog.getMoves()) {
            coordinates.add(move.getCurrentCoordinate());
            coordinates.add(move.getDestinationCoordinate());
        }
        return Collections.unmodifiableList(coordinates);
    }

    public static String getGameData(final MoveLog moveLog, final Board board) {
        final List<Integer> coordinates = saveMoveLogCoordinates(moveLog);
        final StringBuilder stringBuilder = new StringBuilder(coordinates.size());
        int j = 0;
        for (final int i: coordinates) {
            j++;
            if (j == coordinates.size()) {
                stringBuilder.append(i);
            } else {
                stringBuilder.append(i).append(" ");
            }
        }
        return stringBuilder.toString() + getPlayerTimer(board);
    }

    public static Board createGameFromSavedData(final String data, final MoveLog moveLog) {
        final String[] dataPartitioned = data.split("\n");
        final String[] coordinates = dataPartitioned[0].split(" ");
        moveLog.clear();
        Board board = createStandardBoardForMoveHistory(dataPartitioned[1].split(":"), dataPartitioned[2].split(":"));
        for (int i = 0; i < coordinates.length; i++) {
            final Move move = Move.MoveFactory.createMoveFromMoveHistory(board, Integer.parseInt(coordinates[i]), Integer.parseInt(coordinates[++i]));
            moveLog.addMove(move);
            board = board.currentPlayer().makeMove(move).getLatestBoard();
        }
        return board;
    }

    private static String getPlayerTimer(final Board board) {
        final String whitePlayerTimer = board.whitePlayer().getMinute() + ":" + board.whitePlayer().getSecond() + ":" + board.whitePlayer().getMillisecond();
        final String blackPlayerTimer = board.blackPlayer().getMinute() + ":" + board.blackPlayer().getSecond() + ":" + board.blackPlayer().getMillisecond();
        return "\n" + whitePlayerTimer + "\n" + blackPlayerTimer;
    }

    public static String createFENFromGame(final Board board) {
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) + " " +
                calculateEnPassantSquare(board) + " " +
                "0 " + board.getMoveCount();
    }

    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            builder.append(board.getTile(i).toString());
        }
        builder.insert(8, "/");
        builder.insert(17, "/");
        builder.insert(26, "/");
        builder.insert(35, "/");
        builder.insert(44, "/");
        builder.insert(53, "/");
        builder.insert(62, "/");

        return builder.toString()
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");

    }

    private static boolean kingSideCastle(final String fenCastleString, final boolean isWhite) { return isWhite ? fenCastleString.contains("K") : fenCastleString.contains("k"); }

    private static boolean queenSideCastle(final String fenCastleString, final boolean isWhite) { return isWhite ? fenCastleString.contains("Q") : fenCastleString.contains("q"); }

    private static Pawn getEnPassantPawn(final League league, final String fenEnPassantCoordinate) {
        if (!"-".equals(fenEnPassantCoordinate)) {
            final int enPassantPawnPosition = BoardUtils.getCoordinateAtPosition(fenEnPassantCoordinate) - (8) * league.getDirection();
            return new Pawn(league.isBlack() ? League.WHITE : League.BLACK, enPassantPawnPosition);
        }
        return null;
    }

    public static Board createGameFromFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");

        final League playerLeague = getLeague(fenPartitions[1]);

        final Builder builder = new Builder(Integer.parseInt(fenPartitions[fenPartitions.length - 1]), playerLeague, getEnPassantPawn(playerLeague, fenPartitions[3]));

        final boolean whiteKingSideCastle = kingSideCastle(fenPartitions[2], true);
        final boolean whiteQueenSideCastle = queenSideCastle(fenPartitions[2], true);
        final boolean blackKingSideCastle = kingSideCastle(fenPartitions[2], false);
        final boolean blackQueenSideCastle = queenSideCastle(fenPartitions[2], false);

        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r':
                    builder.setPiece(new Rook(League.BLACK, i));
                    i++;
                    break;
                case 'n':
                    builder.setPiece(new Knight(League.BLACK, i));
                    i++;
                    break;
                case 'b':
                    builder.setPiece(new Bishop(League.BLACK, i));
                    i++;
                    break;
                case 'q':
                    builder.setPiece(new Queen(League.BLACK, i));
                    i++;
                    break;
                case 'k':
                    builder.setPiece(new King(League.BLACK, i, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                    break;
                case 'p':
                    builder.setPiece(new Pawn(League.BLACK, i));
                    i++;
                    break;
                case 'R':
                    builder.setPiece(new Rook(League.WHITE, i));
                    i++;
                    break;
                case 'N':
                    builder.setPiece(new Knight(League.WHITE, i));
                    i++;
                    break;
                case 'B':
                    builder.setPiece(new Bishop(League.WHITE, i));
                    i++;
                    break;
                case 'Q':
                    builder.setPiece(new Queen(League.WHITE, i));
                    i++;
                    break;
                case 'K':
                    builder.setPiece(new King(League.WHITE, i, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                    break;
                case 'P':
                    builder.setPiece(new Pawn(League.WHITE, i));
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " + gameConfiguration);
            }
        }
        return builder.build();
    }
    private static League getLeague(final String moveMakerString) {
        if("w".equals(moveMakerString)) {
            return League.WHITE;
        } else if("b".equals(moveMakerString)) {
            return League.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " + moveMakerString);
    }

    private static String calculateEnPassantSquare(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null) {
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() - (8) * enPassantPawn.getLeague().getDirection());
        }
        return "-";
    }

    private static String calculateCurrentPlayerText(final Board board) { return board.currentPlayer().toString().substring(0, 1).toLowerCase(); }

    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();

        if (board.whitePlayer().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if (board.whitePlayer().isQueenSideCastleCapable()) {
            builder.append("Q");
        }

        if (board.blackPlayer().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if (board.blackPlayer().isQueenSideCastleCapable()) {
            builder.append("q");
        }

        final String result = builder.toString();

        return result.isEmpty() ? "-" : result;
    }
}