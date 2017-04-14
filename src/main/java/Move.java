public enum Move {
    ROCK(1), PAPER(2), SCISSORS(3);
    private int value;

    Move(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Move valueOf(int value) {
        if (ROCK.value == value) {
            return ROCK;
        } else if (PAPER.value == value) {
            return PAPER;
        } else if (SCISSORS.value == value) {
            return SCISSORS;
        } else {
            throw new IllegalArgumentException("Invalid move value. Value:" + value);
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
