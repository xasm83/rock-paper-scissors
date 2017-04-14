import java.security.SecureRandom;
import java.util.*;

/**
 * predicts move using frequencies and pattern detection(top n-gram calculation)
 * for frequencies it checks what is the most probable move after the current one
 * based on history frequencies
 */
public class PlayerHistoryProcessor {
    private static final int HISTORY_SIZE = 20;

    private int totalMoves;
    private int gamesComputerWin;
    private int gamesPlayerWin;

    private Deque<Move> recentPlayerMoves = new ArrayDeque<>();
    private int[][] conditionalMoveFrequency = new int[3][3];
    private Random random = new SecureRandom();

    private static final int MAX_NGRAM_LENGTH = 3;
    private static final int MIN_NGRAM_LENGTH = 3;
    private static final double FREQUENT_NGRAM_THRESHOLD = 0.24;
    private Map<String, Integer> nGrams = new HashMap<>();


    public int getTotalMoves() {
        return totalMoves;
    }

    public Deque<Move> getRecentPlayerMoves() {
        return recentPlayerMoves;
    }

    public double getComputerWinRatio() {
        return gamesComputerWin / (double) (gamesComputerWin + gamesPlayerWin);
    }

    public void addMove(Move move) {
        if (recentPlayerMoves.size() > HISTORY_SIZE) {
            recentPlayerMoves.remove();
        }
        recentPlayerMoves.add(move);
        totalMoves++;
        calculateConditionalFrequencies();
        calculateNgrams();
    }

    private void calculateNgrams() {
        nGrams = new HashMap<>();
        String movesString = getMovesString();
        for (int moveIndex = 0; moveIndex < movesString.length(); moveIndex++) {
            for (int length = MIN_NGRAM_LENGTH; length <= MAX_NGRAM_LENGTH && moveIndex + length < movesString.length(); length++) {
                String nGram = movesString.substring(moveIndex, moveIndex + length);
                nGrams.compute(nGram, (key, oldValue) -> oldValue == null ? 1 : ++oldValue);
            }
        }
    }

    private String getMovesString() {
        return recentPlayerMoves.stream().
                map(move -> Integer.toString(move.getValue())).
                reduce("", String::concat);
    }

    public Optional<Move> getPredictedMoveUsingNgrams() {
        String movesString = getMovesString();
        return nGrams.entrySet().
                stream().
                filter(entry -> (entry.getValue() / (double) nGrams.size()) > FREQUENT_NGRAM_THRESHOLD).
                sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue))).
                map(Map.Entry::getKey).
                filter(nGram -> movesString.endsWith(nGram.substring(0, nGram.length() - 1))).
                map(nGramString -> Character.getNumericValue(nGramString.charAt(nGramString.length() - 1))).
                map(Move::valueOf).
                findFirst();
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

    public Move getMostFrequentMoveForPlayerLastMove() {
        Move move = recentPlayerMoves.peekLast();
        if (move == null) {
            //naive human moves have different distributon, we could exploit it here
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
    }

    public void incrementGamesPlayerWin() {
        gamesPlayerWin++;
    }
}
