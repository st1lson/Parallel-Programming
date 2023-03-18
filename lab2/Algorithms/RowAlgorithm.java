package lab2.Algorithms;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;

public final class RowAlgorithm implements IAlgorithm {   
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    public RowAlgorithm(Matrix firstMatrix, Matrix secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    @Override
    public Result solve() {
        var rows = this.firstMatrix.clone().getMatrix();
        var columns = Matrix.transpose(this.secondMatrix.clone()).getMatrix();
        var rowsCount = this.firstMatrix.getRows();
        var columnsCount = this.secondMatrix.getColumns();

        var resultMatrix = new int[rowsCount][columnsCount];

        // algorithm
        var executor = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < rowsCount; i++) {
                var tasks = new ArrayList<Future<Integer>>();
                for (int j = 0; j < columnsCount; j++) {
                    var col = (i + j) % columnsCount;
                    tasks.add(executor.submit(new RowAlgorithmThread(rows[i], columns[col])));   
                }
    
                for (int j = 0; j < columnsCount; j++) {
                    var col = (i + j) % columnsCount;
                    resultMatrix[i][col] = tasks.get(j).get().intValue();
                }
            }   
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        executor.shutdown();

        return new Result(new Matrix(resultMatrix));
    }
}
