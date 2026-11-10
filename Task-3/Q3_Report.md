# Q3 – Engineer the hashCode–equals Contract

## 1. Objective

To investigate how the relationship between `equals()` and `hashCode()` affects duplicate detection in Java HashSet and to evaluate alternative data structures such as TreeSet and HashMap for attendance de-duplication.

## 2. Concept

The `equals()` method determines whether two objects are logically equal, whereas `hashCode()` generates an integer used by hash-based collections to locate objects.

The Java contract states that if two objects are equal according to `equals()`, they must return the same hash code. If this contract is violated, HashSet may store logically duplicate objects in different buckets.

## 3. Experimental Results

| Test | Description | Observed Result |
|---|---|---|
| Test 1 | Duplicate student scans | 2 unique students |
| Test 2 | Different registration numbers with identical names | 3 unique students |
| Test 3 | Modification of registration number after insertion | contains() returned false |
| Test 4 | Corrected HashSet implementation | 2 unique students |
| Test 5 | TreeSet alternative | 2 unique students |
| Test 6 | HashMap alternative | 2 unique students |

## 4. Analysis

### Test 1 – Duplicate Scans

The same student was scanned multiple times using the same registration number. The HashSet retained only one instance because both equals() and hashCode() were implemented using the registration number.

### Test 2 – Different Students

Three students were added, including two students with the same name but different registration numbers. All three were retained because registration number, rather than name, determines student identity.

### Test 3 – Mutable Registration Number

The registration number was modified after the student object was inserted into the HashSet. The contains() operation returned false because the object's hash code changed after insertion. The object remained in the set, but its new hash code could direct a lookup to a different bucket.

This demonstrates why fields used in equals() and hashCode() should remain immutable while objects are stored in hash-based collections.

### Test 4 – Corrected HashSet

The corrected implementation overrides hashCode() using regNo.hashCode(). The HashSet correctly identified duplicate students and retained only two unique entries.

### Test 5 – TreeSet Alternative

A TreeSet with a Comparator based on registration number was used. It maintained sorted order and rejected duplicate registration numbers because the comparator returned zero for matching registration numbers.

### Test 6 – HashMap Alternative

A HashMap using registration number as its key was implemented. When the same key was inserted again, the previous value was replaced. Therefore, only two unique student records remained.

## 5. Complexity Analysis

| Data Structure | Average insertion/search | Space Complexity |
|---|---|---|
| HashSet | O(1) | O(n) |
| TreeSet | O(log n) | O(n) |
| HashMap | O(1) | O(n) |

HashSet and HashMap offer average constant-time operations when hash distribution is good. TreeSet maintains sorted order using a balanced tree, with logarithmic insertion and lookup.

## 6. Corrected Implementation

The hashCode() method is implemented as:

```java
@Override
public int hashCode() {
    return regNo.hashCode();
}
```

This ensures that objects considered equal using regNo also produce the same hash code.

## 7. Conclusion

The experiment demonstrated the importance of maintaining the hashCode–equals contract when using hash-based collections. The initial implementation without a matching hashCode() could allow duplicate student records. The corrected implementation successfully performed attendance de-duplication.

The mutable registration number experiment also demonstrated that modifying fields used in hashCode() after insertion can make objects difficult to locate within a HashSet. Therefore, immutable identifiers are recommended.

HashSet is suitable for efficient attendance de-duplication, TreeSet is useful when sorted output is required, and HashMap is appropriate when student records need to be accessed directly using registration numbers.
