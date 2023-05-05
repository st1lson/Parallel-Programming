import Interfaces.IMultiplier;
import Models.Matrix;
import Models.Result;
import mpi.MPI;

public final class Multiplier implements IMultiplier {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    private final int firstMatrixRows;
    private final int secondMatrixColumns;

    private final String[] args;

    Multiplier(Matrix firstMatrix, Matrix secondMatrix, String[] args) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;

        this.firstMatrixRows = firstMatrix.getRows();
        this.secondMatrixColumns = secondMatrix.getColumns();

        this.args = args;
    }

    public Result multiply() {
        try {
            MPI.Init(args);

            var startTime = System.currentTimeMillis();
            var rank = MPI.COMM_WORLD.Rank();
            var size = MPI.COMM_WORLD.Size();

            var elementsPerProcess = firstMatrixRows / size * secondMatrixColumns;
            var elementsToAddToLastProcess = (firstMatrixRows % size) * secondMatrixColumns;

            var bytes = new int[size];
            for (var i = 0; i < size; i++) {
                if (i != size - 1) {
                    bytes[i] = elementsPerProcess * Matrix.INT32_BYTE_SIZE;
                } else {
                    bytes[i] = (elementsPerProcess + elementsToAddToLastProcess) * Matrix.INT32_BYTE_SIZE;
                }
            }

            var offsets = new int[size];
            for (var i = 0; i < offsets.length; i++) {
                if (i == 0) continue;

                offsets[i] = bytes[i - 1] + offsets[i - 1];
            }

            var firstMatrixBuffer = firstMatrix.toByteArray();
            var secondMatrixBuffer = secondMatrix.toByteArray();

            var taskBytes = bytes[rank];
            var subMatrixBytes = new byte[taskBytes];

            var resByte = new byte[firstMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE];

            MPI.COMM_WORLD.Scatterv(firstMatrixBuffer, 0, bytes, offsets, MPI.BYTE, subMatrixBytes, 0, taskBytes, MPI.BYTE, 0);

            MPI.COMM_WORLD.Bcast(secondMatrixBuffer, 0, secondMatrixBuffer.length, MPI.BYTE, 0);

            var subMatrix = new Matrix(subMatrixBytes, taskBytes / (Matrix.INT32_BYTE_SIZE * secondMatrixColumns), firstMatrixRows);
            var secondMatrix = new Matrix(secondMatrixBuffer, secondMatrixColumns, firstMatrixRows);

            var result = subMatrix.multiply(secondMatrix);
            var resultBuffer = result.toByteArray();

            MPI.COMM_WORLD.Gatherv(resultBuffer, 0, resultBuffer.length, MPI.BYTE, resByte, 0, bytes, offsets, MPI.BYTE, 0);
            //MPI.COMM_WORLD.Allgatherv(resultBuffer, 0, resultBuffer.length, MPI.BYTE, resByte, 0, bytes, offsets, MPI.BYTE);
            
            if (rank == 0) {
                return new Result(new Matrix(resByte, firstMatrixRows, secondMatrixColumns), System.currentTimeMillis() - startTime);
            }

            return null;
        } finally {
            MPI.Finalize();
        }
    }
}
