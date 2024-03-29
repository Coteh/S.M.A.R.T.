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
public class Course {
    private String courseID;  // course name not ID
    private final ArrayList<String> enrolledStudentIDsList;  // student ID not student Name
    
    public Course(String courseID) {
        this.courseID = courseID;
        this.enrolledStudentIDsList = new ArrayList<>();
    }
    
    public Course(String courseID, ArrayList<String> enrolled) {
        this.courseID = courseID;
        this.enrolledStudentIDsList = enrolled;
    }
    
    public String getCourseID() {
        return this.courseID;
    }
    
    public String[] getEnrolledStudentIDs() {
        return this.enrolledStudentIDsList.toArray(new String[0]);
    }
    
    public int getStudentIDPosition(String studentID) {
        for (int i=0; i<enrolledStudentIDsList.size(); i++) {
            return i;
        }
        return -1;
    }
    
    public void addEnrolledStudentID(String enrolledStudentID) {
        this.enrolledStudentIDsList.add(enrolledStudentID);
    }
}
