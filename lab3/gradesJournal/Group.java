package lab3.gradesJournal;

import java.util.ArrayList;

public class Group {
    private final String groupName;

    private final ArrayList<Student> students;

    public Group(String groupName, ArrayList<Student> students) {
        this.groupName = groupName;
        this.students = students;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
