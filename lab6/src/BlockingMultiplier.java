import Interfaces.IMultiplier;
import Models.Chunk;
import Models.Matrix;
import Models.Result;
import mpi.MPI;

public final class BlockingMultiplier implements IMultiplier {

    private static final int FROM_MASTER = 1;
    private static final int FROM_WORKER = 10;

    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    private final int firstMatrixRows;
    private final int firstMatrixColumns;
    private final int secondMatrixRows;
    private final int secondMatrixColumns;

    private final String[] args;

    BlockingMultiplier(Matrix firstMatrix, Matrix secondMatrix, String[] args) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;

        this.firstMatrixRows = firstMatrix.getRows();
        this.firstMatrixColumns = firstMatrix.getColumns();
        this.secondMatrixRows = secondMatrix.getRows();
        this.secondMatrixColumns = secondMatrix.getColumns();

        this.args = args;
    }

    public Result multiply() {
        try {
            MPI.Init(args);
            var size = MPI.COMM_WORLD.Size();
            var rank = MPI.COMM_WORLD.Rank();
            if (size < 2) return null;

            if (rank == 0) {
                return processMaster(size);
            } else {
                processWorker();
            }

            return null;
        } finally {
            MPI.Finalize();
        }
    }

    private Result processMaster(int size) {
        var startTime = System.currentTimeMillis();

        var workersCount = size - 1;
        var rowsPerWorker = firstMatrixRows / workersCount;
        var extraRows = firstMatrixRows % workersCount;

        var result = new Matrix(firstMatrixRows, firstMatrixColumns);
        var secondMatrixBuffer = secondMatrix.toByteArray();

        var chunks = new Chunk[workersCount];
        for (var workerIndex = 1; workerIndex <= workersCount; workerIndex++) {
            var rowStartIndex = (workerIndex - 1) * rowsPerWorker;
            var rowFinishIndex = rowStartIndex + rowsPerWorker;
            if (workerIndex == workersCount) {
                rowFinishIndex += extraRows;
            }

            chunks[workerIndex - 1] = new Chunk(rowStartIndex, rowFinishIndex);

            var firstSubMatrix = firstMatrix.getSubMatrix(rowStartIndex, rowFinishIndex, secondMatrixRows);
            var firstSubMatrixBuffer = firstSubMatrix.toByteArray();
            var subMatrixSize = (rowFinishIndex - rowStartIndex + 1) * firstMatrixRows;

            MPI.COMM_WORLD.Send(new int[]{rowStartIndex}, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
            MPI.COMM_WORLD.Send(new int[]{rowFinishIndex}, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
            MPI.COMM_WORLD.Send(firstSubMatrixBuffer, 0, subMatrixSize * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
            MPI.COMM_WORLD.Send(secondMatrixBuffer, 0, secondMatrixRows * secondMatrixColumns * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
        }

        for (var workerIndex = 1; workerIndex <= workersCount; workerIndex++) {
            var chunk = chunks[workerIndex - 1];
            var startIndex = chunk.startIndex();
            var finishIndex = chunk.finishIndex();

            var resultElementsCount = (finishIndex - startIndex + 1) * firstMatrixRows;
            var resultMatrixBuffer = new byte[resultElementsCount * Matrix.INT32_BYTE_SIZE];

            MPI.COMM_WORLD.Recv(resultMatrixBuffer, 0, resultElementsCount * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_WORKER);

            var resultMatrix = new Matrix(resultMatrixBuffer, finishIndex - startIndex, firstMatrixColumns);
            result.partialUpdate(resultMatrix, startIndex, finishIndex);
        }

        return new Result(result, (System.currentTimeMillis() - startTime));
    }

    private void processWorker() {
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
        var matrixResultBuffer = matrixResult.toByteArray();

        MPI.COMM_WORLD.Send(matrixResultBuffer, 0, matrixResultBuffer.length, MPI.BYTE, 0, FROM_WORKER);
    }
}
