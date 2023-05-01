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

            var countsInBytes = new int[size];
            for (var i = 0; i < size; i++) {
                if (i != size - 1) {
                    countsInBytes[i] = elementsPerProcess * Matrix.INT32_BYTE_SIZE;
                } else {
                    countsInBytes[i] = (elementsPerProcess + elementsToAddToLastProcess) * Matrix.INT32_BYTE_SIZE;
                }
            }

            var displs = new int[size];
            displs[0] = 0;
            for (var i = 1; i < displs.length; i++) {
                displs[i] = countsInBytes[i - 1] + displs[i - 1];
            }

            var byteMatrA = firstMatrix.toByteArray();
            var byteMatrB = secondMatrix.toByteArray();

            var currentCountInBytes = countsInBytes[rank];
            var partMatrAByte = new byte[currentCountInBytes];

            var resByte = new byte[firstMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE];

            MPI.COMM_WORLD.Scatterv(byteMatrA, 0, countsInBytes, displs, MPI.BYTE, partMatrAByte, 0, currentCountInBytes, MPI.BYTE, 0);

            MPI.COMM_WORLD.Bcast(byteMatrB, 0, byteMatrB.length, MPI.BYTE, 0);

            var partMatrixAInProcess = new Matrix(partMatrAByte, currentCountInBytes / (Matrix.INT32_BYTE_SIZE * secondMatrixColumns), firstMatrixRows);
            var matrixBInProcess = new Matrix(byteMatrB, secondMatrixColumns, firstMatrixRows);

            var resInProcess = partMatrixAInProcess.multiply(matrixBInProcess);

            var resInProcessByte = resInProcess.toByteArray();

            MPI.COMM_WORLD.Gatherv(resInProcessByte, 0, resInProcessByte.length, MPI.BYTE, resByte, 0, countsInBytes, displs, MPI.BYTE, 0);

            if (rank == 0) {
                return new Result(new Matrix(resByte, firstMatrixRows, secondMatrixColumns), System.currentTimeMillis() - startTime);
            }

            return null;
        } finally {
            MPI.Finalize();
        }
    }
}
