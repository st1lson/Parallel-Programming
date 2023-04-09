package lab3.gradesJournal;

import java.util.ArrayList;

public class Student {
    private final String name;
    private final ArrayList<Integer> grades = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void addGrade(int grade) {
        if (grade <= 0 || grade > 100) return;

        grades.add(grade);
    }
}
