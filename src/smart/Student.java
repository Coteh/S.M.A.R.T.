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
    
    private static Course findCourseFromCourseArray(ArrayList<Course> courseList, String courseId) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getCourseID().equals(courseId)) {
                return courseList.get(i);
            }
        }
        
        return null;
    }

    public static void main(String[] args) {
        // Test student
        Student student;
        ArrayList<Student> studentArray = new ArrayList<>();
        ArrayList<Course> courseArray = new ArrayList<>();
        MySQLDatabase currentDatabase = new MySQLDatabase("jdbc:mysql://localhost:3306/smart?autoReconnect=true&useSSL=false", "root", "password");
        currentDatabase.connectToDataBase();
        
        //Populate student array
        ResultSet generalResults = currentDatabase.retrieveFromTable("*", "STUDENTS");
        try {
            while (generalResults.next()) {
                String studentId = Integer.toString(generalResults.getInt("studentId"));
                String currentName = generalResults.getString("nameOfStudent");
                String email = generalResults.getString("email") + "@mail.uoguelph.ca";
                Double courseAvg = generalResults.getDouble("courseAvg");
                studentArray.add(new Student(currentName, studentId, email, courseAvg));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("an error in the data base occured");
        } catch (Exception t) {
            System.err.println(t);
        }
        
        //Populate course array
        generalResults = currentDatabase.retrieveFromTable("DISTINCT courseId", "GRADES");
        try {
            while (generalResults.next()) {
                String courseId = generalResults.getString("courseId");
                courseArray.add(new Course(courseId));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("an error in the data base occured");
        } catch (Exception t) {
            System.err.println(t);
        }
        
        Iterator<Student> i = studentArray.iterator();
        System.out.println("job done");
        while (i.hasNext()) {
            String currentCourseId = " nothing";
            Student currentStudent = i.next();
            StudentCourse currentCourse = null;
            System.out.println(currentStudent.getID() + " " + currentStudent.getEmail() + " " + currentStudent.getCourseAverage() + " " + currentStudent.getName());
            generalResults = currentDatabase.retrieveFromTable("*", "GRADES", "studentId = " + currentStudent.getID());
            try {
                while (generalResults.next()) {
                    String studentId = Integer.toString(generalResults.getInt("studentId"));
                    String courseId = generalResults.getString("courseId");
                    double mark = generalResults.getDouble("mark");
                    double wheight = generalResults.getDouble("wheight");
                    int markTime = generalResults.getInt("markTime");
                    if (!courseId.equals(currentCourseId)) {
                        currentCourse = new StudentCourse();
                        currentCourse.setName(courseId);
                        currentStudent.addCourse(currentCourse);
                        currentCourseId = courseId;
                    }
                    if (currentCourse != null) {
                        currentCourse.addMark(mark, wheight);
                    }
                    System.out.println(courseId + " " + studentId + " " + mark + Double.toString(mark) + " " + Double.toString(wheight) + " " + Integer.toString(markTime));
                }
                currentStudent.addCourse(currentCourse);

            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.err.println("an error in the data base occured");
            }
        }
        i = studentArray.iterator();
        
        while (i.hasNext()) {
            String currentCourseId = " nothing";
            Student currentStudent = i.next();
            Course course = null;
            try {
                generalResults = currentDatabase.retrieveFromTable("DISTINCT studentId, courseId", "GRADES", "studentId = " + currentStudent.getID());
                while (generalResults.next()) {
                    String studentId = Integer.toString(generalResults.getInt("studentId"));
                    String courseId = generalResults.getString("courseId");
                    if (!courseId.equals(currentCourseId)) {
                        course = findCourseFromCourseArray(courseArray, courseId);
                        currentCourseId = courseId;
                    }
                    if (course != null) {
                        course.addEnrolledStudentID(studentId);
                    }
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.err.println("an error in the data base occured");
            } catch (Exception t) {
                System.err.println(t);
            }
        }
        
        for (int j = 0; j < courseArray.size(); j++) {
            System.out.println(courseArray.get(j).getCourseID());
            String[] studentIDs = courseArray.get(j).getEnrolledStudentIDs();
            for (int k = 0; k < studentIDs.length; k++) {
                System.out.println("-> " + studentIDs[k]);
            }
        }
    }

}
