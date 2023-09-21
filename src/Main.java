import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int[] vectorSizes = {100, 1000, 10000, 100000};
        int[] vectorCounts = {2, 3, 4, 5, 10};

        System.out.println("Mul\tN\tm\tTime (ms)");

        for (int N : vectorSizes) {
            for (int M : vectorCounts) {
                int[] vector = generateRandomVector(N);

                long startTime, endTime;
                boolean Mul;

                // Параллельная обработка
                Mul = true;
                startTime = System.currentTimeMillis();
                processVectorInParallel(vector, M);
                endTime = System.currentTimeMillis();
                System.out.println(Mul + "\t" + N + "\t" + M + "\t" + (endTime - startTime) + " ms");

                // Не параллельная обработка
                Mul = false;
                startTime = System.currentTimeMillis();
                processVectorSequentially(vector);
                endTime = System.currentTimeMillis();
                System.out.println(Mul + "\t" + N + "\t" + M + "\t" + (endTime - startTime) + " ms");
            }
        }
    }

    private static int[] generateRandomVector(int N) {
        int[] vector = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            vector[i] = random.nextInt(100);
        }
        return vector;
    }

    private static void processVectorInParallel(int[] vector, int threadCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        int segmentSize = vector.length / threadCount;

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int start = i * segmentSize;
            int end = (i == threadCount - 1) ? vector.length : (i + 1) * segmentSize;

            final int[] segment = Arrays.copyOfRange(vector, start, end);

            Callable<Void> task = () -> {
                for (int j = 0; j < segment.length; j++) {
                    segment[j] = (int) Math.sqrt(factorial(segment[j])) * 123423243;
                }
                return null;
            };

            tasks.add(task);
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void processVectorSequentially(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = (int) Math.sqrt(factorial(vector[i]));
        }
    }

    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1) * 123423243;
        }
    }
}