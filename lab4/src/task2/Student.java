package task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student {
    private final String name;
    private final List<Integer> grades = Collections.synchronizedList(new ArrayList<>());

    public Student(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void addGrade(int grade) {
        if (grade <= 0 || grade > 100) return;

        grades.add(grade);
    }
}
