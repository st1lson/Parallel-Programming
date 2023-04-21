package task2;

import java.util.ArrayList;

public record Journal(ArrayList<Group> groups) {

    public int getNumberOfGroup() {
        return this.groups.size();
    }

    public void addGrade(Student student, int grade) {
        student.addGrade(grade);
    }
}
