package assignment;

import java.util.*;

class Medicine {
    String code;
    String name;
    int stock;

    Medicine(String code, String name, int stock) {
        this.code = code;
        this.name = name;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return code + " - " + name + " - Stock: " + stock;
    }
}

public class PharmacyInventory {

    static final int LOOKUPS = 100000;
    static final int REPETITIONS = 5;

    static volatile long sink = 0;

    static String codeFor(int i) {
        return String.format("MED%06d", i);
    }

    static Medicine createMedicine(int i) {
        return new Medicine(
            codeFor(i),
            "Medicine_" + i,
            (i % 500) + 1
        );
    }

    static HashMap<String, Medicine> buildHashMap(
        int n, List<String> order) {

        HashMap<String, Medicine> map = new HashMap<>();

        for (int i = n - 1; i >= 0; i--) {
            Medicine m = createMedicine(i);
            map.put(m.code, m);
            order.add(m.code);
        }

        return map;
    }

    static TreeMap<String, Medicine> buildTreeMap(
        int n, List<String> order) {

        TreeMap<String, Medicine> map = new TreeMap<>();

        for (int i = n - 1; i >= 0; i--) {
            Medicine m = createMedicine(i);
            map.put(m.code, m);
            order.add(m.code);
        }

        return map;
    }

    static LinkedHashMap<String, Medicine> buildLinkedHashMap(
        int n, List<String> order) {

        LinkedHashMap<String, Medicine> map =
            new LinkedHashMap<>();

        for (int i = n - 1; i >= 0; i--) {
            Medicine m = createMedicine(i);
            map.put(m.code, m);
            order.add(m.code);
        }

        return map;
    }

    static long measureLookups(
        Map<String, Medicine> map,
        int n,
        int repetitions) {

        long total = 0;

        for (int r = 0; r < repetitions; r++) {
            long sum = 0;
            long start = System.nanoTime();

            for (int i = 0; i < LOOKUPS; i++) {
                int index = (i * 37) % n;
                Medicine m = map.get(codeFor(index));

                if (m != null) {
                    sum += m.stock;
                }
            }

            long end = System.nanoTime();
            total += end - start;
            sink = sum;
        }

        return total / repetitions;
    }

    static long measureCodeReport(
        Map<String, Medicine> map,
        boolean naturallySorted,
        int repetitions) {

        long total = 0;

        for (int r = 0; r < repetitions; r++) {
            long checksum = 0;
            long start = System.nanoTime();

            if (naturallySorted) {
                for (Map.Entry<String, Medicine> e :
                        map.entrySet()) {
                    checksum += e.getValue().stock;
                }
            } else {
                List<String> keys =
                    new ArrayList<>(map.keySet());

                Collections.sort(keys);

                for (String key : keys) {
                    checksum += map.get(key).stock;
                }
            }

            long end = System.nanoTime();
            total += end - start;
            sink = checksum;
        }

        return total / repetitions;
    }

    static long measureInsertionReport(
        Map<String, Medicine> map,
        List<String> insertionOrder,
        boolean naturallyOrdered,
        int repetitions) {

        long total = 0;

        for (int r = 0; r < repetitions; r++) {
            long checksum = 0;
            long start = System.nanoTime();

            if (naturallyOrdered) {
                for (Map.Entry<String, Medicine> e :
                        map.entrySet()) {
                    checksum += e.getValue().stock;
                }
            } else {
                for (String key : insertionOrder) {
                    Medicine m = map.get(key);
                    checksum += m.stock;
                }
            }

            long end = System.nanoTime();
            total += end - start;
            sink = checksum;
        }

        return total / repetitions;
    }

    static boolean isCodeSorted(Map<String, Medicine> map) {
        String previous = "";

        for (String key : map.keySet()) {
            if (previous.compareTo(key) > 0) {
                return false;
            }
            previous = key;
        }

        return true;
    }

    static boolean isInsertionOrdered(
        Map<String, Medicine> map,
        List<String> order,
        boolean linkedHashMap) {

        if (linkedHashMap) {
            return new ArrayList<>(map.keySet()).equals(order);
        }

        // HashMap and TreeMap do not retain insertion order.
        return false;
    }

    static double toMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    static void runExperiment(int n) {

        System.out.println("\n==================================");
        System.out.println("INVENTORY SIZE: " + n);
        System.out.println("==================================");

        List<String> hashOrder = new ArrayList<>();
        List<String> treeOrder = new ArrayList<>();
        List<String> linkedOrder = new ArrayList<>();

        long start = System.nanoTime();
        HashMap<String, Medicine> hashMap =
            buildHashMap(n, hashOrder);
        long hashPut = System.nanoTime() - start;

        start = System.nanoTime();
        TreeMap<String, Medicine> treeMap =
            buildTreeMap(n, treeOrder);
        long treePut = System.nanoTime() - start;

        start = System.nanoTime();
        LinkedHashMap<String, Medicine> linkedMap =
            buildLinkedHashMap(n, linkedOrder);
        long linkedPut = System.nanoTime() - start;

        System.out.println("\nPUT TIME");
        System.out.printf("HashMap: %.3f ms%n", toMillis(hashPut));
        System.out.printf("TreeMap: %.3f ms%n", toMillis(treePut));
        System.out.printf("LinkedHashMap: %.3f ms%n",
            toMillis(linkedPut));

        // Warm-up lookups
        measureLookups(hashMap, n, 2);
        measureLookups(treeMap, n, 2);
        measureLookups(linkedMap, n, 2);

        System.out.println("\n100,000 LOOKUPS (AVERAGE)");
        System.out.printf("HashMap: %.3f ms%n",
            toMillis(measureLookups(hashMap, n, REPETITIONS)));

        System.out.printf("TreeMap: %.3f ms%n",
            toMillis(measureLookups(treeMap, n, REPETITIONS)));

        System.out.printf("LinkedHashMap: %.3f ms%n",
            toMillis(measureLookups(linkedMap, n, REPETITIONS)));

        System.out.println("\nCODE-ORDER REPORT (AVERAGE)");

        System.out.printf("HashMap: %.3f ms%n",
            toMillis(measureCodeReport(
                hashMap, false, REPETITIONS)));

        System.out.printf("TreeMap: %.3f ms%n",
            toMillis(measureCodeReport(
                treeMap, true, REPETITIONS)));

        System.out.printf("LinkedHashMap: %.3f ms%n",
            toMillis(measureCodeReport(
                linkedMap, false, REPETITIONS)));

        System.out.println("\nINSERTION-ORDER REPORT (AVERAGE)");

        System.out.printf("HashMap: %.3f ms%n",
            toMillis(measureInsertionReport(
                hashMap, hashOrder, false, REPETITIONS)));

        System.out.printf("TreeMap: %.3f ms%n",
            toMillis(measureInsertionReport(
                treeMap, treeOrder, false, REPETITIONS)));

        System.out.printf("LinkedHashMap: %.3f ms%n",
            toMillis(measureInsertionReport(
                linkedMap, linkedOrder, true, REPETITIONS)));

        System.out.println("\nORDER VERIFICATION");

        System.out.println("HashMap code sorted: "
            + isCodeSorted(hashMap));

        System.out.println("TreeMap code sorted: "
            + isCodeSorted(treeMap));

        System.out.println("LinkedHashMap code sorted: "
            + isCodeSorted(linkedMap));

        System.out.println("HashMap insertion order preserved: "
            + isInsertionOrdered(hashMap, hashOrder, false));

        System.out.println("TreeMap insertion order preserved: "
            + isInsertionOrdered(treeMap, treeOrder, false));

        System.out.println("LinkedHashMap insertion order preserved: "
            + isInsertionOrdered(linkedMap, linkedOrder, true));

        System.out.println("\nFirst five code-sorted medicines:");

        for (int i = 0; i < Math.min(5, treeMap.size()); i++) {
            String key = codeFor(i);
            System.out.println(treeMap.get(key));
        }

        System.out.println("\nFirst five insertion-order medicines:");

        for (int i = 0; i < Math.min(5, linkedOrder.size()); i++) {
            System.out.println(linkedMap.get(linkedOrder.get(i)));
        }
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 100000};

        for (int size : sizes) {
            runExperiment(size);
        }
    }
}
