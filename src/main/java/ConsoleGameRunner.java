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
                PlayerHistoryProcessor playerHistoryProcessor = game.getPlayerHistoryProcessor();
                logger.info("Total moves:" + playerHistoryProcessor.getTotalMoves());
                logger.info("Games computer win ratio:" + playerHistoryProcessor.getComputerWinRatio() + "\n");
                logger.info("Recent moves:" + playerHistoryProcessor.getRecentPlayerMoves());
                System.out.println("==============================================");
            }
        } while (!shouldExit);
    }
}
