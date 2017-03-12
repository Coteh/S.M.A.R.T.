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
public class MarkData {
    
    /** analysis function
     * takes in a spreadsheet file (which is passed off to MarkDataInput)
     * returns ordered ArrayList of Students in need (first element is highest priority)
     * @param file  student input spreadsheet
     * @return ordered ArrayList of Students in need
     */
    public static ArrayList analysis(String file){
        ArrayList<StudentInNeed> orderedStudents = new ArrayList();
        ArrayList<Course> courses = MarkDataInput.getCourses();
        ArrayList<Student> students = MarkDataInput.getStudents();
        
        
        return orderedStudents;
    }
    
    private double findStudentCourseSD(StudentCourse student, double mean) {
        double weightedMark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            weightedMark += Math.pow(student.getMarkAt(i)*student.getWeightAt(i) - mean, 2); // (x-mu)^2
        }
        return weightedMark/(i-1); // i-1 subject to change depending on Bessel's correction (suspect I should use it since incomplete data results in a statistic?)
    }
    
    private double findStudentCourseMean(StudentCourse student) {
        double weightedMark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            weightedMark += student.getMarkAt(i)*student.getWeightAt(i);
        }
        return weightedMark/i;
    }
}
