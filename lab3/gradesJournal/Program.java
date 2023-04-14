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

        var weekThread = new Thread(new Week(journal));

        weekThread.start();
    }
}
