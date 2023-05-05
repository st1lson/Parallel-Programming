import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final int ARRAY_SIZE = 10000;
    private static final int MASTER = 1;
    private static final int WORKER = 10;


    public static void main(String[] args) {
        var array = new String[ARRAY_SIZE];
        for (var i = 0; i < ARRAY_SIZE; i++) {
            array[i] = Integer.toString(i);
        }

        var sorter = new Sorter(array, args);
        var result = sorter.sort();

        if (result == null) return;

        System.out.println(result);
    }

    public static class Sorter {

        private final String[] array;
        private final String[] args;

        public Sorter(String[] array, String[] args) {
            this.array = array;
            this.args = args;
        }

        public List<String> sort() {
            try {
                MPI.Init(args);
                var size = MPI.COMM_WORLD.Size();
                var rank = MPI.COMM_WORLD.Rank();

                if (rank == 0) {
                    var workers = size - 1;
                    var itemsPerWorker = ARRAY_SIZE / workers;
                    var extra = ARRAY_SIZE % workers;
                    for (var i = 1; i <= workers; i++) {
                        int indexStart = (i - 1) * itemsPerWorker;
                        int indexEnd = i != workers ? (indexStart + itemsPerWorker - 1) : (indexStart + itemsPerWorker - 1) + extra;

                        var subArray = new String[indexEnd - indexStart + 1];
                        if (indexEnd + 1 - indexStart >= 0)
                            System.arraycopy(array, indexStart, subArray, 0, indexEnd + 1 - indexStart);

                        MPI.COMM_WORLD.Send(new int[]{subArray.length}, 0, 1, MPI.INT, i, MASTER);
                        MPI.COMM_WORLD.Send(subArray, 0, subArray.length, MPI.OBJECT, i, MASTER);
                    }

                    var result = new ArrayList<String>();
                    for (var i = 1; i <= workers; i++) {
                        var resultBuffer = new Object[1];
                        MPI.COMM_WORLD.Recv(resultBuffer, 0, 1, MPI.OBJECT, i, WORKER);

                        result.add(resultBuffer[0].toString());
                    }

                    return result;
                } else {
                    var subArraySize = new int[1];
                    MPI.COMM_WORLD.Recv(subArraySize, 0, 1, MPI.INT, 0, MASTER);

                    var subMatrix = new Object[subArraySize[0]];
                    MPI.COMM_WORLD.Recv(subMatrix, 0, subMatrix.length, MPI.OBJECT, 0, MASTER);

                    var arrayStr = new String[subArraySize[0]];
                    for (var i = 0; i < arrayStr.length; i++) {
                        arrayStr[i] = (String) subMatrix[i];
                    }

                    Arrays.sort(arrayStr);

                    var buffer = new Object[]{arrayStr[0]};
                    MPI.COMM_WORLD.Send(buffer, 0, 1, MPI.OBJECT, 0, WORKER);
                }

                return null;
            } finally {
                MPI.Finalize();
            }
        }
    }
}