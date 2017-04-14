import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

public class RockPaperScissorsGame {
    private PlayerHistoryProcessor playerHistoryProcessor = new PlayerHistoryProcessor();
    private Random random = new SecureRandom();


    PlayerHistoryProcessor getPlayerHistoryProcessor() {
        return playerHistoryProcessor;
    }

    public String play(String moveString) {
        Move playerMove;
        switch (moveString) {
            case "r":
                playerMove = Move.ROCK;
                break;
            case "p":
                playerMove = Move.PAPER;
                break;
            case "s":
                playerMove = Move.SCISSORS;
                break;
            default:
                throw new IllegalArgumentException("Invalid move." + moveString);
        }
        return play(playerMove);
    }

    String play(Move playerMove) {
        Move computerMove = getComputerMove();
        return calculateGameResult(playerMove, computerMove);
    }
    
    private Move getComputerMove() {
        Move computerMove;
        Optional<Move> ngramMove = playerHistoryProcessor.getPredictedMoveUsingNgrams();
        Move predictedMove = ngramMove.orElseGet(playerHistoryProcessor::getMostFrequentMoveForPlayerLastMove);
        computerMove = getMoveForPredictedMove(predictedMove);
        return computerMove;
    }

    private Move getMoveForPredictedMove(Move predictedMove) {
        int computerMoveIndex = (predictedMove.getValue() + 1) % 3;
        return Move.valueOf(computerMoveIndex == 0 ? 3 : computerMoveIndex);
    }

    private String calculateGameResult(Move playerMove, Move computerMove) {
        int result = computerMove.getValue() - playerMove.getValue();
        String resultString = "Computer move:" + computerMove.name() + ".\nYour move:" +
                playerMove.name();
        if (result == 0) {
            resultString += ".\n" + "It's a draw!";
        } else if (result == -1 || result == 2) {
            playerHistoryProcessor.incrementGamesPlayerWin();
            resultString += ".\n" + "You have won!";
        } else {
            playerHistoryProcessor.incrementGamesComputerWin();
            resultString += ".\n" + "You have lost!";
        }
        playerHistoryProcessor.addMove(playerMove);
        return resultString;
    }
}
