package lab1.task5;

public class Program {
    public static void main(String[] args) {
        PrintThread firstThread = new PrintThread("-");
        PrintThread secondThread = new PrintThread("|");
        firstThread.start();
        secondThread.start();
    }
}
