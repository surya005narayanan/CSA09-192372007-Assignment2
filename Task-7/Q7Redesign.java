package assignment;

import java.util.*;

class CircularPlaylist {

    private Object[] elements;
    private int head;
    private int size;

    CircularPlaylist(int capacity) {
        elements = new Object[Math.max(2, capacity)];
        head = 0;
        size = 0;
    }

    private int index(int logicalIndex) {
        return (head + logicalIndex) % elements.length;
    }

    private void grow() {
        Object[] newArray =
            new Object[elements.length * 2];

        for (int i = 0; i < size; i++) {
            newArray[i] = elements[index(i)];
        }

        elements = newArray;
        head = 0;
    }

    void addFirst(Object value) {
        if (size == elements.length) {
            grow();
        }

        head = (head - 1 + elements.length)
                % elements.length;

        elements[head] = value;
        size++;
    }

    void addLast(Object value) {
        if (size == elements.length) {
            grow();
        }

        elements[index(size)] = value;
        size++;
    }

    Object removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Object value = elements[head];
        elements[head] = null;

        head = (head + 1) % elements.length;
        size--;

        if (size == 0) {
            head = 0;
        }

        return value;
    }

    Object removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        int lastIndex = index(size - 1);
        Object value = elements[lastIndex];

        elements[lastIndex] = null;
        size--;

        if (size == 0) {
            head = 0;
        }

        return value;
    }

    Object get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException();
        }

        return elements[index(position)];
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }
}

public class Q7Redesign {

    static volatile long sink;

    static long testArrayList(int n) {
        ArrayList<Integer> list = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        long sum = 0;
        long start = System.nanoTime();

        while (!list.isEmpty()) {
            sum += list.remove(0);
        }

        long end = System.nanoTime();
        sink = sum;

        return end - start;
    }

    static long testCircular(int n) {
        CircularPlaylist playlist =
            new CircularPlaylist(n);

        for (int i = 0; i < n; i++) {
            playlist.addLast(i);
        }

        long sum = 0;
        long start = System.nanoTime();

        while (!playlist.isEmpty()) {
            sum += (Integer) playlist.removeFirst();
        }

        long end = System.nanoTime();
        sink = sum;

        return end - start;
    }

    static void verifyCorrectness() {

        System.out.println("TEST 1: Circular Playlist Verification");

        CircularPlaylist playlist =
            new CircularPlaylist(3);

        playlist.addLast("B");
        playlist.addLast("C");
        playlist.addFirst("A");

        System.out.println("Initial sequence: "
            + playlist.get(0) + " "
            + playlist.get(1) + " "
            + playlist.get(2));

        System.out.println("Removed first: "
            + playlist.removeFirst());

        System.out.println("Removed last: "
            + playlist.removeLast());

        playlist.addFirst("X");
        playlist.addLast("Y");

        System.out.println("Final sequence: "
            + playlist.get(0) + " "
            + playlist.get(1));

        System.out.println("Final size: " + playlist.size());
    }

    static void benchmark(int n) {

        int repetitions = 5;

        // Warm-up
        for (int i = 0; i < 2; i++) {
            testArrayList(n);
            testCircular(n);
        }

        long arrayTotal = 0;
        long circularTotal = 0;

        for (int i = 0; i < repetitions; i++) {
            arrayTotal += testArrayList(n);
            circularTotal += testCircular(n);
        }

        double arrayMs =
            arrayTotal / (double) repetitions / 1_000_000.0;

        double circularMs =
            circularTotal / (double) repetitions / 1_000_000.0;

        System.out.println("\nWORKLOAD SIZE: " + n);

        System.out.printf(
            "ArrayList removal: %.3f ms%n", arrayMs);

        System.out.printf(
            "Circular removal: %.3f ms%n", circularMs);

        System.out.printf(
            "Improvement: %.2fx%n", arrayMs / circularMs);
    }

    public static void main(String[] args) {

        verifyCorrectness();

        System.out.println("\n================================");
        System.out.println("REDESIGN PERFORMANCE COMPARISON");
        System.out.println("================================");

        int[] sizes = {1000, 10000, 100000};

        for (int n : sizes) {
            benchmark(n);
        }
    }
}
