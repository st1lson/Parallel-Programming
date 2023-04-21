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

    public void addGrade(Student student, int grade) {
        student.addGrade(grade);
    }
}
