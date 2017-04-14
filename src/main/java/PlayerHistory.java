import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Random;

public class PlayerHistory {
    private static final int HISTORY_SIZE = 30;

    private int totalMoves;
    private int gamesComputerWin;
    private int gamesPlayerWin;

    private boolean playerWinPreviousGame;
    private boolean computerLostTwoTimesInARow;

    private Deque<Move> recentPlayerMoves = new ArrayDeque<>();
    private int[] frequency = new int[3];
    private int[][] conditionalMoveFrequency = new int[3][3];
    private Random random = new Random();

    public static int getHistorySize() {
        return HISTORY_SIZE;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public boolean isComputerLostTwoTimesInARow() {
        return computerLostTwoTimesInARow;
    }

    public int[] getFrequency() {
        return frequency;
    }

    public Deque<Move> getRecentPlayerMoves() {
        return recentPlayerMoves;
    }

    public double getComputerWinRatio() {
        return gamesComputerWin / (double) (gamesComputerWin + gamesPlayerWin);
    }

    public void addMove(Move move) {
        if (recentPlayerMoves.size() > HISTORY_SIZE) {
            frequency[recentPlayerMoves.remove().getValue() - 1]--;
        }
        recentPlayerMoves.add(move);
        frequency[move.getValue() - 1]++;
        totalMoves++;
        calculateConditionalFrequencies();
    }

    private void calculateConditionalFrequencies() {
        Move previousMove = null;
        for (Move move : recentPlayerMoves) {
            if (previousMove != null) {
                conditionalMoveFrequency[previousMove.getValue() - 1][move.getValue() - 1]++;
            }
            previousMove = move;
        }
    }

    public Move getMostFrequentMove() {
        if (frequency[0] == frequency[1] && frequency[1] == frequency[2]) {
            return Move.valueOf(random.nextInt(3) + 1);
        }
        int maxIndex;
        if (frequency[0] > frequency[1]) {
            maxIndex = frequency[0] > frequency[2] ? 0 : 1;
        } else {
            maxIndex = frequency[1] > frequency[2] ? 1 : 2;
        }
        return Move.valueOf(maxIndex + 1);
    }

    public Move getMostFrequentMoveForPlayerLastMove() {
        Move move = recentPlayerMoves.peekLast();
        if (move == null) {
            return Move.valueOf(random.nextInt(3) + 1);
        }
        int[] frequenciesForSpecificMove = conditionalMoveFrequency[move.getValue() - 1];
        if (frequenciesForSpecificMove[0] == frequenciesForSpecificMove[1] && frequenciesForSpecificMove[1] == frequenciesForSpecificMove[2]) {
            return Move.valueOf(random.nextInt(3) + 1);
        }
        int maxIndex;
        if (frequenciesForSpecificMove[0] > frequenciesForSpecificMove[1]) {
            maxIndex = frequenciesForSpecificMove[0] > frequenciesForSpecificMove[2] ? 0 : 1;
        } else {
            maxIndex = frequenciesForSpecificMove[1] > frequenciesForSpecificMove[2] ? 1 : 2;
        }
        return Move.valueOf(maxIndex + 1);
    }

    public void incrementGamesComputerWin() {
        gamesComputerWin++;
        playerWinPreviousGame = false;
        computerLostTwoTimesInARow = false;
    }

    public void incrementGamesPlayerWin() {
        gamesPlayerWin++;
        if (playerWinPreviousGame) {
            computerLostTwoTimesInARow = true;
        } else {
            playerWinPreviousGame = true;
        }
    }

    public boolean areRecentPlayerMovesAreTheSame() {
        Iterator<Move> descendingIterator = recentPlayerMoves.descendingIterator();
        return recentPlayerMoves.size() > 1 &&
                descendingIterator.next().equals(descendingIterator.next());
    }

    public Move getLastPlayerMove() {
        return recentPlayerMoves.peekLast();
    }
}
