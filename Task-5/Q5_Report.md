# Q5 – Developing a Generic Ranking Component

## 1. Objective

To develop a reusable generic leaderboard component using Java Generics and Comparator. The system ranks sports teams according to multiple criteria and demonstrates the importance of maintaining comparator consistency.

## 2. Ranking Requirements

The sports leaderboard uses the following ranking rules:

1. Higher points receive a higher rank.
2. If points are equal, higher goal difference receives a higher rank.
3. If both values are equal, team names are sorted alphabetically.

The same generic leaderboard component can be reused for different object types by supplying an appropriate Comparator.

## 3. Implementation Concepts

### Generics

The Leaderboard<T> class allows the same implementation to work with different object types without writing separate leaderboard classes.

### Comparable and Comparator

Comparable defines the natural ordering within a class, whereas Comparator provides an external ordering strategy.

Comparator was selected because different ranking rules can be applied without modifying the original object class.

### Comparator Chaining

The implementation uses comparingInt(), reversed() and thenComparing() to implement multiple ranking criteria.

### Wildcards

The addAll(List<? extends T>) method allows collections containing objects of type T or its subclasses to be added to the leaderboard.

## 4. Experimental Results

### Test 1 – Sports Leaderboard

The first dataset contained seven teams.

Observed ranking:

| Rank | Team | Points | Goal Difference |
|---:|---|---:|---:|
| 1 | Eagles | 9 | 6 |
| 2 | Bisons | 9 | 4 |
| 3 | Panthers | 9 | 4 |
| 4 | Lions | 6 | 5 |
| 5 | Tigers | 6 | 5 |
| 6 | Cobras | 6 | 1 |
| 7 | Wolves | 3 | 2 |

The results followed all three ranking criteria correctly.

### Test 2 – Additional Ranking Dataset

A second dataset containing six teams was evaluated.

Observed ranking:

| Rank | Team | Points | Goal Difference |
|---:|---|---:|---:|
| 1 | Falcons | 12 | 8 |
| 2 | Hawks | 12 | 8 |
| 3 | Sharks | 12 | 3 |
| 4 | Bears | 10 | 7 |
| 5 | Dolphins | 10 | 7 |
| 6 | Foxes | 5 | 2 |

The output confirmed that the same generic leaderboard could process another dataset without modifying its implementation.

### Test 3 – Ranking Without Modifying the Original Collection

The ranking() method creates a new ArrayList containing the original elements and sorts that copy.

Therefore, the original insertion order is preserved internally while the returned ranking is sorted.

The displayed first-ranked team was Eagles with 9 points and a goal difference of 6.

### Test 4 – Faulty Comparator

A faulty comparator was implemented using integer subtraction:

```java
(a, b) -> b.points - a.points;
```

Large integer values caused overflow and produced inconsistent comparisons.

Observed results:

| Comparison | Result |
|---|---|
| A compared with B | -2147483647 |
| B compared with C | -2147483648 |
| A compared with C | 1 |

The resulting relationships were:

- A before B: true
- B before C: true
- A before C: false

This demonstrates a violation of comparator transitivity.

### Corrected Comparator

The corrected comparator uses Comparator.comparingInt() and reversed() rather than subtraction.

Observed results:

| Comparison | Result |
|---|---:|
| A compared with B | -1 |
| B compared with C | -1 |
| A compared with C | -1 |

The corrected implementation produced consistent comparison results without integer subtraction overflow.

## 5. Complexity Analysis

Let n represent the number of leaderboard entries.

| Operation | Time Complexity |
|---|---|
| Add one entry | O(1) amortized |
| Add a collection of k entries | O(k) amortized |
| Generate sorted ranking | O(n log n) |
| Store leaderboard entries | O(n) |
| Additional space for ranking copy | O(n) |

The ranking operation requires O(n log n) time because it sorts the copied list.

## 6. Design Decisions

**Why use Comparator instead of Comparable?**

Comparator allows different ranking rules to be supplied externally without modifying the Team class. This makes the component more reusable.

**Why use Generics?**

Generics allow the same Leaderboard<T> implementation to rank teams, players, colleges and quiz participants.

**Why use List<? extends T>?**

This wildcard allows the component to accept a list containing objects of T or its subclasses.

**Why create a new list during ranking?**

Creating a copy prevents sorting from changing the original insertion order of the leaderboard.

**Why avoid subtraction in comparators?**

Integer subtraction can overflow when comparing extreme integer values. Comparator.comparingInt() provides a safer comparison mechanism.

## 7. Conclusion

The experiment successfully demonstrated the development of a reusable generic leaderboard using Java Generics and Comparator. The implementation correctly ranked teams based on points, goal difference and alphabetical order.

The second dataset demonstrated that the same component could be reused without changing its implementation. The faulty comparator experiment demonstrated how integer overflow can violate transitivity and produce inconsistent ordering.

The corrected comparator produced consistent results. Therefore, the generic leaderboard provides a reusable and reliable approach for ranking different types of objects using multiple comparison criteria.
