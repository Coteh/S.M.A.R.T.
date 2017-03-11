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
public class Student {
    private String id;
    private String email;
    private Double courseAverage;
    final private ArrayList<StudentCourse> coursesList;
    
    public Student() {
        this.coursesList = new ArrayList<>();
    }
    
    public String getID() {
        return this.id;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public Double getCourseAverage() {
        return this.courseAverage;
    }
    
    public StudentCourse getCourseAt(int index) {
        if (index < 0 || index >= this.coursesList.size()) {
            return null;
        }
        return this.coursesList.get(index);
    }
    
    public void setID(String id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setCourseAverage(Double courseAverage) {
        this.courseAverage = courseAverage;
    }
    
    public void addCourse(StudentCourse course) {
        this.coursesList.add(course);
    }
    
    public static void main(String[] args) {
        // Test student
        Student student = new Student();
        student.setID("1234567");
        student.setEmail("test@mail.uoguelph.ca");
        student.setCourseAverage(80.0);
        student.addCourse(new StudentCourse("CLAS 2000"));
        student.getCourseAt(0).addMark(70.0, 0.25);
        System.out.println(student.getID() + " " + student.getEmail() + " " + student.getCourseAverage()
                        + " " + student.getCourseAt(0).getName() + " " + student.getCourseAt(0).getWeightAt(0));
                
    }
}
