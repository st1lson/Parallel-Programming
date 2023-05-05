import mpi.MPI;
import java.util.Arrays;

public class Main {

    private static final int MATRIX_SIZE = 100;

    public static void main(String[] args) {
        var firstMatrix = createMatrix();
        var secondMatrix = createMatrix();

        var multiplier = new MatrixMultiplier(args, firstMatrix, secondMatrix);
        var result = multiplier.solve();

        if (result == null) return;

        System.out.println(Arrays.toString(result));
    }

    private static int[] createMatrix() {
        var items = new int[MATRIX_SIZE * MATRIX_SIZE];
        for (var i = 0; i < MATRIX_SIZE * MATRIX_SIZE; i++) {
            items[i] = 1;
        }

        return items;
    }

    public static class MatrixMultiplier {

        private final String[] args;
        private final int[] firstMatrix;
        private final int[] secondMatrix;
        private final int matrixSize;

        public MatrixMultiplier(String[] args, int[] firstMatrix, int[] secondMatrix) {
            this.args = args;
            this.firstMatrix = firstMatrix;
            this.secondMatrix = secondMatrix;
            this.matrixSize = firstMatrix.length;
        }

        public int[] solve() {
            try {
                MPI.Init(args);

                var rank = MPI.COMM_WORLD.Rank();
                var size = MPI.COMM_WORLD.Size();
                var root = 0;

                var sendcounts = new int[size];
                var displs = new int[size];
                var C = new int[matrixSize];

                var distribution = matrixSize / size;
                var extra = matrixSize % size;
                for (var i = 0; i < size; i++) {
                    if (i == size - 1) {
                        sendcounts[i] = distribution + extra;
                    } else {
                        sendcounts[i] = distribution;
                    }

                    displs[i] = i == 0 ? 0 : sendcounts[i - 1] + displs[i - 1];
                }

                var recvcount = sendcounts[rank];
                var recvAbuf = new int[recvcount];
                var recvBbuf = new int[recvcount];

                MPI.COMM_WORLD.Scatterv(firstMatrix, 0, sendcounts, displs, MPI.INT, recvAbuf, 0, recvcount, MPI.INT, root);
                MPI.COMM_WORLD.Scatterv(secondMatrix, 0, sendcounts, displs, MPI.INT, recvBbuf, 0, recvcount, MPI.INT, root);

                var partialC = new int[matrixSize / sendcounts[rank]];

                for (int i = 0; i < matrixSize / sendcounts[rank]; i++) {
                    var a = 0;
                    var b = 0;
                    for (int j = 0; j < matrixSize; j++) {
                        if (j + i * matrixSize == sendcounts[rank]) break;

                        a += recvAbuf[j + i * matrixSize];
                        b += recvBbuf[j + i * matrixSize];
                    }

                    partialC[i] = a * b;
                }

                var recv = new int[partialC.length];

                MPI.COMM_WORLD.Gatherv(partialC, 0, 1, MPI.INT, C, 0, recv, displs, MPI.INT, root);
                if (rank == 0) {
                    return C;
                }

                return null;
            } finally {
                MPI.Finalize();
            }
        }
    }
}