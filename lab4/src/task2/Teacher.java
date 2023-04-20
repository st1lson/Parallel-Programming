package task2;

import java.util.Random;

public class Teacher implements Runnable {

    private final Journal journal;

    public Teacher(Journal journal) {
        this.journal = journal;
    }

    @Override
    public void run() {
        var random = new Random();
        for (var group : journal.groups()) {
            for (var student : group.students()) {
                var grade = random.nextInt(101);
                journal.addGrade(student, grade);
            }
        }
    }
}
