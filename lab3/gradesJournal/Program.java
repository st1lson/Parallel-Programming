package lab3.gradesJournal;

import java.util.ArrayList;

public class Program {
    private static final String[] NAMES = {
        "James",
        "Robert",
        "John",
        "Johnny",
        "Michael",
        "David",
        "William",
        "Richard",
        "Joseph",
        "Thomas"
    };

    private static final String[] GROUPS = { "IP-01", "IP-02", "IP-03" };

    private static final int ASSISTANTS_COUNT = 3;

    public static void main(String[] args) {
        var groups = new ArrayList<Group>();
        for (var group : GROUPS) {
            var students = new ArrayList<Student>();
            for (var name : NAMES) {
                students.add(new Student(name));
            }

            groups.add(new Group(group, students));
        }

        var journal = new Journal(groups);

        var teacher = new Thread(new Teacher(journal));
        var assistants = new ArrayList<Thread>();
        for (int i = 0; i < ASSISTANTS_COUNT; i++) {
            assistants.add(new Thread(new Teacher(journal)));
        }

        teacher.start();
        for (var assistant : assistants) {
            assistant.start();
        }

        try {
            teacher.join();
            for (var assistant : assistants) {
                assistant.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (var group : journal.getGroups()) {
            System.out.println(String.format("Group %s", group.getGroupName()));
            for (var student : group.getStudents()) {
                System.out.println(String.format(
                        "Student %s, Grades: %s",
                        student.getName(),
                        String.join(", ", toStringArray(student.getGrades()))));
            }
        }
    }

    private static String[] toStringArray(ArrayList<Integer> integers) {
        var strArray = new String[integers.size()];

        for (int i = 0; i < integers.size(); i++) {
            strArray[i] = String.valueOf(integers.get(i));
        }

        return strArray;
    }
}
