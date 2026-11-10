# Q4 – Choosing a Map for a Pharmacy Inventory

## 1. Objective

To compare HashMap, TreeMap and LinkedHashMap for managing hospital pharmacy inventory and identify the most suitable data structure for medicine lookup, code-sorted reporting and insertion-order reporting.

## 2. Experimental Design

Three inventory sizes were selected:

- 1,000 medicines
- 10,000 medicines
- 100,000 medicines

The following operations were evaluated:

1. Insertion using put().
2. 100,000 medicine lookups using get().
3. Generation of a medicine-code-sorted report.
4. Generation of an insertion-order report.
5. Verification of output ordering.

Medicines were deliberately inserted in reverse code order to distinguish insertion order from sorted order.

## 3. Experimental Results

### Inventory Size: 1,000

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 32.750 | 13.319 | 5.772 |
| 100,000 lookups | 29.961 | 35.687 | 30.631 |
| Code-order report | 9.420 | 0.276 | 0.328 |
| Insertion-order report | 0.103 | 0.182 | 0.239 |

### Inventory Size: 10,000

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 10.386 | 15.506 | 9.357 |
| 100,000 lookups | 34.868 | 48.517 | 38.127 |
| Code-order report | 3.374 | 0.430 | 0.675 |
| Insertion-order report | Not captured | Not captured | Not captured |

### Inventory Size: 100,000

| Operation | HashMap (ms) | TreeMap (ms) | LinkedHashMap (ms) |
|---|---:|---:|---:|
| Put | 68.983 | 53.091 | 51.317 |
| 100,000 lookups | 66.832 | 100.778 | 60.084 |
| Code-order report | 21.851 | 2.814 | 4.214 |
| Insertion-order report | 7.738 | 13.668 | 2.247 |

The insertion-order timings for 10,000 medicines were not available in the recorded output and have therefore not been included.

## 4. Analysis of Map Implementations

### HashMap

HashMap provides average O(1) insertion and lookup operations. However, it does not guarantee sorted order or insertion order. Therefore, additional sorting is necessary to generate a medicine-code report, and a separate record of insertion order is required to generate an insertion-order report.

### TreeMap

TreeMap maintains keys in sorted order. Its insertion and lookup operations take O(log n) time. It is particularly suitable for generating medicine-code reports because the keys are already sorted. However, it does not preserve insertion order.

### LinkedHashMap

LinkedHashMap maintains insertion order while providing average O(1) insertion and lookup. It is suitable for applications that require fast medicine lookup and insertion-order reporting. Additional sorting is required for code-sorted reports.

## 5. Order Verification

The experiment produced the following results:

| Map | Code-sorted order | Insertion order |
|---|---|---|
| HashMap | False | False |
| TreeMap | True | False |
| LinkedHashMap | False | True |

The results confirm that TreeMap naturally produces sorted output, while LinkedHashMap preserves insertion order. HashMap does not guarantee either ordering behaviour.

## 6. Complexity Analysis

| Operation | HashMap | TreeMap | LinkedHashMap |
|---|---|---|---|
| put() | O(1) average | O(log n) | O(1) average |
| get() | O(1) average | O(log n) | O(1) average |
| Iteration | O(n) | O(n) | O(n) |
| Code-sorted report | O(n log n) | O(n) | O(n log n) |
| Insertion-order report | O(n) with saved order | O(n) with saved order | O(n) |
| Space | O(n) | O(n) | O(n) |

### Alternative: Sorted Array

An alternative is to maintain an array of medicines sorted by medicine code and use Arrays.binarySearch().

- Lookup: O(log n).
- Insertion: O(n), due to shifting elements.
- Code-sorted report: O(n).
- Space: O(n).

Although binary search provides efficient lookup, maintaining the sorted order during frequent insertions introduces additional overhead.

## 7. Scalability Inquiry

An acceptable response time was defined as 10 microseconds or less for one medicine lookup.

For 100,000 medicines, the observed average lookup times were:

| Map | Average lookup time |
|---|---:|
| HashMap | 0.668 microseconds |
| TreeMap | 1.008 microseconds |
| LinkedHashMap | 0.601 microseconds |

All three implementations satisfied the defined lookup requirement in the experiment.

The remaining lookup time is caused by hashing, key comparisons, bucket traversal or tree traversal, and JVM execution overhead.

## 8. Deployment Decision

LinkedHashMap is recommended when fast medicine-code lookups and insertion-order reporting are the primary requirements. It provides average constant-time lookup and preserves the order in which medicines were added.

If code-sorted reports are generated frequently, TreeMap is a suitable alternative because it automatically maintains medicines in sorted order.

HashMap is appropriate when ordering is not required and fast average lookup is the primary objective.

Hashtable is not preferred in modern Java applications because it is a legacy synchronized collection. Modern alternatives such as HashMap, LinkedHashMap and ConcurrentHashMap provide more appropriate choices depending on the application's requirements.

## 9. Conclusion

The experiment demonstrated that selecting a Map implementation depends on the application's lookup and reporting requirements. HashMap provides efficient average lookup without ordering guarantees. TreeMap maintains sorted keys at the cost of logarithmic insertion and lookup. LinkedHashMap combines efficient average lookup with insertion-order preservation.

Based on the experimental results, LinkedHashMap is recommended for the pharmacy inventory when lookup performance and insertion-order reporting are important. TreeMap is preferable when sorted medicine-code reporting is the dominant requirement.

The experiment also demonstrated that data structure selection should be based on actual application needs rather than relying only on theoretical complexity.
