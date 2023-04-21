package task4;

public record Occurrence(String file, String word, int line) {
    @Override
    public String toString() {
        return String.format("%nFile: %s%nLine: %d%nWord: %s%n", file, line, word);
    }
}
