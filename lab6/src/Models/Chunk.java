package Models;

public class Chunk {
    private byte[] buffer = null;
    private final int startIndex;
    private final int finishIndex;

    public Chunk(int startIndex, int finishIndex) {
        this.startIndex = startIndex;
        this.finishIndex = finishIndex;
    }

    public int startIndex() {
        return startIndex;
    }

    public int finishIndex() {
        return finishIndex;
    }

    public byte[] buffer() {
        return this.buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}
