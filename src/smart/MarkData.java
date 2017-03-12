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
    public static void main(String[] args) {
        double[][] test = new double[5][2];
        System.out.println(test.length); // == 5
    }
    
    /** analysis function
     * takes in a spreadsheet file (which is passed off to MarkDataInput)
     * returns ordered ArrayList of Students in need (first element is highest priority)
     * @param file  student input spreadsheet
     * @return ordered ArrayList of Students in need
     */
    public static ArrayList analysis(String file){
        ArrayList<StudentInNeed> orderedStudents = new ArrayList();
        ArrayList<Course> courses = MarkDataInput.getCourses(file);
        ArrayList<Student> students = MarkDataInput.getStudents(file);
        int pa=0, pb=0, pc=0;
        for (int i=0; i<students.size(); i++) {
            //pa = ...
            
            for (int j=0; j<students.get(i).getCoursesList().size(); j++) {
                String courseName = students.get(i).getCourseAt(j).getName();
                int coursePosition = 0; // default 0 is problematic with imposing a found course
                for (int m=0; i<courses.size(); m++) {
                    if (courses.get(m).getCourseID().equals(courseName))
                        coursePosition = m;  // relative to courses
                }
                
                double[][] SCStatPair = generateCourseStatPairs(students, courses, courseName, coursePosition);
                String studentID = students.get(i).getID();
                
                int studentPosition = courses.get(coursePosition).getStudentIDPosition(studentID); // position in courses
                //since student position in courses is equivalent to student position in SCStatPair, then
                double StudentSDinCourse = SCStatPair[studentPosition][1];
                double classAverageSD = 0;
                for (int k=0; k<SCStatPair.length; k++) { // row lenght (SCStatPair.length == hultSize)
                    if (k != studentPosition) {
                        classAverageSD += SCStatPair[k][1];
                    }
                    classAverageSD = classAverageSD/(SCStatPair.length-1);
                }
                pb += StudentSDinCourse/classAverageSD;
            }
            pb = pb/students.get(i).getCoursesList().size(); // (sum of SDs/SDca)/n
            
            //pc...
            
            try {
            orderedStudents.add(new StudentInNeed(students.get(i), pa, pb, pc));
            } catch (Exception e) { // discard student
                System.err.println("analysis error: student " + students.get(i).getID() + " had to be discarded");
            }
        }
        
        return orderedStudents;
    }
    
    /**
     * 
     * @param students
     * @param courses
     * @param coursePosition  int position of subject course in courses
     * @param courseName  name of course
     * @return double[student relative to course][0,1 = mean, SD]
     */
    private static double[][] generateCourseStatPairs(ArrayList<Student> students, ArrayList<Course> courses, String courseName, int coursePosition) {
        int hultSize = courses.get(coursePosition).getEnrolledStudentIDs().length;
        int j=0;
        int studentCoursePos;
        double[][] statPairs = new double[hultSize][2];
        for (Student i : students) {
            if (j == hultSize) // hulting gaurd for statPairs
                break;
            studentCoursePos = i.isTakingCourse(courseName);  // assuming that the position of student i in Students is stable onto courses
            if (studentCoursePos >= 0) {
                StudentCourse studentCourse = i.getCourseAt(studentCoursePos);
                statPairs[j][0] = findStudentCourseMean(studentCourse);
                statPairs[j][1] = findStudentCourseSD(studentCourse, statPairs[j][0]);
                j++;
            }
        }
        return statPairs;
    }
    
    private static double findStudentCourseSD(StudentCourse student, double mean) {
        double mark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            mark += Math.pow(student.getMarkAt(i) - mean, 2); // (x-mu)^2
        }
        return Math.sqrt(mark/(i-1)); // i-1 subject to change depending on Bessel's correction (suspect I should use it since incomplete data results in a statistic?)
    }
    
    private static double findStudentCourseMean(StudentCourse student) {
        double mark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            mark += student.getMarkAt(i);
        }
        return mark/i;
    }
}
