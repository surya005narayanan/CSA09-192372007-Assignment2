
import java.util.*;

public class Q2CollectionComparison {

    static volatile long sink = 0;

    static final int[] SIZES = {1000, 10000, 100000};
    static final int REPETITIONS = 5;

    // Create pre-filled collections
    static ArrayList<Integer> createArrayList(int n) {
        ArrayList<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }

    static LinkedList<Integer> createLinkedList(int n) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) list.add(i);
        return list;
    }

    static ArrayDeque<Integer> createArrayDeque(int n) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) deque.addLast(i);
        return deque;
    }

    interface Test {
        long run();
    }

    // Measure average execution time
    static double measure(Test test) {

        for (int i = 0; i < 2; i++) {
            sink = test.run();
        }

        long total = 0;

        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();

            sink = test.run();

            long end = System.nanoTime();
            total += end - start;
        }

        return total / (double) REPETITIONS / 1_000_000.0;
    }

    // Add at end
    static long addEnd(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? new ArrayList<>()
                : new LinkedList<>();

        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        return list.size();
    }

    // Insert at front
    static long insertFront(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? new ArrayList<>()
                : new LinkedList<>();

        for (int i = 0; i < n; i++) {
            list.add(0, i);
        }

        return list.size();
    }

    // Insert in middle
    static long insertMiddle(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? createArrayList(n)
                : createLinkedList(n);

        for (int i = 0; i < 100; i++) {
            list.add(list.size() / 2, -1);
        }

        return list.size();
    }

    // Remove from front
    static long removeFront(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? createArrayList(n)
                : createLinkedList(n);

        for (int i = 0; i < n; i++) {
            list.remove(0);
        }

        return list.size();
    }

    // Remove from middle
    static long removeMiddle(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? createArrayList(n)
                : createLinkedList(n);

        for (int i = 0; i < 100; i++) {
            list.remove(list.size() / 2);
        }

        return list.size();
    }

    // Access K-th element
    static long accessK(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? createArrayList(n)
                : createLinkedList(n);

        long sum = 0;

        for (int i = 0; i < 1000; i++) {
            sum += list.get(n / 2);
        }

        return sum;
    }

    // Full iteration
    static long iterate(String type, int n) {
        List<Integer> list = type.equals("ArrayList")
                ? createArrayList(n)
                : createLinkedList(n);

        long sum = 0;

        for (int value : list) {
            sum += value;
        }

        return sum;
    }

    // ArrayDeque operations
    static long dequeEnds(int n) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            deque.addLast(i);
        }

        for (int i = 0; i < 100; i++) {
            deque.addFirst(-1);
            deque.removeFirst();
            deque.addLast(-1);
            deque.removeLast();
        }

        return deque.size();
    }

    static long dequeIteration(int n) {
        ArrayDeque<Integer> deque = createArrayDeque(n);

        long sum = 0;

        for (int value : deque) {
            sum += value;
        }

        return sum;
    }

    static void report(String operation, int n,
                       Test array, Test linked) {

        System.out.println("\n" + operation);

        System.out.printf("ArrayList  : %.3f ms%n",
                measure(array));

        System.out.printf("LinkedList : %.3f ms%n",
                measure(linked));
    }

    public static void main(String[] args) {

        for (int n : SIZES) {

            System.out.println("\n================================");
            System.out.println("PLAYLIST SIZE: " + n);
            System.out.println("================================");

            report("Add at end", n,
                    () -> addEnd("ArrayList", n),
                    () -> addEnd("LinkedList", n));

            report("Insert at front", n,
                    () -> insertFront("ArrayList", n),
                    () -> insertFront("LinkedList", n));

            report("Insert in middle (100 operations)", n,
                    () -> insertMiddle("ArrayList", n),
                    () -> insertMiddle("LinkedList", n));

            report("Remove from front", n,
                    () -> removeFront("ArrayList", n),
                    () -> removeFront("LinkedList", n));

            report("Remove from middle (100 operations)", n,
                    () -> removeMiddle("ArrayList", n),
                    () -> removeMiddle("LinkedList", n));

            report("Access K-th element (1000 accesses)", n,
                    () -> accessK("ArrayList", n),
                    () -> accessK("LinkedList", n));

            report("Full iteration", n,
                    () -> iterate("ArrayList", n),
                    () -> iterate("LinkedList", n));

            System.out.println("\nArrayDeque:");

            System.out.printf("End operations: %.3f ms%n",
                    measure(() -> dequeEnds(n)));

            System.out.printf("Full iteration: %.3f ms%n",
                    measure(() -> dequeIteration(n)));

            System.out.println(
                    "Middle insertion/removal: Not directly supported");

            System.out.println(
                    "K-th indexed access: Not supported");
        }
    }
}
