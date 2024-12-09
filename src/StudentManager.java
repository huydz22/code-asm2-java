import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Student {
    private String id;
    private String name;
    private double marks;

    public Student(String id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getRank() {
        if (marks >= 9.0) return "Excellent";
        if (marks >= 7.5) return "Very Good";
        if (marks >= 6.5) return "Good";
        if (marks >= 5.0) return "Medium";
        return "Fail";
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Marks: %.2f, Rank: %s", id, name, marks, getRank());
    }
}

public class StudentManager {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Stack<Student> historyStack = new Stack<>();
    private static Queue<Student> studentQueue = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Undo Last Action");
            System.out.println("5. Sort Students by Marks");
            System.out.println("6. Search Student by ID");
            System.out.println("7. Display All Students");
            System.out.println("8. Add Student to Queue");
            System.out.println("9. Remove Student from Queue");
            System.out.println("10. View Front Student in Queue");
            System.out.println("11. Display All Students in Queue");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> editStudent();
                case 3 -> deleteStudent();
                case 4 -> undoLastAction();
                case 5 -> sortStudents();
                case 6 -> searchStudent();
                case 7 -> displayStudents();
                case 8 -> addStudentToQueue();
                case 9 -> removeStudentFromQueue();
                case 10 -> viewFrontStudentInQueue();
                case 11 -> displayQueue();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Marks: ");
        double marks = Double.parseDouble(scanner.nextLine());

        Student student = new Student(id, name, marks);
        students.add(student);
        historyStack.push(student);
        System.out.println("Student added successfully.");
    }

    private static void editStudent() {
        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                historyStack.push(new Student(student.getId(), student.getName(), student.getMarks()));
                System.out.print("Enter new Student Name: ");
                student.setName(scanner.nextLine());
                System.out.print("Enter new Student Marks: ");
                student.setMarks(Double.parseDouble(scanner.nextLine()));
                System.out.println("Student updated successfully.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                historyStack.push(student);
                students.remove(student);
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    private static void undoLastAction() {
        if (!historyStack.isEmpty()) {
            Student lastAction = historyStack.pop();
            if (!students.contains(lastAction)) {
                students.add(lastAction);
                System.out.println("Undo successful: Restored " + lastAction);
            } else {
                students.remove(lastAction);
                System.out.println("Undo successful: Removed " + lastAction);
            }
        } else {
            System.out.println("No actions to undo.");
        }
    }

    private static void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to sort.");
            return;
        }
        students = mergeSort(students);
        System.out.println("Students sorted by marks.");
    }

    private static ArrayList<Student> mergeSort(ArrayList<Student> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<Student> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<Student> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static ArrayList<Student> merge(ArrayList<Student> left, ArrayList<Student> right) {
        ArrayList<Student> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getMarks() >= right.get(j).getMarks()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        return merged;
    }

    private static void searchStudent() {
        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                System.out.println(student);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }

    // Queue Methods
    private static void addStudentToQueue() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Marks: ");
        double marks = Double.parseDouble(scanner.nextLine());
        studentQueue.add(new Student(id, name, marks));
        System.out.println("Student added to queue.");
    }

    private static void removeStudentFromQueue() {
        if (!studentQueue.isEmpty()) {
            Student removedStudent = studentQueue.poll();
            System.out.println("Removed Student: " + removedStudent);
        } else {
            System.out.println("Queue is empty.");
        }
    }

    private static void viewFrontStudentInQueue() {
        if (!studentQueue.isEmpty()) {
            Student frontStudent = studentQueue.peek();
            System.out.println("Front Student: " + frontStudent);
        } else {
            System.out.println("Queue is empty.");
        }
    }

    private static void displayQueue() {
        if (!studentQueue.isEmpty()) {
            System.out.println("Students in the queue:");
            for (Student student : studentQueue) {
                System.out.println(student);
            }
        } else {
            System.out.println("Queue is empty.");
        }
    }
}
