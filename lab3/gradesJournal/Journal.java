package lab3.gradesJournal;

import java.util.ArrayList;

public final class Journal {
    private final ArrayList<Group> groups;

    public Journal(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public synchronized void addGrade(Student student, int grade) {
        for (var group : groups) {
            if (!group.getStudents().contains(student)) continue;

            student.addGrade(grade);
        }
    }
}
