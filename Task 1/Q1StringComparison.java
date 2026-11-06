package assignment;
import java.util.*;

public class task1 {

    static volatile long sink = 0;

    static class Workload {
        int tickets;
        int fields;

        Workload(int tickets, int fields) {
            this.tickets = tickets;
            this.fields = fields;
        }
    }

    // Generate identical input fields for all approaches
    static String[] generateFields(int count) {
        String[] fields = new String[count];

        for (int i = 0; i < count; i++) {
            fields[i] = "Field" + i + "|";
        }

        return fields;
    }

    // Approach 1: String
    static long usingString(Workload w, String[] fields) {
        long checksum = 0;

        for (int i = 0; i < w.tickets; i++) {
            String ticket = "";

            for (int j = 0; j < w.fields; j++) {
                ticket = ticket + fields[j];
            }

            checksum += ticket.length();
        }

        return checksum;
    }

    // Approach 2: StringBuilder
    static long usingStringBuilder(Workload w, String[] fields) {
        long checksum = 0;

        for (int i = 0; i < w.tickets; i++) {
            StringBuilder ticket = new StringBuilder();

            for (int j = 0; j < w.fields; j++) {
                ticket.append(fields[j]);
            }

            checksum += ticket.length();
        }

        return checksum;
    }

    // Approach 3: StringBuffer
    static long usingStringBuffer(Workload w, String[] fields) {
        long checksum = 0;

        for (int i = 0; i < w.tickets; i++) {
            StringBuffer ticket = new StringBuffer();

            for (int j = 0; j < w.fields; j++) {
                ticket.append(fields[j]);
            }

            checksum += ticket.length();
        }

        return checksum;
    }

    // Measure average execution time
    interface Test {
        long execute();
    }

    static void measure(String name, Test test) {

        // Warm-up
        for (int i = 0; i < 2; i++) {
            sink = test.execute();
        }

        int repetitions = 5;
        long totalTime = 0;

        for (int i = 0; i < repetitions; i++) {
            long start = System.nanoTime();

            sink = test.execute();

            long end = System.nanoTime();

            totalTime += (end - start);
        }

        double averageMs =
                (totalTime / (double) repetitions) / 1_000_000.0;

        System.out.printf("%-18s : %.3f ms%n",
                          name, averageMs);
    }
    static void measureMemory(String name, Test test) {

        Runtime runtime = Runtime.getRuntime();

        System.gc();

        long before = runtime.totalMemory()
                      - runtime.freeMemory();

        sink = test.execute();

        long after = runtime.totalMemory()
                     - runtime.freeMemory();

        System.out.println(name + " memory change: "
            + ((after - before) / 1024.0) + " KB");
    }

    public static void main(String[] args) {

        Workload[] workloads = {
            new Workload(1, 6),
            new Workload(10000, 50),
            new Workload(1000, 200)
        };

        for (int i = 0; i < workloads.length; i++) {

            Workload w = workloads[i];
            String[] fields = generateFields(w.fields);

            System.out.println("\n================================");
            System.out.println("WORKLOAD " + (i + 1));
            System.out.println("Tickets: " + w.tickets);
            System.out.println("Fields per ticket: " + w.fields);
            System.out.println("================================");

            measure("String",
                () -> usingString(w, fields));

            measure("StringBuilder",
                () -> usingStringBuilder(w, fields));

            measure("StringBuffer",
                () -> usingStringBuffer(w, fields));
        }
    }
}
