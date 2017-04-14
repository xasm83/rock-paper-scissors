import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class RockPaperScissorsGameTest {
    private static final Logger logger = LoggerFactory.getLogger(RockPaperScissorsGameTest.class);

    private Random random = new SecureRandom();
    private static final int AMOUNT_OF_RUNS = 5000;
    private static final double TARGET_WIN_RATE_FOR_RANDOM = 0.49;
    private static final double TARGET_WIN_RATE_FOR_SEMI_RANDOM = 0.57;
    private static final int MAX_REPEAT_LENGTH = 20;


    @Test
    public void halfGamesShouldBeWonWhenRandomPlayer() {
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        random.ints(1, 4).
                limit(AMOUNT_OF_RUNS).
                mapToObj(Move::valueOf).
                forEach(game::play);

        PlayerHistoryProcessor hist = game.getPlayerHistoryProcessor();
        logger.info("Computer win rate " + hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE_FOR_RANDOM < hist.getComputerWinRatio());
    }

    @Test
    public void targetAmountOfGamesShouldBeWonWhenMoveIsRepeated() {
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        int runsLeft = AMOUNT_OF_RUNS;
        while (runsLeft-- > 0) {
            Collections.nCopies(random.nextInt(MAX_REPEAT_LENGTH) + 2,
                    Move.valueOf(random.nextInt(3) + 1)).
                    forEach(game::play);
        }

        PlayerHistoryProcessor hist = game.getPlayerHistoryProcessor();
        logger.info("Computer win rate " + hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE_FOR_RANDOM < hist.getComputerWinRatio());
    }


    @Test
    public void targetAmountOfGamesShouldBeWonWhenSemiRandom() {
        //semi random strings,  with more frequent 2 3,  1 2, 1 3, 1 , 2
        //probability distribution is shifted ~10-15% for mentioned moves
        //http://www.dcode.fr/frequency-analysis  used for freq analysis
        String[] semiRandomMoves = {
                "2321231122321231123212321231122321231122321232121232112231122321221123222322123",
                "2232333311131323121133321333122132233122132323122133322123232232333311131323121",
                "3122121322311121111123322112223133212131223123221313321212133212333213112232123",
                "3112121322311121111123312112223133212131223123221313321212113212313213112132123",
                "2232322211131323121232321333122132233122132323122133322122232232322232123132312"};
        Arrays.stream(semiRandomMoves).forEach(this::runMoves);
    }

    @Test
    public void targetAmountOfGamesShouldBeWonWhenRepeatingPattern() {
        String[] repeatingPatternMoves = {
                "1231231231231231231231",
                "1121233112123311212331",
                "1233212311233212311233",
                "2133121322133121322133"
        };
        Arrays.stream(repeatingPatternMoves).forEach(this::runMoves);

    }

    private void runMoves(String moves) {
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        int runsLeft = AMOUNT_OF_RUNS;
        while (runsLeft-- > 0) {
            moves.chars().
                    mapToObj(Character::getNumericValue).
                    map(Integer::valueOf).
                    map(Move::valueOf).
                    forEach(game::play);
        }
        PlayerHistoryProcessor playerHistoryProcessor = game.getPlayerHistoryProcessor();
//        logger.info("Moves frequencies for ROCK:" +
//                Arrays.toString(playerHistoryProcessor.getConditionalFrequenciesPerMove()[Move.ROCK.getValue() - 1]));
//        logger.info("Moves frequencies for PAPER:" +
//                Arrays.toString(playerHistoryProcessor.getConditionalFrequenciesPerMove()[Move.PAPER.getValue() - 1]));
//        logger.info("Moves frequencies for SCISSORS:" +
//                Arrays.toString(playerHistoryProcessor.getConditionalFrequenciesPerMove()[Move.SCISSORS.getValue() - 1]));
//        logger.info("Recent moves:" + playerHistory.getPlayerMoves());
//        logger.info("Ngrams:" + playerHistoryProcessor.getnGrams().toString());
        logger.info("Computer win rate " + playerHistoryProcessor.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE_FOR_SEMI_RANDOM < playerHistoryProcessor.getComputerWinRatio());
    }
}
