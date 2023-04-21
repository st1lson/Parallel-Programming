package lab3.gradesJournal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
        var studentNames = Arrays.stream(generateArray(NAMES, 1000)).toList();
        var groupNames = Arrays.stream(generateArray(GROUPS, 1000)).toList();

        var groups = new ArrayList<Group>();
        for (var group : groupNames) {
            var students = new ArrayList<Student>();
            for (var name : studentNames) {
                students.add(new Student(name));
            }

            groups.add(new Group(group, students));
        }
        var journal = new Journal(groups);

        var weekThread = new Thread(new Week(journal));

        weekThread.start();
    }

    private static String[] generateArray(String[] source, int size) {
        var random = new Random();

        var sourceLength = source.length;

        var newArray = new String[size];
        for (int i = 0; i < size; i++) {
            newArray[i] = source[random.nextInt(sourceLength)];
        }

        return newArray;
    }
}
