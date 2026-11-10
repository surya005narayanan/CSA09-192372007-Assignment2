
import java.util.*;

class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {

    private long counter = 0;

    // Lower severity number first.
    // For equal severity, earlier arrival first.
    private final Comparator<Patient> patientOrder =
        Comparator.comparingInt((Patient p) -> p.severity)
            .thenComparingLong(p -> p.arrival);

    private final PriorityQueue<Patient> queue =
        new PriorityQueue<>(patientOrder);

    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                "Severity must be between 1 and 5"
            );
        }

        queue.add(new Patient(name, severity, counter++));
    }

    Patient next() {
        return queue.poll();
    }

    void displayWaitingList() {
        // Copy and poll to display actual treatment order.
        PriorityQueue<Patient> copy =
            new PriorityQueue<>(queue);

        System.out.println("Waiting list in treatment order:");

        while (!copy.isEmpty()) {
            System.out.println(copy.poll());
        }
    }

    static void runHandTest() {

        System.out.println("TEST 1: Hospital Emergency Triage");

        Triage er = new Triage();

        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Priya", 4);
        er.admit("Arun", 2);

        System.out.println("\nPriorityQueue direct display:");
        System.out.println(er.queue);

        System.out.println();

        er.displayWaitingList();

        System.out.println("\nActual treatment sequence:");

        Patient p;
        while ((p = er.next()) != null) {
            System.out.print(p + " ");
        }

        System.out.println();
    }

    static volatile long sink = 0;

    static void runPerformanceTest(int n) {

        int repetitions = 5;
        long totalAdd = 0;
        long totalPoll = 0;

        // Warm-up runs
        for (int w = 0; w < 2; w++) {
            PriorityQueue<Patient> warmup =
                new PriorityQueue<>(Comparator
                    .comparingInt((Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival));

            for (int i = 0; i < n; i++) {
                warmup.add(new Patient(
                    "Patient" + i,
                    (i % 5) + 1,
                    i
                ));
            }

            while (!warmup.isEmpty()) {
                sink += warmup.poll().arrival;
            }
        }

        for (int r = 0; r < repetitions; r++) {

            PriorityQueue<Patient> pq =
                new PriorityQueue<>(Comparator
                    .comparingInt((Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival));

            // Measure insertion
            long startAdd = System.nanoTime();

            for (int i = 0; i < n; i++) {
                pq.add(new Patient(
                    "Patient" + i,
                    (i % 5) + 1,
                    i
                ));
            }

            long endAdd = System.nanoTime();

            // Measure removal separately
            long sum = 0;
            long startPoll = System.nanoTime();

            while (!pq.isEmpty()) {
                sum += pq.poll().arrival;
            }

            long endPoll = System.nanoTime();

            totalAdd += endAdd - startAdd;
            totalPoll += endPoll - startPoll;

            sink = sum;
        }

        double addMs =
            totalAdd / (double) repetitions / 1_000_000.0;

        double pollMs =
            totalPoll / (double) repetitions / 1_000_000.0;

        System.out.println("\nQueue size: " + n);
        System.out.printf("Average admission time: %.3f ms%n", addMs);
        System.out.printf("Average removal time: %.3f ms%n", pollMs);
    }

    public static void main(String[] args) {

        runHandTest();

        System.out.println("\n================================");
        System.out.println("PERFORMANCE INVESTIGATION");
        System.out.println("================================");

        int[] sizes = {1000, 10000, 100000};

        for (int size : sizes) {
            runPerformanceTest(size);
        }
    }
}
