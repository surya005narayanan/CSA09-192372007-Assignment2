# Q7 – Day-4 Design Challenge: Diagnosing and Redesigning a Playlist

## A. Diagnose the Bottleneck – 2 Marks

### 1. Original Implementation

The original implementation used ArrayList to manage a music playlist.

ArrayList stores elements in a contiguous array. Although it provides efficient indexed access and amortized constant-time insertion at the end, inserting or removing elements at the beginning requires shifting the remaining elements.

### 2. Experimental Evidence

The original Q2 experiment demonstrated the limitation at 100,000 elements:

| Operation | ArrayList | LinkedList |
|---|---:|---:|
| Insert at front | 229.373 ms | 2.039 ms |
| Remove from front | 249.957 ms | 3.305 ms |

The repeated front operations became expensive because ArrayList had to shift the remaining elements after each modification.

For n front removals, the total shifting work is:

(n - 1) + (n - 2) + ... + 1

Therefore, repeated front removals require O(n²) total work.

### 3. Bottleneck Identification

The main bottleneck is the shifting operation performed internally by ArrayList during front insertion and removal.

The problem is not the playlist data itself but the internal storage arrangement of the chosen collection.

## B. Define the Design Objective – 2 Marks

The redesign aims to develop a playlist structure that supports efficient modifications at both ends without shifting all existing elements.

The measurable objectives are:

1. Reduce the execution time of repeated front removals.
2. Avoid unnecessary element shifting.
3. Preserve the logical order of playlist elements.
4. Support insertion and removal at both ends.
5. Maintain efficient indexed access.

The primary performance objective is to reduce the time required to remove 100,000 elements from the front compared with the original ArrayList implementation.

## C. Design the Revised Solution – 4 Marks

### 1. Proposed Design

A custom Circular Array Playlist was developed.

Instead of physically shifting elements, the implementation uses a circular array with three important variables:

- elements: stores the playlist entries.
- head: represents the starting position of the logical playlist.
- size: represents the current number of elements.

### 2. Working Principle

When a song is added at the front, the head index moves backward and the new element is inserted at that position.

When a song is removed from the front, the head index moves forward.

The array wraps around when the beginning or end is reached.

When the array becomes full, its capacity is doubled and the elements are copied into a new array in their logical order.

### 3. Design Representation

CircularPlaylist:

- Object[] elements
- int head
- int size
- addFirst()
- addLast()
- removeFirst()
- removeLast()
- get(index)
- grow()

### 4. Complexity Analysis

| Operation | Original ArrayList | Circular Array |
|---|---|---|
| Add at front | O(n) | O(1) amortized |
| Add at end | O(1) amortized | O(1) amortized |
| Remove at front | O(n) | O(1) |
| Remove at end | O(1) | O(1) |
| Indexed access | O(1) | O(1) |
| Storage | O(n) | O(n) |

The circular array avoids repeated shifting by updating the head index.

### 5. Trade-offs

The redesign introduces additional implementation complexity because circular indexing and resizing must be handled correctly.

Resizing requires O(n) time and additional temporary memory. However, resizing occurs occasionally rather than during every front modification.

The structure requires O(n) storage and provides efficient operations at both ends.

## D. Predict Before Implementing – 1 Mark

Before implementation, the expected behaviour was:

- Front insertion would improve from O(n) to O(1) amortized.
- Front removal would improve from O(n) to O(1).
- Indexed access would remain O(1).
- Repeated front operations would show increasingly significant improvements as the playlist size increased.

The primary prediction was that the circular array would outperform ArrayList for repeated front removals, particularly at 100,000 elements.

## E. Implement and Validate the Redesign – 2 Marks

### 1. Correctness Verification

The redesigned circular playlist was tested using the following operations:

1. Add B and C at the end.
2. Add A at the front.
3. Remove the first element.
4. Remove the last element.
5. Add X at the front.
6. Add Y at the end.

The expected final sequence was:

X, B, Y

The implementation successfully maintained the logical order of the elements.

### 2. Performance Experiment

The original ArrayList and redesigned circular array were tested using the same workloads.

The initial collection population was performed outside the timed section. Only repeated front removals were measured.

Two warm-up runs were performed, followed by five measured repetitions. Average execution times were calculated.

### 3. Observed Results

| Workload Size | ArrayList (ms) | Circular Array (ms) | Improvement |
|---:|---:|---:|---:|
| 1,000 | 0.136 | 0.074 | 1.83x |
| 10,000 | 2.242 | 0.163 | 13.76x |
| 100,000 | 231.238 | 0.323 | 716.13x |

### 4. Speedup Calculation

Speedup is calculated as:

Speedup = Original execution time / Redesigned execution time

For 100,000 elements:

Speedup = 231.238 / 0.323

Speedup = approximately 716.13x

The redesigned circular array was approximately 716 times faster than ArrayList for this particular front-removal workload.

## F. Defend the Final Design – 2 Marks

### 1. Prediction Evaluation

The experimental results supported the original prediction.

The circular array outperformed ArrayList at all three workload sizes. The improvement became increasingly significant as the workload increased.

### 2. Magnitude of Improvement

At 1,000 elements, the improvement was 1.83x.

At 10,000 elements, the improvement was 13.76x.

At 100,000 elements, the improvement reached 716.13x.

This demonstrates the benefit of eliminating repeated element shifting.

### 3. New Costs and Trade-offs

The redesigned structure requires additional logic for circular indexing, capacity management and resizing.

Occasional resizing requires copying existing elements into a larger array, resulting in O(n) work during those operations.

The implementation also requires careful handling of empty and full states to avoid indexing errors.

### 4. Situations Where ArrayList Is Still Preferable

ArrayList remains suitable when:

- Frequent indexed access is required.
- Elements are mostly appended at the end.
- Front insertion and removal are uncommon.
- A simple and well-established collection implementation is preferred.

The circular array is preferable when frequent modifications occur at both ends.

## Conclusion

The design challenge identified repeated element shifting as the main limitation of ArrayList during front removals.

A custom circular array was developed to eliminate this unnecessary work by maintaining a movable head index.

Experimental validation demonstrated improvements of 1.83x, 13.76x and 716.13x for workloads of 1,000, 10,000 and 100,000 elements respectively.

The redesigned structure successfully achieved its primary performance objective while preserving playlist order.

The experiment demonstrates that understanding the internal behaviour of a data structure is essential before redesigning an application. Rather than simply replacing one collection with another, the revised solution addresses the underlying cause of the performance limitation.
