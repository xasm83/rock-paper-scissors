import java.util.Random;

public class RockPaperScissorsGame {
    private PlayerHistory playerHistory = new PlayerHistory();
    private Random random = new Random();

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

    PlayerHistory getPlayerHistory() {
        return playerHistory;
    }

    private Move getComputerMove() {
        Move computerMove;
        Move predictedMove = playerHistory.getMostFrequentMoveForPlayerLastMove();
        computerMove = getMoveForPredictedMove(predictedMove);

        return computerMove;
    }

    private Move getRandomMove() {
        return Move.valueOf(random.nextInt(3) + 1);
    }

    private Move getMoveForPredictedMove(Move predictedMove) {
        int computerMoveIndex = (predictedMove.getValue() + 1) % 3;
        return Move.valueOf(computerMoveIndex == 0 ? 3 : computerMoveIndex);
    }

    private Move getMoveBasedOnPlayerFrequencies() {
        int randomMoveBase = random.nextInt(PlayerHistory.getHistorySize()) + 1;
        int[] frequency = playerHistory.getFrequency();
        Move computerMove;
        if (randomMoveBase < frequency[0]) {
            computerMove = Move.ROCK;
        } else if (randomMoveBase < frequency[0] + frequency[1]) {
            computerMove = Move.ROCK;
        } else {
            computerMove = Move.SCISSORS;
        }
        return computerMove;
    }

    private String calculateGameResult(Move playerMove, Move computerMove) {
        int result = computerMove.getValue() - playerMove.getValue();
        String resultString = "Computer move:" + computerMove.name() + ".\nYour move:" +
                playerMove.name();
        if (result == 0) {
            resultString += ".\n" + "It's a draw!";
        } else if (result == -1 || result == 2) {
            playerHistory.incrementGamesPlayerWin();
            resultString += ".\n" + "You have won!";
        } else {
            playerHistory.incrementGamesComputerWin();
            resultString += ".\n" + "You have lost!";
        }
        playerHistory.addMove(playerMove);
        return resultString;
    }
}
