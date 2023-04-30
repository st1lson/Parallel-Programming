import Interfaces.IMultiplier;
import Models.Matrix;
import Models.Result;
import mpi.MPI;
import mpi.MPIException;

public final class BlockingMultiplier implements IMultiplier {

    private static final int FROM_MASTER = 1;
    private static final int FROM_WORKER = 1;

    private static String[] args;

    public static void setArgs(String[] args) {
        BlockingMultiplier.args = args;
    }

    public Result multiply(Matrix firstMatrix, Matrix secondMatrix) throws MPIException {
        var firstMatrixRows = firstMatrix.getRows();
        var firstMatrixColumns = firstMatrix.getColumns();
        var secondMatrixRows = secondMatrix.getRows();
        var secondMatrixColumns = secondMatrix.getColumns();

        var result = new Matrix(firstMatrixRows, firstMatrixColumns);

        MPI.Init(args);
        var size = MPI.COMM_WORLD.Size();
        var rank = MPI.COMM_WORLD.Rank();
        var workersCount = size - 1;

        if (rank == 0) {
            var startTime = 0d;
            var endTime = 0d;

            startTime = MPI.Wtime();
            var rowsPerWorker = firstMatrixRows / workersCount;
            var extraRows = firstMatrixRows % workersCount;

            var secondMatrixBuffer = secondMatrix.flattenIntArray();
            for (var workerIndex = 1; workerIndex <= workersCount; workerIndex++) {
                var rowStartIndex = (workerIndex - 1) * rowsPerWorker;
                var rowFinishIndex = rowStartIndex + rowsPerWorker;
                if (workerIndex == workersCount) {
                    rowFinishIndex += extraRows;
                }

                var firstSubMatrix = firstMatrix.getSubMatrix(rowStartIndex, rowFinishIndex, secondMatrixRows);
                var firstSubMatrixBuffer = firstSubMatrix.flattenIntArray();
                var subMatrixSize = (rowFinishIndex - rowStartIndex + 1) * firstMatrixRows;

                MPI.COMM_WORLD.Send(new int[]{ rowStartIndex }, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(new int[]{ rowFinishIndex }, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(firstSubMatrixBuffer, 0, subMatrixSize * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(secondMatrixBuffer, 0, secondMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
            }

            for (var workerIndex = 1; workerIndex <= workersCount; workerIndex++) {
                var rowStartIndex = new int[1];
                var rowFinishIndex = new int[1];

                MPI.COMM_WORLD.Recv(rowStartIndex, 0, 1, MPI.INT, workerIndex, FROM_WORKER);
                MPI.COMM_WORLD.Recv(rowFinishIndex, 0, 1, MPI.INT, workerIndex, FROM_WORKER);

                var resultElementsCount = (rowFinishIndex[0] - rowStartIndex[0] + 1) * firstMatrixRows;
                var resultMatrixBuffer = new byte[resultElementsCount * Matrix.INT32_BYTE_SIZE];

                MPI.COMM_WORLD.Recv(resultMatrixBuffer, 0, resultElementsCount * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_WORKER);

                var resultMatrix = new Matrix(resultMatrixBuffer, rowFinishIndex[0] - rowStartIndex[0], firstMatrixColumns);
                result.partialUpdate(resultMatrix, rowStartIndex[0], rowFinishIndex[0]);
            }

            endTime = MPI.Wtime();

            return new Result(result, (long) (endTime - startTime));
        } else {
            var rowStartIndex = new int[1];
            var rowFinishIndex = new int[1];

            MPI.COMM_WORLD.Recv(rowStartIndex, 0, 1, MPI.INT, 0, FROM_MASTER);
            MPI.COMM_WORLD.Recv(rowFinishIndex, 0, 1, MPI.INT, 0, FROM_MASTER);

            var subMatrixSize = (rowFinishIndex[0] - rowStartIndex[0] + 1) * firstMatrixRows;
            var subMatrixBuffer = new byte[subMatrixSize * Matrix.INT32_BYTE_SIZE];
            var matrixBuffer = new byte[secondMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE];

            MPI.COMM_WORLD.Recv(subMatrixBuffer, 0, subMatrixSize * Matrix.INT32_BYTE_SIZE, MPI.BYTE, 0, FROM_MASTER);
            MPI.COMM_WORLD.Recv(matrixBuffer, 0, secondMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE, MPI.BYTE, 0, FROM_MASTER);

            var matrix = new Matrix(matrixBuffer, secondMatrixRows, secondMatrixColumns);
            var subMatrix = new Matrix(subMatrixBuffer, rowFinishIndex[0] - rowStartIndex[0], firstMatrixColumns);
            var matrixResult = subMatrix.multiply(matrix);
            var matrixResultBuffer = matrixResult.flattenIntArray();

            MPI.COMM_WORLD.Send(rowStartIndex, 0, 1, MPI.INT, 0, FROM_WORKER);
            MPI.COMM_WORLD.Send(rowFinishIndex, 0, 1, MPI.INT, 0, FROM_WORKER);
            MPI.COMM_WORLD.Send(matrixResultBuffer, 0, matrixResultBuffer.length, MPI.BYTE, 0, FROM_WORKER);
        }

        MPI.Finalize();

        return null;
    }
}
