/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.util.ArrayList;

/**
 *
 * @author James
 */
public class TestMarkDataInput implements MarkDataInput {
    private ArrayList<Course> allCoursesB;
    private ArrayList<Student> allStudentsB;
    public StudentCourse studentC;
    public String courseAStr = "Course A";
    public String courseBStr = "Course B";
    
    public TestMarkDataInput() {
        ArrayList<StudentCourse> enroled = new ArrayList<>();
        studentC = new StudentCourse(courseAStr);
        studentC.addMark(0.0, 0.02);
        studentC.addMark(0.7, 0.3);
        studentC.addMark(0.8, 0.2);
        studentC.addMark(0.3, 0.1);
        studentC.addMark(0.01, 0.02);
        enroled.add(studentC);
        studentC = new StudentCourse(courseBStr);
        studentC.addMark(0.6, 0.1);
        studentC.addMark(0.7, 0.3);
        studentC.addMark(0.8, 0.2);
        studentC.addMark(0.4, 0.1);
        studentC.addMark(0.85, 0.2);
        enroled.add(studentC);
        Student stu;
        try {
            stu = new Student("Stu", "0091125", "Stu@somewhere.com", 0.90, enroled);
        } catch (Exception e) {
            System.err.println("stu error");
            return;
        }
        ArrayList<Student> allStudents = new ArrayList<>();
        allStudents.add(stu);
        
        enroled = new ArrayList<>();
        studentC = new StudentCourse(courseBStr);
        studentC.addMark(0.7, 0.1);
        studentC.addMark(0.7, 0.3);
        studentC.addMark(0.7, 0.2);
        studentC.addMark(0.75, 0.1);
        studentC.addMark(0.65, 0.2);
        enroled.add(studentC);
        try {
            stu = new Student("Joe", "0087777", "Joe@somewhere.com", 0.60, enroled);
        } catch (Exception e) {
            System.err.println("stu error");
            return;
        }
        allStudents.add(stu);
        
        ArrayList<String> enNames = new ArrayList<>();
        enNames.add("Stu");
        ArrayList<Course> allCourses = new ArrayList<>();
        allCourses.add(new Course(courseAStr, enNames));
        enNames.add("Joe");
        allCourses.add(new Course(courseBStr, enNames));
        
        allCoursesB = allCourses;
        allStudentsB = allStudents;
    }
    
    public ArrayList getStudents() {
        return this.allStudentsB;
    }
    
    public ArrayList getCourses() {
        return this.allCoursesB;
    }
    
    public static void main(String[] args) {
        TestMarkDataInput input = new TestMarkDataInput();
        ArrayList<Course> coursesList = input.getCourses();
        for (int i = 0; i < coursesList.size(); i++) {
            System.out.println(coursesList.get(i).getCourseID());
        }
        
        ArrayList<Student> studentsList = input.getStudents();
        for (int i = 0; i < studentsList.size(); i++) {
            System.out.println(studentsList.get(i).getName());
            System.out.println(studentsList.get(i).getID());
            System.out.println(studentsList.get(i).getEmail());
            System.out.println(studentsList.get(i).getCourseAverage());
        }
    }
}
