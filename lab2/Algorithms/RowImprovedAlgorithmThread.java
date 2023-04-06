package lab2.Algorithms;

import java.util.concurrent.CopyOnWriteArrayList;

import lab2.Models.Matrix;
import lab2.Models.SubTask;

public final class RowImprovedAlgorithmThread implements Runnable {
    private final CopyOnWriteArrayList<SubTask> tasks;
    private final Matrix resultMatrix;

    public RowImprovedAlgorithmThread(CopyOnWriteArrayList<SubTask> tasks, Matrix resultMatrix) {
        this.tasks = tasks;
        this.resultMatrix = resultMatrix;
    }

    @Override
    public void run() {
        for (SubTask task : this.tasks) {
            var result = 0;
            for (var i = 0; i < task.getRow().length; i++) {
                result += task.getRow()[i] * task.getColumn()[i];
            }
            
            this.resultMatrix.setItem(task.getI(), task.getJ(), result);
        }
    }
}
