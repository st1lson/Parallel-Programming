package task2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class TeacherTask extends RecursiveAction {

    private static final int GROUPS_PER_TASK = 5;

    private final Journal journal;

    private final int firstGroup;
    private final int finalGroup;

    public TeacherTask(Journal journal) {
        this.journal = journal;
        this.firstGroup = 0;
        this.finalGroup = journal.getNumberOfGroup() - 1;
    }

    private TeacherTask(Journal journal, int firstGroup, int finalGroup) {
        this.journal = journal;
        this.firstGroup = firstGroup;
        this.finalGroup = finalGroup;
    }

    private int getOffest() {
        return this.finalGroup - this.firstGroup;
    }

    @Override
    protected void compute() {
        var forks = new ArrayList<RecursiveAction>();

        if (getOffest() <= GROUPS_PER_TASK) {
            var random = new Random();
            for (int i = firstGroup; i <= finalGroup; i++) {
                for (var student : journal.groups().get(i).students()) {
                    var grade = random.nextInt(101);
                    journal.addGrade(student, grade);
                }
            }

            return;
        } else {
            var firstTask = new TeacherTask(journal, firstGroup, (firstGroup + finalGroup) / 2);
            var secondTask = new TeacherTask(journal, (firstGroup + finalGroup) / 2 + 1, finalGroup);

            firstTask.fork();
            secondTask.fork();

            forks.add(firstTask);
            forks.add(secondTask);
        }

        for (var fork : forks) {
            fork.join();
        }
    }
}
