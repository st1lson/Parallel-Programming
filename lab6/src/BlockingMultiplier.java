import Interfaces.IMultiplier;
import Models.Matrix;
import Models.Result;
import mpi.MPI;
import mpi.MPIException;

import static java.lang.System.exit;

public class BlockingMultiplier implements IMultiplier {

    private static final int FROM_MASTER = 1;
    private static final int FROM_WORKER = 1;

    private static String[] args;

    public static void setArgs(String[] args) {
        BlockingMultiplier.args = args;
    }

    public void multiply(Matrix firstMatrix, Matrix secondMatrix) throws MPIException {
        var rowsMatrixA = firstMatrix.getRows();
        var columnsMatrixA = firstMatrix.getColumns();
        var rowsMatrixB = secondMatrix.getRows();
        var columnsMatrixB = secondMatrix.getColumns();

        var result = new Matrix(rowsMatrixA, columnsMatrixA);
        var startTime = 0d;
        var endTime = 0d;

        MPI.Init(args);
        var tasksNum = MPI.COMM_WORLD.Size();
        var taskID = MPI.COMM_WORLD.Rank();

        var workersNum = tasksNum - 1;

        if (tasksNum < 2 || rowsMatrixA < workersNum) {
            System.out.println("Need at least two MPI tasks or more rows than workers");
            MPI.COMM_WORLD.Abort(1);
            exit(1);
        }

        if (taskID == 0) {
            System.out.println("Started with " + tasksNum + " tasks");
            startTime = MPI.Wtime();
            var rowsPerWorker = rowsMatrixA / workersNum;
            var extraRows = rowsMatrixA % workersNum;

            var secondMatrixBuffer = secondMatrix.flattenIntArray();
            for (var workerIndex = 1; workerIndex <= workersNum; workerIndex++) {

                var rowStartIndex = (workerIndex - 1) * rowsPerWorker;
                var rowFinishIndex = rowStartIndex + rowsPerWorker;
                if (workerIndex == workersNum) rowFinishIndex += extraRows;

                var firstSubMatrix = firstMatrix.getSubMatrix(rowStartIndex, rowFinishIndex, rowsMatrixB);
                var firstSubMatrixBuffer = firstSubMatrix.flattenIntArray();
                var subMatrixSize = (rowFinishIndex - rowStartIndex + 1) * rowsMatrixA;
                MPI.COMM_WORLD.Send(new int[]{rowStartIndex}, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(new int[]{rowFinishIndex}, 0, 1, MPI.INT, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(firstSubMatrixBuffer, 0, subMatrixSize * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
                MPI.COMM_WORLD.Send(secondMatrixBuffer, 0, rowsMatrixB * columnsMatrixB * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_MASTER);
            }

            for (var workerIndex = 1; workerIndex <= workersNum; workerIndex++) {
                var rowStartIndex = new int[1];
                var rowFinishIndex = new int[1];

                MPI.COMM_WORLD.Recv(rowStartIndex, 0, 1, MPI.INT, workerIndex, FROM_WORKER);
                MPI.COMM_WORLD.Recv(rowFinishIndex, 0, 1, MPI.INT, workerIndex, FROM_WORKER);

                var resultElementsCount = (rowFinishIndex[0] - rowStartIndex[0] + 1) * rowsMatrixA;
                var resultMatrixBuffer = new byte[resultElementsCount * Matrix.INT32_BYTE_SIZE];

                MPI.COMM_WORLD.Recv(resultMatrixBuffer, 0, resultElementsCount * Matrix.INT32_BYTE_SIZE, MPI.BYTE, workerIndex, FROM_WORKER);

                var resultMatrix = new Matrix(resultMatrixBuffer, rowFinishIndex[0] - rowStartIndex[0], columnsMatrixA);
                Matrix.partialUpdate(result, resultMatrix, rowStartIndex[0], rowFinishIndex[0]);
            }

            endTime = MPI.Wtime();

            System.out.println(result);
        } else {
            var rowStartIndex = new int[1];
            var rowFinishIndex = new int[1];
            MPI.COMM_WORLD.Recv(rowStartIndex, 0, 1, MPI.INT, 0, FROM_MASTER);
            MPI.COMM_WORLD.Recv(rowFinishIndex, 0, 1, MPI.INT, 0, FROM_MASTER);
            var submatrAElements = (rowFinishIndex[0] - rowStartIndex[0] + 1) * rowsMatrixA;
            var submatrABuffer = new byte[submatrAElements * Matrix.INT32_BYTE_SIZE];
            var matrBBuffer = new byte[rowsMatrixB * columnsMatrixB * Matrix.INT32_BYTE_SIZE];
            MPI.COMM_WORLD.Recv(submatrABuffer, 0, submatrAElements * Matrix.INT32_BYTE_SIZE, MPI.BYTE, 0, FROM_MASTER);
            MPI.COMM_WORLD.Recv(matrBBuffer, 0, rowsMatrixB * columnsMatrixB * Matrix.INT32_BYTE_SIZE, MPI.BYTE, 0, FROM_MASTER);
            System.out.println("Row start: " + rowStartIndex[0] + " Row finish " + rowFinishIndex[0] + " From task " + taskID);


            var matrB = new Matrix(matrBBuffer, rowsMatrixB, columnsMatrixB);
            var subMatrA = new Matrix(submatrABuffer, rowFinishIndex[0] - rowStartIndex[0], columnsMatrixA);
            var matrRes = subMatrA.multiply(matrB);


            var matrResBuffer = matrRes.flattenIntArray();

            MPI.COMM_WORLD.Send(rowStartIndex, 0, 1, MPI.INT, 0, FROM_WORKER);
            MPI.COMM_WORLD.Send(rowFinishIndex, 0, 1, MPI.INT, 0, FROM_WORKER);
            MPI.COMM_WORLD.Send(matrResBuffer, 0, matrResBuffer.length, MPI.BYTE, 0, FROM_WORKER);
        }

        MPI.Finalize();
    }
}
