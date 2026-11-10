# PROGRAMMING IN JAVA – ASSIGNMENT 2
## Collection Framework, Generics, Performance Analysis and Design Challenge

**Course Code:** CSA09  
**Course:** Programming in Java  
**Assignment:** Assignment 2 – CO1–CO5

---

# STUDENT DETAILS

| Particular | Details |
|---|---|
| Student Name | Surya Narayanan Muthu Kumar |
| Register Number | 192372007 |
| Programming Language | Java |
| Java Version | 21.0.11 |
| Assignment | Assignment 2 |
| Total Marks | 50 |

---

# ABSTRACT

This assignment investigates the behaviour, performance, efficiency and practical applications of Java collection classes and object-oriented programming concepts. Seven experiments were conducted using Java to understand the differences between immutable and mutable strings, list implementations, sets, maps, generic programming, priority-based queues and custom data structures.

The experiments compare String, StringBuilder and StringBuffer; ArrayList, LinkedList and ArrayDeque; HashSet and its equality and hashing contract; HashMap, TreeMap and LinkedHashMap; generic ranking components using Comparator; and PriorityQueue-based emergency scheduling.

The final design challenge identifies a performance limitation in ArrayList during repeated front-removal operations and introduces a custom circular-array playlist to address the problem.

Experimental measurements were collected using Java's System.nanoTime() method. Multiple workload sizes were evaluated wherever applicable. The results were analysed using execution time, time complexity, ordering behaviour, correctness and practical application requirements.

The assignment demonstrates that selecting an appropriate data structure requires understanding its internal behaviour and evaluating it against the actual operations performed by an application.

---

# TABLE OF CONTENTS

1. Introduction
2. Objectives
3. Experimental Environment and Methodology
4. Q1 – String, StringBuilder and StringBuffer
5. Q2 – ArrayList, LinkedList and ArrayDeque
6. Q3 – HashSet and the hashCode–equals Contract
7. Q4 – HashMap, TreeMap and LinkedHashMap
8. Q5 – Generics, Comparable and Comparator
9. Q6 – Queue and PriorityQueue
10. Q7 – Diagnose, Redesign and Validate
11. Overall Findings
12. Conclusion
13. References

---

# 1. INTRODUCTION

The Java Collections Framework provides reusable data structures and algorithms for storing, retrieving, organising and processing data.

Different collection classes are designed for different operational requirements. A collection that performs efficiently for one operation may become inefficient when the workload or access pattern changes.

For example, ArrayList provides constant-time indexed access but requires shifting elements during front insertion and removal. LinkedList supports efficient modifications at its ends but requires traversal for indexed access.

Similarly, HashMap provides efficient average lookup but does not guarantee iteration order, whereas TreeMap maintains sorted keys and LinkedHashMap preserves insertion order.

This assignment explores these differences through practical implementations and experiments. Instead of relying only on theoretical complexity, the experiments use actual Java execution measurements to understand how data structures behave under different workloads.

The final question extends this process by identifying a limitation in an existing implementation and developing a redesigned solution to improve performance.

# 2. OBJECTIVES

The primary objectives of this assignment are:

1. Understand the behaviour and performance characteristics of different Java collection classes.
2. Compare mutable and immutable string-handling approaches.
3. Evaluate list implementations under different insertion, removal, access and iteration workloads.
4. Understand the relationship between equals() and hashCode() in hash-based collections.
5. Compare map implementations based on insertion, lookup and ordering requirements.
6. Apply Java Generics and Comparator to develop reusable ranking components.
7. Implement priority-based scheduling using PriorityQueue.
8. Analyse execution-time differences across multiple workload sizes.
9. Identify performance bottlenecks in existing implementations.
10. Develop and experimentally validate an improved data structure.

# 3. EXPERIMENTAL ENVIRONMENT AND METHODOLOGY

## 3.1 Development Environment

| Component | Configuration |
|---|---|
| Programming Language | Java |
| Java Version | 21.0.11 |
| Development Environment | Eclipse IDE |
| Execution Environment | Java Application |
| Timing Method | System.nanoTime() |
| Collection Framework | Java Collections Framework |

## 3.2 Experimental Methodology

The experiments were designed to compare Java implementations under different operational conditions.

The following approach was used:

1. Identify the collection operation being investigated.
2. Select suitable Java data structures.
3. Develop independent implementations for comparison.
4. Execute the implementations using controlled input data.
5. Record execution time and correctness results.
6. Repeat measurements where implemented.
7. Compare the results with theoretical time complexity.
8. Identify suitable implementations based on application requirements.

## 3.3 Execution-Time Measurement

The primary timing method used was:

```java
long start = System.nanoTime();

// Operation being measured

long end = System.nanoTime();

double elapsed =
    (end - start) / 1_000_000.0;
```

The conversion to milliseconds is performed by dividing the elapsed nanoseconds by 1,000,000.

Some experiments used warm-up executions and repeated measurements. However, the benchmark implementations were educational experiments rather than formally controlled JMH benchmarks. JVM compilation, garbage collection, operating-system scheduling and machine load can influence the recorded timings.

---

# 4. Q1 – STRING, STRINGBUILDER AND STRINGBUFFER

## 4.1 Objective

To compare String, StringBuilder and StringBuffer for generating railway ticket information and identify the most appropriate implementation for different application requirements.

## 4.2 Theoretical Background

### String

String objects are immutable. Once a String object is created, its contents cannot be modified.

Repeated concatenation creates additional String objects because each concatenation produces a new String result.

### StringBuilder

StringBuilder is mutable and allows characters to be appended to an existing character sequence.

It avoids creating a separate intermediate String for every append operation. It is not synchronized and is generally suitable for single-threaded string construction.

### StringBuffer

StringBuffer is mutable and provides synchronized methods.

It is designed for situations where synchronized access to a shared string buffer is required.

## 4.3 Experimental Design

Three workloads were selected:

| Workload | Tickets | Fields per ticket |
|---|---:|---:|
| Workload 1 | 1 | 6 |
| Workload 2 | 10,000 | 50 |
| Workload 3 | 1,000 | 200 |

The three implementations were evaluated using the same workload conditions.

The benchmark used two warm-up executions and five measured repetitions.

## 4.4 Experimental Results

| Workload | String (ms) | StringBuilder (ms) | StringBuffer (ms) |
|---|---:|---:|---:|
| 1 | 0.005 | 0.005 | 0.001 |
| 2 | 21.437 | 4.265 | 4.583 |
| 3 | 30.939 | 1.680 | 1.339 |

## 4.5 Analysis

### Workload 1

All three implementations recorded extremely small execution times.

For a small ticket containing only six fields, the performance difference was negligible.

### Workload 2

For 10,000 tickets with 50 fields each, String concatenation required 21.437 ms.

StringBuilder required only 4.265 ms, while StringBuffer required 4.583 ms.

StringBuilder was approximately five times faster than String concatenation in this workload.

### Workload 3

For 1,000 tickets with 200 fields each, String required 30.939 ms, StringBuilder required 1.680 ms and StringBuffer required 1.339 ms.

The results demonstrated that repeated immutable String concatenation becomes expensive when the amount of generated text increases.

StringBuffer was slightly faster than StringBuilder in this particular measurement. This isolated result does not establish that StringBuffer is generally faster, because JVM execution and measurement variation can influence small timing differences.

## 4.6 Complexity Analysis

| Operation | Expected Behaviour |
|---|---|
| Repeated String concatenation | O(n²) character-copying work in a repeated concatenation pattern |
| StringBuilder append | O(n) total character construction, amortized |
| StringBuffer append | O(n) total character construction, amortized, with synchronization overhead |

Here, n represents the amount of text being constructed under the assumption of bounded field lengths.

## 4.7 Memory Analysis

String concatenation can create multiple intermediate String objects because String is immutable.

StringBuilder and StringBuffer reuse mutable character storage and therefore reduce intermediate object creation.

Actual allocation counts and memory consumption were not captured in this experiment. Consequently, the memory comparison is based on the theoretical behaviour of the implementations rather than measured memory values.

## 4.8 Design Decision

For individual railway ticket generation, StringBuilder is recommended because it provides efficient mutable string construction without unnecessary synchronization.

For a shared synchronized buffer, StringBuffer may be considered. However, synchronization of individual methods does not automatically make an entire multi-step log-entry operation atomic. Explicit synchronization around the complete operation or a suitable logging framework may be required.

## 4.9 Conclusion

The experiment demonstrated that String is suitable for small or fixed text values, while StringBuilder is more efficient for repeated string construction.

StringBuffer provides synchronized operations for shared-buffer scenarios.

Based on the experimental evidence, StringBuilder is the recommended choice for generating individual railway tickets.

---

# 5. Q2 – ARRAYLIST, LINKEDLIST AND ARRAYDEQUE

## 5.1 Objective

To compare ArrayList, LinkedList and ArrayDeque for music playlist operations and determine which structure is most appropriate for different access patterns.

## 5.2 Theoretical Background

### ArrayList

ArrayList uses a dynamically resizable array.

It provides efficient indexed access and amortized constant-time insertion at the end. However, insertion and removal near the beginning require shifting existing elements.

### LinkedList

LinkedList uses a doubly linked structure in which each node maintains references to its neighbouring nodes.

It supports efficient insertion and removal at the beginning and end. However, indexed access requires traversal.

### ArrayDeque

ArrayDeque uses a resizable circular-array structure.

It is designed for efficient insertion and removal at both ends. It does not provide indexed access or direct middle insertion and removal.

## 5.3 Experimental Design

Three playlist sizes were evaluated:

- 1,000 elements
- 10,000 elements
- 100,000 elements

The operations included:

1. Add at end.
2. Insert at front.
3. Insert in the middle.
4. Remove from front.
5. Remove from the middle.
6. Access the k-th element.
7. Full iteration.
8. ArrayDeque end operations and iteration.

Middle insertion and removal measurements used 100 operations. Indexed access used 1,000 accesses.

## 5.4 Experimental Results – 1,000 Elements

| Operation | ArrayList (ms) | LinkedList (ms) |
|---|---:|---:|
| Add at end | 0.213 | 0.098 |
| Insert at front | 0.243 | 0.258 |
| Insert in middle (100 operations) | 0.102 | 0.226 |
| Remove from front | 0.228 | 0.176 |
| Remove from middle (100 operations) | 0.095 | 0.178 |
| Access k-th element (1,000 accesses) | 0.175 | 1.011 |
| Full iteration | 0.158 | 0.145 |

ArrayDeque:

| Operation | Time (ms) |
|---|---:|
| End operations | 0.164 |
| Full iteration | 0.187 |

## 5.5 Experimental Results – 10,000 Elements

| Operation | ArrayList (ms) | LinkedList (ms) |
|---|---:|---:|
| Add at end | 0.605 | 0.288 |
| Insert at front | 3.359 | 0.252 |
| Insert in middle (100 operations) | 0.564 | 1.350 |
| Remove from front | 5.010 | 0.288 |
| Remove from middle (100 operations) | 0.071 | 1.368 |
| Access k-th element (1,000 accesses) | 0.102 | 10.003 |
| Full iteration | 0.590 | 0.156 |

ArrayDeque:

| Operation | Time (ms) |
|---|---:|
| End operations | 0.657 |
| Full iteration | 1.357 |

## 5.6 Experimental Results – 100,000 Elements

| Operation | ArrayList (ms) | LinkedList (ms) |
|---|---:|---:|
| Add at end | 4.911 | 0.515 |
| Insert at front | 229.373 | 2.039 |
| Insert in middle (100 operations) | 1.651 | 9.291 |
| Remove from front | 249.957 | 3.305 |
| Remove from middle (100 operations) | 0.767 | 8.495 |
| Access k-th element (1,000 accesses) | 0.350 | 67.597 |

The available execution screenshots did not provide complete 100,000-element measurements for every operation. Unavailable results have therefore not been estimated or included.

## 5.7 Analysis

### Front Insertion

At 100,000 elements, ArrayList required 229.373 ms, while LinkedList required 2.039 ms.

ArrayList performs element shifting when inserting at index zero. LinkedList can insert at the beginning by updating its head reference and neighbouring links.

### Front Removal

ArrayList required 249.957 ms to remove elements from the front, while LinkedList required 3.305 ms.

This demonstrates the cost of repeated element shifting in ArrayList.

### Indexed Access

At 100,000 elements, ArrayList required 0.350 ms for 1,000 accesses, whereas LinkedList required 67.597 ms.

ArrayList provides direct indexed access, while LinkedList must traverse nodes to reach the requested position.

### Middle Operations

ArrayList performed better than LinkedList in the recorded middle-operation workloads.

Although LinkedList can insert or remove a node efficiently after locating it, locating a middle position requires traversal.

## 5.8 Complexity Analysis

| Operation | ArrayList | LinkedList | ArrayDeque |
|---|---|---|---|
| Add at end | O(1) amortized | O(1) | O(1) amortized |
| Insert at front | O(n) | O(1) | O(1) amortized |
| Insert in middle | O(n) | O(n) | Not directly supported |
| Remove at front | O(n) | O(1) | O(1) amortized |
| Remove in middle | O(n) | O(n) | Not directly supported |
| Indexed access | O(1) | O(n) | Not supported |
| Iteration | O(n) | O(n) | O(n) |

## 5.9 Design Decision

ArrayList is suitable when indexed access and end insertion are frequent.

LinkedList is suitable when frequent insertion and removal occur at the ends or when an iterator is already positioned at the required location.

ArrayDeque is recommended for an Up Next queue that frequently adds and removes elements from both ends.

## 5.10 Conclusion

The experiment demonstrated that no single list implementation is optimal for every playlist operation.

ArrayList provides efficient indexed access, LinkedList provides efficient end modifications, and ArrayDeque provides efficient double-ended queue operations.

The appropriate choice depends on the frequency and location of playlist modifications.

---

# 6. Q3 – HASHSET AND THE HASHCODE–EQUALS CONTRACT

## 6.1 Objective

To investigate how HashSet identifies unique objects and understand the relationship between equals() and hashCode().

## 6.2 Theoretical Background

HashSet stores unique elements and uses hashing to determine where elements should be stored.

The equals() method determines whether two objects are logically equal.

The hashCode() method provides an integer hash value used to identify a potential storage location.

The Java contract requires that if two objects are equal according to equals(), they must return the same hash code.

If this contract is violated, hash-based collections may store logically duplicate objects.

## 6.3 Experimental Tests

Six tests were implemented.

### Test 1 – Duplicate Scans

Two student records representing the same logical student were inserted into a HashSet.

Observed result:

```text
TEST 1: Duplicate Scans
Unique students: 2
```

The result demonstrated the effect of object equality and hashing when duplicate records are processed.

### Test 2 – Different Students

Different student records were inserted.

Observed result:

```text
TEST 2: Different Students
Unique students: 3
```

The collection retained three distinct student records.

### Test 3 – Mutable Registration Number

A student's registration number was modified after insertion.

Observed result:

```text
TEST 3: Mutable Registration Number
Before modification: true
After modification: false
Set size: 1
```

This demonstrates why changing fields used by equals() and hashCode() after insertion can make an object difficult to locate in a HashSet.

The object may remain physically stored in the set even though contains() can no longer find it using its updated hash code.

### Test 4 – Corrected HashSet Verification

The corrected equality and hashing implementation was tested.

Observed result:

```text
TEST 4: Corrected HashSet Verification
Unique students: 2
```

The corrected implementation identified logical duplicates as equal.

### Test 5 – TreeSet Alternative

A TreeSet implementation was evaluated.

Observed result:

```text
TEST 5: TreeSet Alternative
Unique students: 2
Students: [192311010 Asha, 192311011 John]
```

TreeSet maintained the student records according to their comparison ordering.

### Test 6 – HashMap Alternative

A HashMap-based attendance representation was evaluated.

Observed result:

```text
TEST 6: HashMap Alternative
Unique students: 2
Attendance: [192311012 Kumar, 192311013 Latha]
```

HashMap stored student records as keys and associated attendance information as values.

## 6.4 Analysis

The experiments demonstrate that hash-based collections depend on a correct implementation of equals() and hashCode().

If equals() considers two objects equal but hashCode() produces different values, HashSet may fail to identify them as duplicates.

Fields used in equality and hashing should preferably remain immutable while objects are stored in hash-based collections.

## 6.5 Complexity Analysis

| Operation | HashSet |
|---|---|
| Add | O(1) average |
| Contains | O(1) average |
| Remove | O(1) average |
| Iteration | O(n) |

Performance can degrade when hash collisions occur.

## 6.6 Design Decision

For student registration records, immutable identifiers should be used as the basis for equality and hashing.

HashSet is suitable for identifying unique records.

TreeSet is useful when unique records must also be maintained in sorted order.

HashMap is suitable when student identifiers need to be associated with additional information such as attendance status.

## 6.7 Conclusion

The experiment demonstrated the importance of the equals() and hashCode() contract in hash-based collections.

It also showed that mutable fields used for equality and hashing can cause lookup problems after insertion.

Correct equality and hashing implementation is essential for reliable duplicate detection.

---

# 7. Q4 – HASHMAP, TREEMAP AND LINKEDHASHMAP

## 7.1 Objective

To compare HashMap, TreeMap and LinkedHashMap for managing pharmacy inventory and determine the appropriate implementation for medicine lookup and reporting.

## 7.2 Theoretical Background

### HashMap

HashMap stores key-value pairs using hashing.

It provides average O(1) insertion and lookup but does not guarantee iteration order.

### TreeMap

TreeMap maintains its keys in sorted order.

It uses a tree-based structure and provides O(log n) insertion, lookup and removal.

### LinkedHashMap

LinkedHashMap maintains a predictable iteration order, which by default is insertion order.

It provides average O(1) insertion and lookup.

## 7.3 Experimental Design

Three inventory sizes were evaluated:

- 1,000 medicines
- 10,000 medicines
- 100,000 medicines

The experiment measured:

1. Put operations.
2. 100,000 lookups.
3. Code-order report generation.
4. Insertion-order report generation.
5. Ordering verification.

## 7.4 Experimental Results – 1,000 Medicines

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 32.750 | 13.319 | 5.772 |
| 100,000 lookups | 29.961 | 35.687 | 30.631 |
| Code-order report | 9.420 | 0.276 | 0.328 |
| Insertion-order report | 0.103 | 0.182 | 0.239 |

## 7.5 Experimental Results – 10,000 Medicines

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 10.386 | 15.506 | 9.357 |
| 100,000 lookups | 34.868 | 48.517 | 38.127 |
| Code-order report | 3.374 | 0.430 | 0.675 |

The insertion-order report timings were not available in the recorded execution output.

## 7.6 Experimental Results – 100,000 Medicines

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 68.983 | 53.091 | 51.317 |
| 100,000 lookups | 66.832 | 100.778 | 60.084 |
| Code-order report | 21.851 | 2.814 | 4.214 |
| Insertion-order report | 7.738 | 13.668 | 2.247 |

## 7.7 Order Verification

| Map | Code Sorted | Insertion Order Preserved |
|---|---|---|
| HashMap | False | False |
| TreeMap | True | False |
| LinkedHashMap | False | True |

The results demonstrate that TreeMap automatically maintains sorted key order, whereas LinkedHashMap preserves insertion order.

HashMap does not guarantee either ordering behaviour.

## 7.8 Analysis

### Lookup Performance

At 100,000 medicines, the observed times for 100,000 lookups were:

- HashMap: 66.832 ms.
- TreeMap: 100.778 ms.
- LinkedHashMap: 60.084 ms.

The observed average lookup times were approximately:

| Map | Average time per lookup |
|---|---:|
| HashMap | 0.668 microseconds |
| TreeMap | 1.008 microseconds |
| LinkedHashMap | 0.601 microseconds |

LinkedHashMap recorded the lowest lookup time in this particular experiment.

### Code-Order Reporting

At 100,000 medicines, TreeMap required 2.814 ms to generate the code-order report.

HashMap required 21.851 ms, while LinkedHashMap required 4.214 ms.

TreeMap benefits from maintaining sorted keys internally.

### Insertion-Order Reporting

At 100,000 medicines, LinkedHashMap required 2.247 ms for the insertion-order report.

HashMap required 7.738 ms, while TreeMap required 13.668 ms.

LinkedHashMap is suitable for this requirement because it maintains insertion order.

### Insertion Measurement Limitation

The 1,000-medicine HashMap insertion measurement was unusually high compared with the larger workloads.

The insertion measurements were recorded as single executions rather than repeated averaged measurements. They may be influenced by JVM warm-up, garbage collection and other runtime effects. Therefore, they should not be treated as definitive evidence of insertion performance.

## 7.9 Complexity Analysis

| Operation | HashMap | TreeMap | LinkedHashMap |
|---|---|---|---|
| put() | O(1) average | O(log n) | O(1) average |
| get() | O(1) average | O(log n) | O(1) average |
| Iteration | O(n) | O(n) | O(n) |
| Code-sorted report | O(n log n) | O(n) | O(n log n) |
| Space | O(n) | O(n) | O(n) |

## 7.10 Alternative Design

A sorted array combined with Arrays.binarySearch() is another possible implementation.

It provides O(log n) lookup and O(n) insertion because elements may need to be shifted to maintain sorted order.

This approach can be useful when data is mostly static and sorted reporting is frequent.

## 7.11 Deployment Decision

LinkedHashMap is recommended when fast medicine lookup and insertion-order reporting are the primary requirements.

TreeMap is recommended when code-sorted reporting is the dominant requirement.

HashMap is suitable when ordering is not required and average lookup performance is the main priority.

## 7.12 Conclusion

The experiment demonstrated that map selection depends on lookup and ordering requirements.

HashMap provides efficient average lookup without ordering guarantees. TreeMap maintains sorted keys, while LinkedHashMap preserves insertion order.

LinkedHashMap is the recommended overall choice for the pharmacy inventory when both fast lookup and insertion-order reporting are important.

---

# 8. Q5 – GENERICS, COMPARABLE AND COMPARATOR

## 8.1 Objective

To develop a reusable generic leaderboard using Java Generics and Comparator, with multiple ranking criteria and tie-breaking rules.

## 8.2 Problem Description

An inter-college sports leaderboard must rank teams based on:

1. Higher points first.
2. Higher goal difference when points are equal.
3. Alphabetical team name when both points and goal difference are equal.

The same leaderboard component should be reusable with different object types and ranking rules.

## 8.3 Comparable and Comparator

Comparable defines the natural ordering within a class through compareTo().

Comparator provides an external comparison strategy.

Comparator was selected because the same object can be ranked using different rules without modifying its original class.

## 8.4 Generic Implementation

The Leaderboard<T> class stores objects of generic type T.

The Comparator<? super T> parameter allows the leaderboard to receive a comparator for T or a suitable parent type.

The addAll(List<? extends T>) method allows collections containing T or its subclasses.

The ranking() method returns a sorted copy of the original collection.

## 8.5 Test 1 – Sports Leaderboard

Seven teams were evaluated.

Observed output:

| Rank | Team | Points | Goal Difference |
|---:|---|---:|---:|
| 1 | Eagles | 9 | 6 |
| 2 | Bisons | 9 | 4 |
| 3 | Panthers | 9 | 4 |
| 4 | Lions | 6 | 5 |
| 5 | Tigers | 6 | 5 |
| 6 | Cobras | 6 | 1 |
| 7 | Wolves | 3 | 2 |

The output followed all three ranking criteria.

## 8.6 Test 2 – Additional Dataset

A second dataset was used to validate the reusability of the generic component.

| Rank | Team | Points | Goal Difference |
|---:|---|---:|---:|
| 1 | Falcons | 12 | 8 |
| 2 | Hawks | 12 | 8 |
| 3 | Sharks | 12 | 3 |
| 4 | Bears | 10 | 7 |
| 5 | Dolphins | 10 | 7 |
| 6 | Foxes | 5 | 2 |

The results confirmed that the same generic leaderboard could process a different dataset without modifying its implementation.

## 8.7 Test 3 – Original Collection Preservation

The ranking() method creates a new ArrayList and sorts the copy.

This prevents the sorting operation from modifying the internal insertion order of the original collection.

The observed highest-ranked team was Eagles with 9 points and a goal difference of 6.

## 8.8 Test 4 – Faulty Comparator

A faulty comparator was implemented using subtraction:

```java
(a, b) -> b.points - a.points;
```

Integer overflow caused inconsistent comparison results.

Observed results:

| Comparison | Result |
|---|---:|
| A compared with B | -2147483647 |
| B compared with C | -2147483648 |
| A compared with C | 1 |

The resulting relationships were:

- A before B: true.
- B before C: true.
- A before C: false.

This violates the transitivity requirement of a comparator.

## 8.9 Corrected Comparator

The corrected comparator used Comparator.comparingInt() and reversed() instead of subtraction.

Observed results:

| Comparison | Result |
|---|---:|
| A compared with B | -1 |
| B compared with C | -1 |
| A compared with C | -1 |

The corrected implementation produced consistent comparisons without subtraction overflow.

## 8.10 Complexity Analysis

| Operation | Time Complexity |
|---|---|
| Add one entry | O(1) amortized |
| Add a collection | O(k) amortized |
| Generate ranking | O(n log n) |
| Store entries | O(n) |
| Additional sorting-copy space | O(n) |

## 8.11 Design Decisions

Comparator was selected because ranking logic can be changed without modifying the Team class.

Generics allow the leaderboard to be reused for teams, players, colleges and quiz participants.

List<? extends T> allows collections of subtypes to be accepted.

Creating a new sorted list prevents the ranking operation from modifying the original collection.

## 8.12 Conclusion

The experiment successfully implemented a reusable generic leaderboard using Java Generics and Comparator.

The two datasets demonstrated the reusability of the implementation, while the faulty comparator experiment demonstrated how integer overflow can violate transitivity.

The corrected comparator produced consistent results and provided a reliable approach for implementing multi-criteria rankings.

---

# 9. Q6 – QUEUE AND PRIORITYQUEUE

## 9.1 Objective

To implement a hospital emergency triage system using Java PriorityQueue and ensure that patients are treated according to medical severity while maintaining arrival order among patients with equal severity.

## 9.2 Problem Description

Patients are assigned severity levels from 1 to 5.

| Severity | Meaning |
|---:|---|
| 1 | Critical |
| 2 | High priority |
| 3 | Moderate |
| 4 | Low priority |
| 5 | Minor |

Patients with lower severity numbers must be treated first.

For patients with equal severity, the earlier arrival must receive treatment first.

## 9.3 Data Structure Selection

A normal FIFO queue is unsuitable because it removes patients strictly according to arrival order.

PriorityQueue provides priority-based removal using a Comparator.

The comparator evaluates severity first and arrival counter second.

The arrival counter guarantees stable ordering among patients with equal severity.

## 9.4 Correctness Experiment

Eight patients were admitted:

| Arrival | Patient | Severity |
|---:|---|---:|
| 1 | Ravi | 3 |
| 2 | Meena | 1 |
| 3 | John | 3 |
| 4 | Asha | 5 |
| 5 | Kumar | 1 |
| 6 | Lata | 2 |
| 7 | Priya | 4 |
| 8 | Arun | 2 |

### Observed Treatment Order

1. Meena – Severity 1.
2. Kumar – Severity 1.
3. Lata – Severity 2.
4. Arun – Severity 2.
5. Ravi – Severity 3.
6. John – Severity 3.
7. Priya – Severity 4.
8. Asha – Severity 5.

The observed treatment sequence matched the expected priority and arrival-order rules.

## 9.5 PriorityQueue Display Behaviour

Printing a PriorityQueue directly does not guarantee a completely sorted sequence.

PriorityQueue internally maintains a heap. It guarantees that the highest-priority element is at the head but does not guarantee that all elements are displayed in sorted order.

The implementation therefore creates a copy and repeatedly calls poll() to display the actual treatment sequence without modifying the original queue.

## 9.6 Performance Investigation

Three queue sizes were tested:

- 1,000 patients.
- 10,000 patients.
- 100,000 patients.

The benchmark used two warm-up executions and five measured repetitions.

Admission and removal were measured separately.

### Experimental Results

| Queue Size | Average Admission Time (ms) | Average Removal Time (ms) |
|---:|---:|---:|
| 1,000 | 0.663 | 1.109 |
| 10,000 | 0.867 | 2.617 |
| 100,000 | 3.514 | 29.973 |

### Analysis

Admission time increased from 0.663 ms at 1,000 patients to 3.514 ms at 100,000 patients.

Removal time increased from 1.109 ms to 29.973 ms.

These results are consistent with the logarithmic complexity of insertion and removal operations in PriorityQueue.

The measurements represent the total batch processing times rather than individual operation times.

## 9.7 Complexity Analysis

| Operation | PriorityQueue |
|---|---|
| Admission using add() | O(log n) |
| Remove next using poll() | O(log n) |
| View next using peek() | O(1) |
| Display all in priority order | O(n log n) |
| Space | O(n) |

## 9.8 Alternative Implementations

### Sorted LinkedList

A sorted LinkedList can maintain patients in priority order.

Admission requires O(n) traversal to locate the correct position, while removal from the front requires O(1).

### Five FIFO Queues

Since there are five fixed severity levels, five separate FIFO queues can be maintained.

Each patient is inserted into the corresponding severity queue.

The system checks the queues from severity 1 to severity 5 and removes the first patient from the highest-priority nonempty queue.

Admission and removal can both be O(1) when the number of severity levels is fixed.

## 9.9 Starvation and Fairness

Starvation occurs when low-priority patients continuously wait because higher-priority patients keep arriving.

An aging mechanism can be introduced to improve fairness.

The system records waiting time and periodically reviews patient priority. Any priority changes must follow clinically approved rules and must not compromise emergency care.

## 9.10 Deployment Decision

PriorityQueue provides a suitable foundation for emergency triage scheduling.

A hospital receiving 5,000 patients per day has an average arrival rate of approximately 3.47 patients per minute.

The data structure can manage patient priority using O(log n) admission and removal operations.

A production hospital system would additionally require concurrent access handling, persistent patient records, audit logging, clinical escalation policies and starvation prevention.

## 9.11 Conclusion

The experiment successfully implemented priority-based hospital triage using PriorityQueue.

The system correctly handled severity ordering and arrival-order tie-breaking.

Performance measurements demonstrated that the implementation could process a queue containing 100,000 patients.

The study also identified starvation as a limitation of strict priority scheduling and discussed aging as a possible fairness mechanism.

---

# 10. Q7 – DAY-4 DESIGN CHALLENGE: DIAGNOSE, REDESIGN AND VALIDATE

## 10.1 Objective

To identify a performance limitation in an existing collection-based implementation, redesign the data structure to address the underlying problem, and validate the improvement experimentally.

## 10.2 A. Diagnose the Bottleneck

### Original Implementation

The original Q2 experiment used ArrayList for music playlist operations.

ArrayList stores elements in a contiguous array.

When an element is removed from the beginning, all remaining elements must shift one position to the left.

Repeated front removals therefore cause increasing amounts of data movement.

### Evidence from Q2

At 100,000 elements:

| Operation | ArrayList (ms) | LinkedList (ms) |
|---|---:|---:|
| Insert at front | 229.373 | 2.039 |
| Remove from front | 249.957 | 3.305 |

The measurements demonstrated that repeated front modifications were a significant limitation of ArrayList.

For n front removals, the total number of element shifts is proportional to:

(n - 1) + (n - 2) + ... + 1

This results in O(n²) total work.

### Bottleneck Identification

The main bottleneck was repeated element shifting during front insertion and removal.

The limitation originated from the contiguous-array storage arrangement of ArrayList.

## 10.3 B. Define the Design Objective

The redesign aimed to develop a playlist data structure that supports efficient insertion and removal at both ends.

The objectives were:

1. Avoid shifting all remaining elements during front removal.
2. Support efficient front and rear insertion.
3. Preserve playlist order.
4. Maintain efficient indexed access.
5. Reduce the execution time of repeated front-removal operations.

The primary measurable objective was to improve the time required to remove 100,000 elements from the front.

## 10.4 C. Design the Revised Solution

### Proposed Design: Custom Circular Array Playlist

A custom circular-array structure was developed.

The structure uses:

- Object[] elements to store playlist entries.
- int head to identify the first logical element.
- int size to track the number of stored elements.

The array is treated as circular, allowing the head position to move without physically shifting the stored elements.

### Front Insertion

The head index moves backward and the new element is stored at the new position.

### Front Removal

The current head element is returned and the head index moves forward.

### Rear Insertion and Removal

Elements are added or removed using the logical end position.

### Resizing

When the array becomes full, its capacity is doubled. Existing elements are copied into the new array in logical playlist order.

### Complexity Analysis

| Operation | ArrayList | Circular Array |
|---|---|---|
| Add at front | O(n) | O(1) amortized |
| Add at end | O(1) amortized | O(1) amortized |
| Remove at front | O(n) | O(1) |
| Remove at end | O(1) | O(1) |
| Indexed access | O(1) | O(1) |
| Space | O(n) | O(n) |

### Trade-offs

The redesigned structure introduces additional implementation complexity.

Circular indexing, empty-state handling, capacity management and resizing must be implemented correctly.

Resizing requires O(n) time and additional temporary storage, but it occurs occasionally rather than on every front modification.

## 10.5 D. Predict Before Implementing

Before implementation, the following predictions were established:

- Front insertion would improve from O(n) to O(1) amortized.
- Front removal would improve from O(n) to O(1).
- Indexed access would remain O(1).
- The performance improvement would become more significant as workload size increased.

The primary prediction was that the circular array would outperform ArrayList for repeated front removals, especially at 100,000 elements.

## 10.6 E. Implement and Validate the Redesign

### Correctness Verification

The circular playlist was tested using the following sequence:

1. Add B and C at the end.
2. Add A at the front.
3. Remove the first element.
4. Remove the last element.
5. Add X at the front.
6. Add Y at the end.

Expected final sequence:

X, B, Y

The implementation maintained the logical order of the elements.

The recorded console output displayed X and B in its final-sequence line, while the reported size was 3. The print statement was subsequently identified as displaying only two elements; it should be updated to print all three elements to provide complete visual verification.

### Performance Experiment

The original ArrayList and redesigned circular array were tested using identical workload sizes.

The initial population of each structure occurred outside the timed section.

Only repeated front-removal operations were measured.

Two warm-up executions were performed, followed by five measured repetitions. Average execution times were calculated.

### Experimental Results

| Workload Size | ArrayList (ms) | Circular Array (ms) | Speedup |
|---:|---:|---:|---:|
| 1,000 | 0.136 | 0.074 | 1.83x |
| 10,000 | 2.242 | 0.163 | 13.76x |
| 100,000 | 231.238 | 0.323 | 716.13x |

### Speedup Calculation

Speedup is calculated using:

Speedup = Original Execution Time / Redesigned Execution Time

For 100,000 elements:

Speedup = 231.238 / 0.323

Speedup = approximately 716.13x

The redesigned circular array was approximately 716 times faster than ArrayList for the tested 100,000-element front-removal workload.

## 10.7 F. Defend the Final Design

### Prediction Evaluation

The prediction was supported by the experimental results.

The circular array outperformed ArrayList at all three workload sizes.

The improvement increased as the workload size increased.

### Magnitude of Improvement

At 1,000 elements, the improvement was 1.83x.

At 10,000 elements, the improvement was 13.76x.

At 100,000 elements, the improvement was 716.13x.

The significant improvement resulted from avoiding repeated element shifting.

### New Costs and Trade-offs

The redesigned structure requires additional logic for circular indexing, capacity management and resizing.

Resizing requires copying elements into a larger array, introducing occasional O(n) work.

Careful handling of empty and full states is also necessary.

### Situations Where ArrayList Remains Preferable

ArrayList remains suitable when:

- Frequent indexed access is required.
- Most insertions occur at the end.
- Front modifications are uncommon.
- A simple and established collection implementation is preferred.

The circular array is preferable when frequent modifications occur at both ends.

## 10.8 Conclusion

The design challenge identified repeated element shifting as the main performance limitation of ArrayList during front removals.

A custom circular-array playlist was developed to address the underlying problem by moving the head index instead of shifting existing elements.

The experimental results demonstrated improvements of 1.83x, 13.76x and 716.13x for workload sizes of 1,000, 10,000 and 100,000 respectively.

The redesigned structure achieved its primary performance objective while preserving the logical order of playlist elements.

This experiment demonstrates the importance of understanding the internal behaviour of a data structure before redesigning an application.

---

# 11. OVERALL FINDINGS

The seven experiments demonstrated that data structure selection must be based on the operations and requirements of an application.

The major findings are summarised below.

| Question | Main Finding | Recommended Design |
|---|---|---|
| Q1 | Mutable string construction reduces repeated object creation | StringBuilder |
| Q2 | Different list implementations suit different access patterns | ArrayList, LinkedList or ArrayDeque depending on workload |
| Q3 | Correct equals() and hashCode() implementations are essential | HashSet with immutable equality fields |
| Q4 | Map selection depends on lookup and ordering requirements | LinkedHashMap or TreeMap depending on reporting needs |
| Q5 | Generics and Comparator enable reusable multi-criteria ranking | Generic Leaderboard<T> |
| Q6 | PriorityQueue supports severity-based scheduling | PriorityQueue with arrival-order tie-breaking |
| Q7 | Repeated front removal causes expensive ArrayList shifting | Custom circular-array playlist |

## 11.1 Important Performance Observations

1. StringBuilder demonstrated a substantial advantage over repeated String concatenation for larger string-generation workloads.
2. ArrayList demonstrated efficient indexed access but poor performance for repeated front modifications.
3. LinkedList supported efficient front operations but performed poorly for repeated indexed access.
4. TreeMap maintained sorted keys without requiring an additional sorting step.
5. LinkedHashMap maintained insertion order while providing average constant-time lookup.
6. Comparator chaining allowed multiple ranking conditions to be implemented without modifying the original object class.
7. PriorityQueue successfully processed patients according to severity and arrival order.
8. The custom circular array substantially reduced front-removal time compared with ArrayList.

---

# 12. CONCLUSION

This assignment provided practical experience with Java collection classes, generic programming, comparison strategies, priority scheduling and performance-oriented data structure design.

The experiments demonstrated that different collection implementations provide different advantages depending on the application workload.

StringBuilder was identified as a suitable choice for repeated string construction. ArrayList, LinkedList and ArrayDeque were compared based on insertion, removal, indexed access and iteration requirements. The HashSet experiment demonstrated the importance of the equals() and hashCode() contract. Map implementations were evaluated based on lookup and ordering requirements.

The generic leaderboard demonstrated how Java Generics and Comparator can be used to build reusable components with multiple ranking conditions. The emergency triage implementation demonstrated priority-based scheduling and the importance of preserving arrival order among patients with equal severity.

The final design challenge was particularly important because it required identifying the cause of a performance limitation rather than simply replacing one collection with another.

The custom circular-array playlist addressed the repeated shifting performed by ArrayList during front removals. Experimental results showed that the redesigned implementation achieved a speedup of 716.13x for the 100,000-element workload.

Overall, the assignment reinforced the importance of analysing data structure behaviour, selecting suitable implementations, measuring performance and validating design decisions using experimental evidence.

The work followed the engineering process:

**Predict → Design → Implement → Measure → Analyse → Evaluate → Diagnose → Redesign → Validate**

---

# 13. REFERENCES

1. Oracle, *Java Platform, Standard Edition 21 API Specification*, https://docs.oracle.com/en/java/javase/21/docs/api/
2. Oracle, *Java Collections Framework*, Java SE Documentation.
3. Oracle, *String Class Documentation*, Java SE 21.
4. Oracle, *StringBuilder Class Documentation*, Java SE 21.
5. Oracle, *StringBuffer Class Documentation*, Java SE 21.
6. Oracle, *ArrayList Class Documentation*, Java SE 21.
7. Oracle, *LinkedList Class Documentation*, Java SE 21.
8. Oracle, *ArrayDeque Class Documentation*, Java SE 21.
9. Oracle, *HashSet, HashMap, TreeMap and LinkedHashMap Documentation*, Java SE 21.
10. Oracle, *Comparator and Comparable Interface Documentation*, Java SE 21.
11. Oracle, *PriorityQueue Class Documentation*, Java SE 21.

---

# APPENDIX A – SOURCE CODE ORGANISATION

The Java source files for the seven experiments are maintained separately in the GitHub repository.

Suggested repository structure:

```text
CSA09-192372007-Assignment2/
│
├── README.md
│
├── Task01/
│   ├── Q1StringComparison.java
│   ├── Q1_Report.md
│   ├── results.csv
│   └── screenshots/
│       └── execution_output.png
│
├── Task02/
│   ├── Q2CollectionComparison.java
│   ├── Q2_Report.md
│   ├── results.csv
│   └── screenshots/
│
├── Task03/
│   ├── Attendance.java
│   └── screenshots/
│
├── Task04/
│   ├── PharmacyInventory.java
│   ├── Q4_Report.md
│   └── screenshots/
│
├── Task05/
│   ├── LeaderboardDemo.java
│   ├── Q5_Report.md
│   └── screenshots/
│
├── Task06/
│   ├── Triage.java
│   ├── Q6_Report.md
│   └── screenshots/
│
├── Task07/
│   ├── Q7Redesign.java
│   ├── Q7_Report.md
│   └── screenshots/
│
└── Master_Assignment_Report.md
```

# APPENDIX B – EXPERIMENTAL LIMITATIONS

The following limitations should be considered while interpreting the results:

1. Execution times can vary because of JVM compilation, garbage collection, operating-system scheduling and machine load.
2. The experiments were conducted as educational benchmarks and were not implemented using a formal benchmarking framework such as JMH.
3. Q1 memory allocation counts and actual memory consumption were not measured.
4. Some Q2 and Q4 measurements were unavailable in the recorded execution output and have not been estimated.
5. Q4 insertion timings were single recorded measurements and should not be treated as definitive performance comparisons.
6. Q5 validates comparator behaviour using selected test cases rather than an exhaustive mathematical proof.
7. Q6 measures complete admission and removal batches rather than individual operation latency.
8. Q7 measures repeated front removals after initial collection population, so the results specifically represent that workload rather than every possible playlist operation.

These limitations do not invalidate the experiments but should be considered when drawing conclusions about performance in production systems.

---

**END OF REPORT**
