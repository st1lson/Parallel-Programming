package lab3.gradesJournal;

import java.util.ArrayList;
import java.util.List;

public class Week implements Runnable {

    private static final int NUMBER_OF_WEEKS = 3;

    private static final int ASSISTANTS_COUNT = 3;

    private final Journal journal;

    public Week(Journal journal) {
        this.journal = journal;
    }

    @Override
    public void run() {
        var teacher = new Teacher(journal);
        var assistants = new ArrayList<Teacher>();
        for (var i = 0; i < ASSISTANTS_COUNT; i++) {
            assistants.add(new Teacher(journal));
        }

        for (var i = 0; i < NUMBER_OF_WEEKS; i++) {
            System.out.println(String.format("Week %s%n", i + 1));

            var teacherThread = new Thread(teacher);
            var assistantsThreads = new ArrayList<Thread>();
            for (var j = 0; j < ASSISTANTS_COUNT; j++) {
                assistantsThreads.add(new Thread(assistants.get(j)));
            }

            teacherThread.start();
            for (var assistant : assistantsThreads) {
                assistant.start();
            }
    
            try {
                teacherThread.join();
                for (var assistant : assistantsThreads) {
                    assistant.join();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            printJournal(journal);
        }
    }

    private static void printJournal(Journal journal) {
        for (var group : journal.getGroups()) {
            System.out.println(String.format("Group %s", group.getGroupName()));
            for (var student : group.getStudents()) {
                System.out.println(String.format(
                        "Student %s, Grades: %s",
                        student.getName(),
                        String.join(", ", toStringArray(student.getGrades())))
                    );
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
}
