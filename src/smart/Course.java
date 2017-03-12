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
    private String courseID;
    private final ArrayList<String> enrolledStudentIDsList;
    
    public Course(String courseID) {
        this.courseID = courseID;
        this.enrolledStudentIDsList = new ArrayList<>();
    }
    
    public String getCourseID() {
        return this.courseID;
    }
    
    public String[] getEnrolledStudentIDs() {
        return this.enrolledStudentIDsList.toArray(new String[0]);
    }
}
