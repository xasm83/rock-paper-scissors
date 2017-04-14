import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class RockPaperScissorsGameTest {
    private Random random = new Random();
    private static final int AMOUNT_OF_RUNS = 20000;
    private static final double TARGET_WIN_RATE = 0.30;
    private static final double TARGET_WIN_RATE_FOR_SEMI_RANDOM = 0.30;
    private static final int MAX_REPEAT_LENGTH = 10000;


    @Test
    public void halfGamesShouldBeWonWhenRandomPlayer() {
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        random.ints(1, 4).
                limit(AMOUNT_OF_RUNS).
                mapToObj(Move::valueOf).
                forEach(game::play);
        PlayerHistory hist = game.getPlayerHistory();
        System.out.println(hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE < hist.getComputerWinRatio());
    }

    @Test
    public void halfGamesShouldBeWonWhenMoveIsRepeated() {
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        int runsLeft = AMOUNT_OF_RUNS;
        while (runsLeft-- > 0) {
            Collections.nCopies(random.nextInt(MAX_REPEAT_LENGTH) + 2,
                    Move.valueOf(random.nextInt(3) + 1)).
                    forEach(game::play);
        }

        PlayerHistory hist = game.getPlayerHistory();
        System.out.println(hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE < hist.getComputerWinRatio());
    }


    @Test
    public void halfGamesShouldBeWonWhenSemiRandom() {

        //semi random strings, one with more prevaling 2 3, one with 1 2, one with 1 3
        //http://www.dcode.fr/frequency-analysis  used for freq analysis
        String[] semiRandomStrings = {"2321231122321231123212321231122321231122321232121232112231122321221123222322123",
                "223233331113132312113332133312213223312213232312213332212323223233331113132312113332133312213223312213232312213332212323",
                "31221213223111211111233221122231332121312231232213133212121332123332131122321233332211123132221113123113231311332331112"};
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        int runsLeft = AMOUNT_OF_RUNS;
        while (runsLeft-- > 0) {
            Arrays.stream(semiRandomStrings).forEach(string -> {
                string.chars().
                        mapToObj(Character::getNumericValue).
                        map(Integer::valueOf).
                        map(Move::valueOf).
                        forEach(game::play);
            });
        }
        PlayerHistory hist = game.getPlayerHistory();
        System.out.println(hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE_FOR_SEMI_RANDOM < hist.getComputerWinRatio());
    }

    @Test
    public void halfGamesShouldBeWonWhenRandomPatterns() {
        String[] semiRandomStrings = {"123123123123123123123123123",
                "112233112233112233112233"};
        RockPaperScissorsGame game = new RockPaperScissorsGame();
        int runsLeft = AMOUNT_OF_RUNS;
        while (runsLeft-- > 0) {
            Arrays.stream(semiRandomStrings).forEach(string -> {
                string.chars().
                        mapToObj(Character::getNumericValue).
                        map(Integer::valueOf).
                        map(Move::valueOf).
                        forEach(game::play);
            });
        }
        PlayerHistory hist = game.getPlayerHistory();
        System.out.println(hist.getComputerWinRatio());
        assertTrue(TARGET_WIN_RATE < hist.getComputerWinRatio());
    }
}
