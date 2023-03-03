package lab1.task5;

public class Program {
    public static void main(String[] args) {
        Boolean isSynchronized = Boolean.TRUE;
        PrintThread firstThread = new PrintThread('-', isSynchronized);
        PrintThread secondThread = new PrintThread('|', isSynchronized);
        firstThread.start();
        secondThread.start();
    }
}
