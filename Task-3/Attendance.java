package assignment;

import java.util.*;

class Student {
    private String regNo;
    private String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student s = (Student) o;
        return regNo.equals(s.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Attendance {

    public static void main(String[] args) {

        // TEST 1: Repeated scans of the same student
        System.out.println("TEST 1: Duplicate Scans");

        Set<Student> set1 = new HashSet<>();

        set1.add(new Student("192311001", "Arun"));
        set1.add(new Student("192311002", "Divya"));
        set1.add(new Student("192311001", "Arun"));
        set1.add(new Student("192311001", "Arun"));

        System.out.println("Unique students: " + set1.size());


        // TEST 2: Different registration numbers, same names
        System.out.println("\nTEST 2: Different Students");

        Set<Student> set2 = new HashSet<>();

        set2.add(new Student("192311003", "Rahul"));
        set2.add(new Student("192311004", "Rahul"));
        set2.add(new Student("192311005", "Priya"));

        System.out.println("Unique students: " + set2.size());


        // TEST 3: Changing registration number after insertion
        System.out.println("\nTEST 3: Mutable Registration Number");

        Set<Student> set3 = new HashSet<>();

        Student s1 = new Student("192311006", "Kiran");

        set3.add(s1);

        System.out.println("Before modification: " + set3.contains(s1));

        s1.setRegNo("192311007");

        System.out.println("After modification: " + set3.contains(s1));

        System.out.println("Set size: " + set3.size());


        // CORRECTED DESIGN: Immutable registration number
        System.out.println("\nTEST 4: Corrected HashSet Verification");

        Set<Student> set4 = new HashSet<>();

        set4.add(new Student("192311008", "Meena"));
        set4.add(new Student("192311009", "Ravi"));
        set4.add(new Student("192311008", "Meena"));

        System.out.println("Unique students: " + set4.size());


        // ALTERNATIVE 1: TreeSet
        System.out.println("\nTEST 5: TreeSet Alternative");

        Set<Student> treeSet = new TreeSet<>(
            Comparator.comparing(Student::getRegNo)
        );

        treeSet.add(new Student("192311010", "Asha"));
        treeSet.add(new Student("192311011", "John"));
        treeSet.add(new Student("192311010", "Asha"));

        System.out.println("Unique students: " + treeSet.size());
        System.out.println("Students: " + treeSet);


        // ALTERNATIVE 2: HashMap
        System.out.println("\nTEST 6: HashMap Alternative");

        Map<String, Student> attendance = new HashMap<>();

        attendance.put("192311012",
            new Student("192311012", "Kumar"));

        attendance.put("192311013",
            new Student("192311013", "Latha"));

        attendance.put("192311012",
            new Student("192311012", "Kumar"));

        System.out.println("Unique students: " + attendance.size());
        System.out.println("Attendance: " + attendance.values());
    }
}
