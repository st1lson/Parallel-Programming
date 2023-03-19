package lab2.Algorithms;

import java.util.concurrent.CopyOnWriteArrayList;

import lab2.Models.Matrix;
import lab2.Models.SubTask;

public class RowAlgorithmThread implements Runnable {
    private final CopyOnWriteArrayList<SubTask> tasks;
    private final Matrix resultMatrix;

    public RowAlgorithmThread(CopyOnWriteArrayList<SubTask> tasks, Matrix resultMatrix) {
        this.tasks = tasks;
        this.resultMatrix = resultMatrix;
    }

    @Override
    public void run() {
        for (SubTask task : this.tasks) {
            var result = 0;
            for (int i = 0; i < task.getRow().length; i++) {
                result += task.getRow()[i] * task.getColumn()[i];
            }
            
            this.resultMatrix.setItem(task.getI(), task.getJ(), result);
        }
    }
}
