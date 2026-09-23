import java.util.Scanner;

public class CorEnManager {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Course code: "); String code = input.nextLine().trim();
        System.out.print("Course title: "); String title = input.nextLine().trim();
        int capacity = readPositiveInt(input, "Course capacity: ");
        Course course = new Course(code, title, capacity);

        int studentCount = readPositiveInt(input, "Number of Enrolled students: ");
        Student[] students = new Student[studentCount];
        for (int i = 0; i < students.length; i++) {
            System.out.print("Student ID: "); String id = input.nextLine().trim();
            System.out.print("Full name: "); String name = input.nextLine().trim();
            students[i] = new Student(id, name);
        }

        int attempts = readNonNegativeInt(input, "Enrollment attempts: ");
        for (int i = 0; i < attempts; i++) {
            System.out.print("Student ID to enroll: ");
            String id = input.nextLine().trim();
            Student student = findEnrolledStudent(students, id);
            if (student == null) System.out.println("Rejected: student ID not found.");
            else System.out.println(course.enroll(student) ? "Enrolled successfully." : "Rejected: duplicate enrollment or course is full.");
        }
        System.out.println();
        course.displayRoster();
        System.out.println("Occupied slots: " + course.getEnrolledCount());
        System.out.println("Remaining slots: " + (course.getCapacity() - course.getEnrolledCount()));
        input.close();
    }
    private static Student findEnrolledStudent(Student[] students, String id) {
        for (Student student : students) if (student.getId().equals(id)) return student;
        return null;
    }
    private static int readPositiveInt(Scanner s, String prompt) { int v; do { v = readNonNegativeInt(s, prompt); } while (v == 0); return v; }
    private static int readNonNegativeInt(Scanner s, String prompt) {
        System.out.print(prompt); while (!s.hasNextInt()) { s.nextLine(); System.out.print(prompt); }
        int value = s.nextInt(); s.nextLine(); return value < 0 ? readNonNegativeInt(s, prompt) : value;
    }
}

class Student {
    private String id;
    private String fullName;
    public Student(String id, String fullName) { this.id = id; this.fullName = fullName; }
    public String getId() { return id; }
    public String getFullName() { return fullName; }
}

class Course {
    private String code;
    private String title;
    private int capacity;
    private Student[] enrolledStudents;
    private int enrolledCount;
    public Course(String code, String title, int capacity) {
        this.code = code; this.title = title; this.capacity = capacity;
        this.enrolledStudents = new Student[capacity]; this.enrolledCount = 0;
    }
    public boolean enroll(Student student) {
        if (student == null || enrolledCount >= capacity || findStudent(student.getId()) != null) return false;
        enrolledStudents[enrolledCount++] = student; return true;
    }
    public Student findStudent(String id) {
        for (int i = 0; i < enrolledCount; i++) if (enrolledStudents[i].getId().equals(id)) return enrolledStudents[i];
        return null;
    }
    public void displayRoster() {
        System.out.println("ROSTER: " + code + " - " + title);
        if (enrolledCount == 0) System.out.println("No enrolled students.");
        for (int i = 0; i < enrolledCount; i++) System.out.println((i + 1) + ". " + enrolledStudents[i].getId() + " - " + enrolledStudents[i].getFullName());
    }
    public int getCapacity() { return capacity; }
    public int getEnrolledCount() { return enrolledCount; }
}

