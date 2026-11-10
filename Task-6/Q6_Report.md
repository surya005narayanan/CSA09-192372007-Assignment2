# Q6 – Priority, Order and Fairness in a Queue

## 1. Objective

To design and implement a hospital emergency triage system using Java PriorityQueue. The system prioritizes patients based on medical severity while preserving arrival order among patients with equal severity.

## 2. Problem Requirements

Patients are assigned severity levels from 1 to 5:

- Severity 1: Critical
- Severity 2: High priority
- Severity 3: Moderate
- Severity 4: Low priority
- Severity 5: Minor

The patient with the lowest severity number must be treated first. When two patients have equal severity, the patient who arrived earlier must be treated first.

## 3. Data Structure Selection

A normal FIFO Queue implemented using LinkedList or ArrayDeque is insufficient because it always removes the earliest admitted patient, regardless of severity.

A PriorityQueue is more suitable because it removes elements according to a specified ordering rule.

The implementation uses a Comparator that compares:

1. Severity in ascending order.
2. Arrival counter in ascending order.

The arrival counter provides a unique sequence number for every admitted patient and preserves FIFO order among patients with equal severity.

## 4. Experimental Design and Correctness Verification

Eight patients were admitted in the following order:

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

### Observed Treatment Sequence

Meena(S1), Kumar(S1), Lata(S2), Arun(S2), Ravi(S3), John(S3), Priya(S4), Asha(S5).

The observed sequence matched the expected order.

The experiment confirmed that:

- Severity 1 patients were treated before all other severity levels.
- Patients with equal severity were treated according to arrival order.
- All eight patients were successfully removed from the queue.

## 5. PriorityQueue Display Behaviour

Printing a PriorityQueue directly does not guarantee that elements will appear in sorted treatment order.

A PriorityQueue internally maintains a heap structure. The heap guarantees that the highest-priority element is available at the head, but it does not fully sort every element.

To display the complete treatment order, a copy of the queue is created and its elements are repeatedly removed using poll().

This approach displays the correct treatment sequence without modifying the original waiting queue.

## 6. Performance Investigation

Three queue sizes were evaluated: 1,000, 10,000 and 100,000 patients.

The admission and removal operations were measured separately, with five repetitions after two warm-up runs.

### Experimental Results

| Queue Size | Average Admission Time (ms) | Average Removal Time (ms) |
|---:|---:|---:|
| 1,000 | 0.663 | 1.109 |
| 10,000 | 0.867 | 2.617 |
| 100,000 | 3.514 | 29.973 |

The recorded timings represent the total time for admitting or removing the complete batch of patients, rather than the time for one individual operation.

### Analysis

As the number of patients increased, both admission and removal times increased.

The admission time increased from 0.663 ms for 1,000 patients to 3.514 ms for 100,000 patients.

The removal time increased from 1.109 ms to 29.973 ms.

This behaviour is consistent with the expected logarithmic complexity of PriorityQueue insertion and removal operations.

## 7. Complexity Analysis

| Operation | PriorityQueue | Sorted LinkedList | Five FIFO Queues |
|---|---|---|---|
| Admission | O(log n) | O(n) | O(1) |
| Remove next | O(log n) | O(1) | O(1) |
| Space | O(n) | O(n) | O(n) |

### Alternative 1 – Sorted LinkedList

A LinkedList can maintain patients in sorted order by inserting each new patient at the correct position.

Admission requires searching for the correct insertion position, taking O(n) time.

Removing the first patient takes O(1) time.

### Alternative 2 – Five FIFO Queues

Since the hospital uses only five fixed severity levels, five separate FIFO queues can be maintained.

Each patient is inserted into the queue corresponding to their severity.

To select the next patient, the system checks severity queues from 1 to 5 and removes the first patient from the highest-priority nonempty queue.

Admission and removal can both be performed in O(1) time because the number of severity levels is fixed.

## 8. Starvation and Fairness

Starvation occurs when a low-priority patient waits indefinitely because higher-priority patients continuously arrive.

For example, a severity-5 patient may remain in the waiting queue if severity-1 to severity-4 patients keep arriving.

A possible solution is an aging mechanism.

The system records the waiting time of each patient and periodically increases their priority after predefined waiting-time thresholds.

For example, a severity-5 patient who exceeds a specified waiting threshold may be promoted to severity 4, subject to clinical approval and safety rules.

This helps reduce starvation while maintaining medically appropriate prioritization.

## 9. Deployment Decision

The proposed PriorityQueue design is suitable as a foundation for a hospital triage scheduling component.

A hospital receiving 5,000 patients per day has an average arrival rate of:

5,000 / 24 / 60 = approximately 3.47 patients per minute.

PriorityQueue provides O(log n) admission and removal operations and O(1) access to the next patient through peek().

The experimental results demonstrate that the implementation can process a queue containing 100,000 patients.

However, a real hospital deployment would require additional features such as concurrent access handling, persistent patient records, audit logging, clinical escalation policies and starvation prevention.

## 10. Conclusion

The experiment successfully implemented a hospital emergency triage system using Java PriorityQueue and Comparator.

The system correctly prioritized patients based on severity and preserved arrival order among patients with equal severity.

Performance testing across three queue sizes demonstrated that the implementation could efficiently handle increasing numbers of patients.

The study also identified starvation as an important limitation of strict priority scheduling and proposed an aging mechanism to improve fairness.

Therefore, PriorityQueue provides an effective foundation for emergency triage scheduling, while additional clinical and operational safeguards are necessary for real-world deployment.
