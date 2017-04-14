import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleGameRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleGameRunner.class);

    public static void main(String... args) {

        RockPaperScissorsGame game = new RockPaperScissorsGame();
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean shouldExit;
        do {
            System.out.println("Enter your move. rock(r), paper(p), scissors(s) or exit(e).");
            input = scanner.nextLine();
            shouldExit = "e".equals(input);
            if (!shouldExit && input != null && input.matches("^[rps]$")) {
                String result = game.play(input);
                System.out.println(result);
                PlayerHistory playerHistory = game.getPlayerHistory();
                logger.info("Total moves:" + playerHistory.getTotalMoves());
                logger.info("Games computer win ratio:" + playerHistory.getComputerWinRatio() + "\n");
                logger.info("Moves frequencies [R P S]:" + Arrays.toString(playerHistory.getFrequency()));
                logger.info("Recent moves:" + playerHistory.getRecentPlayerMoves());
                logger.info("Most frequent player's move:" + playerHistory.getMostFrequentMove());
                System.out.println("==============================================");
            }
        } while (!shouldExit);
    }
}
