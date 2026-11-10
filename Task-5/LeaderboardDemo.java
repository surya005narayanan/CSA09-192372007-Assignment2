
import java.util.*;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {

    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    // Accepts a list containing T or any subtype of T
    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {
        List<T> sortedItems = new ArrayList<>(items);
        sortedItems.sort(order);
        return sortedItems;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        // Ranking: points descending, goal difference descending,
        // team name ascending
        Comparator<Team> byRank =
            Comparator.comparingInt((Team t) -> t.points)
                .reversed()
                .thenComparing(
                    Comparator.comparingInt(
                        (Team t) -> t.goalDiff
                    ).reversed()
                )
                .thenComparing(t -> t.name);

        // TEST 1: Multiple ties at different ranking levels
        System.out.println("TEST 1: Sports Leaderboard");

        Leaderboard<Team> board =
            new Leaderboard<>(byRank);

        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        board.add(new Team("Tigers", 6, 5));
        board.add(new Team("Lions", 6, 5));
        board.add(new Team("Wolves", 3, 2));

        System.out.println("Final Ranking:");
        for (Team t : board.ranking()) {
            System.out.println(t);
        }

        // TEST 2: A second independently designed dataset
        System.out.println("\nTEST 2: Additional Ranking Dataset");

        Leaderboard<Team> board2 =
            new Leaderboard<>(byRank);

        List<Team> additionalTeams = Arrays.asList(
            new Team("Falcons", 12, 8),
            new Team("Hawks", 12, 8),
            new Team("Sharks", 12, 3),
            new Team("Dolphins", 10, 7),
            new Team("Bears", 10, 7),
            new Team("Foxes", 5, 2)
        );

        board2.addAll(additionalTeams);

        for (Team t : board2.ranking()) {
            System.out.println(t);
        }

        // TEST 3: Verify that ranking does not modify original order
        System.out.println("\nTEST 3: Original List Preservation");

        System.out.println("Original first team: " + 
            board.ranking().get(0));

        // Faulty comparator using subtraction
        Comparator<Team> faultyComparator =
            (a, b) -> b.points - a.points;

        Team a = new Team("A", Integer.MAX_VALUE, 0);
        Team b = new Team("B", 0, 0);
        Team c = new Team("C", Integer.MIN_VALUE, 0);

        System.out.println("\nTEST 4: Faulty Comparator");

        System.out.println("A compared with B: " +
            faultyComparator.compare(a, b));

        System.out.println("B compared with C: " +
            faultyComparator.compare(b, c));

        System.out.println("A compared with C: " +
            faultyComparator.compare(a, c));

        boolean ab = faultyComparator.compare(a, b) < 0;
        boolean bc = faultyComparator.compare(b, c) < 0;
        boolean ac = faultyComparator.compare(a, c) < 0;

        System.out.println("A before B: " + ab);
        System.out.println("B before C: " + bc);
        System.out.println("A before C: " + ac);

        System.out.println("\nCorrect comparator check:");

        System.out.println("A compared with B: " +
            byRank.compare(a, b));

        System.out.println("B compared with C: " +
            byRank.compare(b, c));

        System.out.println("A compared with C: " +
            byRank.compare(a, c));
    }
}
