package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class WeekTask extends RecursiveAction {

    private static final int NUMBER_OF_WEEKS = 3;

    private static final int ASSISTANTS_COUNT = 3;

    private static final int TEACHERS_COUNT = 1;

    private final Journal journal;

    public WeekTask(Journal journal) {
        this.journal = journal;
    }

    private static void printJournal(Journal journal) {
        for (var group : journal.groups()) {
            System.out.printf("Group %s%n", group.groupName());
            for (var student : group.students()) {
                System.out.printf(
                        "Student %s, Grades: %s%n",
                        student.getName(),
                        String.join(", ", toStringArray(student.getGrades())));
            }

            System.out.println();
        }
    }

    private static String[] toStringArray(List<Integer> list) {
        var strArray = new String[list.size()];
        for (var i = 0; i < list.size(); i++) {
            strArray[i] = String.valueOf(list.get(i));
        }

        return strArray;
    }

    @Override
    protected void compute() {
        var startTime = System.currentTimeMillis();
        for (var i = 0; i < NUMBER_OF_WEEKS; i++) {
            //System.out.printf("Week %s%n%n", i + 1);

            var forks = new ArrayList<RecursiveAction>();
            for (var j = 0; j < TEACHERS_COUNT + ASSISTANTS_COUNT + 1; j++) {
                var task = new TeacherTask(journal);
                task.fork();
                forks.add(task);
            }

            for (var fork : forks) {
                fork.join();
            }

            //printJournal(journal);
        }

        var endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
