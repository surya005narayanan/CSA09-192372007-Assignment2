# ASSIGNMENT 2 – QUESTION 1
## Building Text Under Load: String, StringBuilder or StringBuffer?

**Subject:** Programming in Java  
**Course Outcome:** CO2  
**Marks:** 6  
**Problem Context:** Railway e-Ticket Generation

---

## 1. INTRODUCTION

A railway reservation system generates an e-ticket for every successful booking. Each ticket contains multiple fields, including PNR number, passenger name, train details, coach, berth, fare and journey information. These fields must be combined into a single printable text block.

When the number of bookings increases, the method used to construct ticket information can affect execution time, memory consumption and overall application performance.

Java provides three approaches for constructing strings:

- String
- StringBuilder
- StringBuffer

This experiment investigates the performance differences between these approaches under different workloads and identifies the most suitable solution for a railway reservation system.

## 2. OBJECTIVES

1. Compare String, StringBuilder and StringBuffer for constructing ticket information.
2. Understand the impact of String immutability on performance.
3. Measure execution time under different workloads.
4. Estimate intermediate object creation and memory requirements.
5. Analyse the time and space complexity of each approach.
6. Recommend a suitable approach for individual ticket generation and shared booking logs.

## 3. THEORETICAL BACKGROUND

### 3.1 String

String objects in Java are immutable. Once created, their contents cannot be modified.

When concatenation is performed using the `+` operator, a new resulting String is created rather than modifying the existing String.

Example:

```java
String ticket = "";
ticket = ticket + "PNR";
ticket = ticket + "Passenger";
ticket = ticket + "Train";
```

Repeated concatenation creates additional String objects and repeatedly copies previously accumulated characters.

### 3.2 StringBuilder

StringBuilder is a mutable sequence of characters. Its append() method modifies its internal character storage instead of creating a new String for every append operation.

Example:

```java
StringBuilder ticket = new StringBuilder();

ticket.append("PNR");
ticket.append("Passenger");
ticket.append("Train");

String result = ticket.toString();
```

It provides efficient string construction and does not synchronize its methods.

### 3.3 StringBuffer

StringBuffer is also a mutable sequence of characters. However, its methods are synchronized, providing thread-safe individual operations.

Example:

```java
StringBuffer ticket = new StringBuffer();

ticket.append("PNR");
ticket.append("Passenger");
ticket.append("Train");

String result = ticket.toString();
```

Synchronization can introduce additional overhead compared with StringBuilder.

## 4. PREDICTION BEFORE IMPLEMENTATION

Before conducting the experiments, the following predictions were made:

1. String was expected to perform poorly as the number of fields and tickets increased because of its immutable nature.
2. StringBuilder was expected to provide better performance because it uses mutable internal storage and reduces repeated object creation.
3. StringBuffer was expected to perform similarly to StringBuilder, but synchronization could introduce additional overhead.
4. For a small ticket containing only a few fields, the performance difference between the three approaches was expected to be negligible.
5. For large workloads containing thousands of tickets and multiple fields, StringBuilder was expected to provide the best overall performance.

## 5. EXPERIMENTAL DESIGN

Three different workloads were designed to investigate the performance of the three approaches.

| Workload | Number of tickets | Fields per ticket | Purpose |
|---|---:|---:|---|
| Workload 1 | 1 | 6 | Small workload |
| Workload 2 | 10,000 | 50 | Large number of tickets |
| Workload 3 | 1,000 | 200 | Large number of fields per ticket |

The three approaches were implemented using identical input fields to maintain consistency.

### 5.1 Performance Measurement

Execution time was measured using Java's `System.nanoTime()` method.

The measurement procedure included:

1. Executing two warm-up iterations.
2. Running each implementation five times.
3. Recording execution time for every run.
4. Calculating the average execution time.
5. Converting the average time into milliseconds.

The formula used was:

Average Execution Time = Total Execution Time / Number of Repetitions

All three implementations used the same workload parameters.

## 6. IMPLEMENTATION

The experiment was implemented using Java.

Three separate methods were developed:

- `usingString()` – constructs tickets using repeated String concatenation.
- `usingStringBuilder()` – constructs tickets using StringBuilder.append().
- `usingStringBuffer()` – constructs tickets using StringBuffer.append().

A common measurement method was used to calculate average execution time using `System.nanoTime()`.

The generated ticket length was also accumulated as a checksum to ensure that the constructed results were used during execution.

**Source file:** Q1StringComparison.java

## 7. EXPERIMENTAL RESULTS

The following results were obtained from the Java implementation.

### 7.1 Workload 1 – Small Workload

**Configuration:** 1 ticket containing 6 fields.

| Approach | Average execution time |
|---|---:|
| String | 0.005 ms |
| StringBuilder | 0.005 ms |
| StringBuffer | 0.001 ms |

**Observation:**

All three approaches demonstrated negligible execution times. Since only one ticket with six fields was generated, the performance difference was insignificant.

The lower observed time of StringBuffer should not be considered evidence that it is generally faster, because measurement noise can significantly influence such small workloads.

### 7.2 Workload 2 – Large Number of Tickets

**Configuration:** 10,000 tickets containing 50 fields each.

| Approach | Average execution time |
|---|---:|
| String | 21.437 ms |
| StringBuilder | 4.265 ms |
| StringBuffer | 4.583 ms |

**Observation:**

String required the highest execution time of 21.437 ms.

StringBuilder completed the workload in 4.265 ms, while StringBuffer required 4.583 ms.

StringBuilder was approximately 5.03 times faster than String.

This demonstrates the overhead associated with repeated String concatenation when generating a large number of tickets.

### 7.3 Workload 3 – Large Number of Fields

**Configuration:** 1,000 tickets containing 200 fields each.

| Approach | Average execution time |
|---|---:|
| String | 30.939 ms |
| StringBuilder | 1.680 ms |
| StringBuffer | 1.339 ms |

**Observation:**

String required 30.939 ms, whereas StringBuilder required only 1.680 ms.

StringBuffer completed the experiment in 1.339 ms.

StringBuilder was approximately 18.42 times faster than String.

Although StringBuffer recorded a lower execution time than StringBuilder in this particular experiment, this result does not establish that StringBuffer is generally faster. Further controlled benchmarking is necessary.

## 8. PERFORMANCE COMPARISON

The speedup was calculated using:

Speedup = Execution Time of String / Execution Time of Alternative Approach

| Workload | Comparison | Speedup |
|---|---|---:|
| Workload 2 | String vs StringBuilder | 5.03× |
| Workload 2 | String vs StringBuffer | 4.68× |
| Workload 3 | String vs StringBuilder | 18.42× |
| Workload 3 | String vs StringBuffer | 23.10× |

The results demonstrate that the performance advantage of mutable string classes becomes more significant as the workload increases.

## 9. INTERMEDIATE OBJECT CREATION AND MEMORY ANALYSIS

### 9.1 String

For repeated concatenation, each operation produces a new resulting String.

For a ticket containing n fields, approximately n resulting String objects are created through the repeated concatenation operations.

Previously created intermediate Strings become eligible for garbage collection when they are no longer referenced.

This results in additional object allocation and character copying.

### 9.2 StringBuilder

StringBuilder maintains mutable internal character storage.

It avoids creating a new resulting String for every append operation.

Its internal storage may be expanded when the available capacity is insufficient. The final call to `toString()` creates the resulting String.

### 9.3 StringBuffer

StringBuffer also maintains mutable internal character storage and avoids repeated resulting String creation.

However, its methods use synchronization, which can introduce additional execution overhead.

### 9.4 Memory Measurement Status

The current experiment measured execution time but did not collect actual memory measurements or JVM allocation counts.

Therefore, exact memory consumption and object allocation values are not reported as measured results.

The theoretical comparison is:

| Approach | Intermediate object creation | Memory behaviour |
|---|---|---|
| String | High | Repeated resulting String allocations |
| StringBuilder | Lower | Mutable buffer with possible resizing |
| StringBuffer | Lower | Mutable synchronized buffer with possible resizing |

For repeated concatenation of similarly sized fields, cumulative character allocation for String can grow quadratically with the number of fields.

StringBuilder and StringBuffer require approximately linear backing storage in the total number of characters, excluding temporary resizing allocations and the final String.

## 10. TIME AND SPACE COMPLEXITY ANALYSIS

Let n represent the number of fields in a ticket, assuming approximately equal field lengths.

| Approach | Time complexity | Space complexity |
|---|---|---|
| String with repeated + | O(n²) | O(n²) cumulative intermediate character allocation |
| StringBuilder | O(n) amortized | O(n) |
| StringBuffer | O(n) amortized | O(n) |

**Explanation:**

String:

Each concatenation copies the previously accumulated characters along with the new field. Therefore, repeated concatenation results in quadratic character-copying work.

StringBuilder:

Append operations use mutable storage. Although resizing may occasionally occur, the overall append operation has amortized linear complexity.

StringBuffer:

It follows the same general complexity as StringBuilder, but synchronization introduces additional constant-factor overhead.

The complexity analysis considers bounded field lengths. For varying field lengths, the total character count should be considered explicitly.

## 11. COMPARISON OF PREDICTED AND OBSERVED BEHAVIOUR

The experimental results largely supported the initial predictions.

In Workload 1, the difference between the three approaches was negligible because the workload was very small.

In Workload 2, StringBuilder performed approximately 5.03 times faster than String. StringBuffer also showed better performance than String.

In Workload 3, StringBuilder performed approximately 18.42 times faster than String. StringBuffer recorded the lowest execution time in this particular experiment.

The results confirmed that repeated String concatenation becomes expensive as the number of fields increases.

The slight performance difference between StringBuilder and StringBuffer indicates that synchronization overhead alone does not determine the observed execution time. JVM optimisations and measurement variations can also affect the results.

## 12. DESIGN DECISION

The railway ticket generator will operate inside a multithreaded booking server.

The recommended choices are:

**For individual ticket generation: StringBuilder**

StringBuilder is recommended because each ticket can be constructed using a local StringBuilder instance. It provides efficient text construction without unnecessary synchronization overhead.

**For a shared booking log: StringBuffer**

StringBuffer can be used when multiple threads share one buffer because its individual methods are synchronized.

However, if an entire log entry requires multiple append operations, the complete operation must also be protected to prevent interleaving. A dedicated logging framework or synchronized logging mechanism would be preferable in a production application.

## 13. ALTERNATIVE CONCEPTS

Other Java approaches can also be used to construct ticket information.

### 13.1 String.join()

String.join() combines multiple strings using a specified delimiter.

Example:

```java
String ticket = String.join("|",
    "PNR:12345",
    "Passenger:Surya",
    "Train:Chennai Express",
    "Coach:B2"
);
```

It is suitable when the fields are already available as strings.

### 13.2 StringJoiner

StringJoiner provides a convenient way to construct delimited strings with a prefix, delimiter and suffix.

Example:

```java
StringJoiner ticket = new StringJoiner("|");

ticket.add("PNR:12345");
ticket.add("Passenger:Surya");
ticket.add("Train:Chennai Express");

System.out.println(ticket);
```

It is useful when building structured text with consistent delimiters.

### 13.3 String.format()

String.format() can construct formatted text using placeholders.

Example:

```java
String ticket = String.format(
    "PNR: %s | Passenger: %s | Train: %s",
    "12345",
    "Surya",
    "Chennai Express"
);
```

It is useful for readable and structured formatting, although it can introduce additional formatting overhead.

### Comparison

| Approach | Main advantage | Limitation |
|---|---|---|
| String.join() | Simple delimiter-based joining | Requires existing string values |
| StringJoiner | Convenient structured joining | Less suitable for complex incremental formatting |
| String.format() | Readable formatting | Formatting overhead |
| StringBuilder | Efficient repeated appending | Requires explicit append operations |

For high-volume repeated ticket construction, StringBuilder remains the recommended general-purpose approach.

## 14. CONCLUSION

This experiment compared String, StringBuilder and StringBuffer for generating railway e-ticket information under three different workloads.

The experimental results demonstrated that repeated String concatenation requires significantly more execution time as the workload increases.

In the largest-field workload, String required 30.939 ms, whereas StringBuilder required only 1.680 ms. This represents an approximately 18.42-fold speedup.

StringBuilder is therefore recommended for constructing individual railway tickets because of its mutable internal storage and efficient append operations.

StringBuffer is a suitable option when synchronized access to a shared character buffer is required, although synchronization of complete logical operations must also be considered.

Overall, the experiment demonstrates that choosing an appropriate Java string-handling class can significantly improve application performance, particularly in systems that generate large volumes of text.

---

**FINAL ENGINEERING DECISION**

- Individual e-ticket generation: StringBuilder.
- Shared synchronized booking log: StringBuffer, with appropriate synchronization of complete log entries.
- Large-scale repeated concatenation: Avoid repeated String `+` operations inside loops.

**Experimental limitation:** Actual memory consumption and allocation counts have not yet been measured. They should be added as supplementary experimental evidence before final submission.
