/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class Student {

    private String name;
    private String id;
    private String email;
    private double courseAverage;
    final private ArrayList<StudentCourse> coursesList;

    public Student(String name, String id, String email, double courseAverage, ArrayList<StudentCourse> coursesList) throws Exception {
        if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
            throw new Exception("Student: empty value(s)");
        }
        if (id.length() != 7) {
            throw new Exception("Student: wrong number of digits");
        }
        if (coursesList == null) {
            throw new Exception("Student: coursesList does not exist");
        }

        this.name = name;
        this.id = id;
        this.email = email;
        this.courseAverage = courseAverage;
        this.coursesList = coursesList;
    }

    public Student(String name, String id, String email, double courseAverage) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new Exception("Student: empty value(s)");
        }
        if (id == null || id.length() != 7) {
            throw new Exception("Student: wrong number of digits");
        }
        if (email == null || email.isEmpty()) {
            throw new Exception("Student: coursesList does not exist");
        }
        this.name = name;
        this.id = id;
        this.email = email;
        this.courseAverage = courseAverage;
        this.coursesList = new ArrayList<>();
    }

    public String getName() {
        return name;
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

    public int isTakingCourse(String courseName) {
        for (int i = 0; i < coursesList.size(); i++) {
            if (coursesList.get(i).getName().equals(courseName)) {
                return i;
            }
        }
        return -1;
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

    public ArrayList getCoursesList() {
        return coursesList;
    }

    public void addCourse(StudentCourse course) {
        this.coursesList.add(course);
    }

    public static void main(String[] args) {
        // Test student
        ArrayList<Student> studentArray = new ArrayList<>();
        ArrayList<Course> courseArray = new ArrayList<>();
        MySQLDatabase currentDatabase = new MySQLDatabase("jdbc:mysql://localhost:3306/smart?autoReconnect=true&useSSL=false", "root", "password");
        currentDatabase.connectToDataBase();
        
        
        
        
    }

}
