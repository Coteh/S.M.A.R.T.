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
    private static final double aFactor = 1.0;
    private static final double bFactor = 1.0;
    private static final double cFactor = 1.0;
    private static final double weightFactor = 0.15;
    
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
        double pa=0.0, pb=0.0, pc=0.0;
        for (int i=0; i<students.size(); i++) {
            double ClassAveragePartialPA = 0.0;
            double StudentPartialPA = 0.0;
            for (int j=0; j<students.get(i).getCoursesList().size(); j++) {
                String courseName = students.get(i).getCourseAt(j).getName();
                int coursePosition = 0; // default 0 is problematic with imposing a found course
                for (int k=0; i<courses.size(); k++) {
                    if (courses.get(k).getCourseID().equals(courseName)) {
                        coursePosition = k;  // relative to courses
                        break;
                    }
                }
                
                double[][] SCStatPair = generateCourseStatPairs(students, courses, courseName, coursePosition);
                String studentID = students.get(i).getID();
                
                int studentPosition = courses.get(coursePosition).getStudentIDPosition(studentID); // position in courses
                //since student position in courses is equivalent to student position in SCStatPair, then
                double mStudent = mSolve(students.get(i).getCourseAt(j));
                StudentPartialPA += (SCStatPair[studentPosition][0] - SCStatPair[studentPosition][2]) / SCStatPair[studentPosition][1];
                
                double classAverageSD = 0.0;
                double classAverageMean = 0.0;
                double mClassAverage = 0.0;
                for (int k=0; k<SCStatPair.length; k++) { // row lenght (SCStatPair.length == hultSize)
                    if (k != studentPosition) {
                        classAverageMean += SCStatPair[k][0];
                        classAverageSD += SCStatPair[k][1];
                        mClassAverage += SCStatPair[k][2];
                    }
                }
                classAverageSD = classAverageSD / (SCStatPair.length-1);
                classAverageMean = classAverageMean / (SCStatPair.length-1);
                mClassAverage = mClassAverage / (SCStatPair.length-1);
                ClassAveragePartialPA += (classAverageMean - mClassAverage) / classAverageSD;
            }
            pa = (StudentPartialPA * ClassAveragePartialPA) / students.get(i).getCoursesList().size(); // (sum of SDs/SDca)/n
            
            for (int j=0; j<students.get(i).getCoursesList().size(); j++) {
                String courseName = students.get(i).getCourseAt(j).getName();
                int coursePosition = 0; // default 0 is problematic with imposing a found course
                for (int k=0; i<courses.size(); k++) {
                    if (courses.get(k).getCourseID().equals(courseName)) {
                        coursePosition = k;  // relative to courses
                        break;
                    }
                }
                
                double[][] SCStatPair = generateCourseStatPairs(students, courses, courseName, coursePosition);
                String studentID = students.get(i).getID();
                
                int studentPosition = courses.get(coursePosition).getStudentIDPosition(studentID); // position in courses
                //since student position in courses is equivalent to student position in SCStatPair, then
                double StudentSDinCourse = SCStatPair[studentPosition][1];
                double classAverageSD = 0.0;
                for (int k=0; k<SCStatPair.length; k++) { // row lenght (SCStatPair.length == hultSize)
                    if (k != studentPosition) {
                        classAverageSD += SCStatPair[k][1];
                    }
                }
                classAverageSD = classAverageSD / (SCStatPair.length-1);
                pb += StudentSDinCourse / classAverageSD;
            }
            pb = pb / students.get(i).getCoursesList().size(); // (sum of SDs/SDca)/n
            
            for (int j=0; j<students.get(i).getCoursesList().size(); j++) {
                pc = findStudentCourseMean(students.get(i).getCourseAt(j));
            }
            pc = students.get(i).getCourseAverage() - (pc / students.get(i).getCoursesList().size()); // (sum of SDs/SDca)/n
            
            
            try {
                orderedStudents.add(new StudentInNeed(students.get(i), pa*aFactor, pb*bFactor, pc*cFactor));
            } catch (Exception e) { // discard student
                System.err.println("analysis error: student " + students.get(i).getID() + " had to be discarded");
            }
        }
        
        //orderedStudents are unordered and need to be sorted
        return orderedStudents;
    }
    
    private static double mSolve(StudentCourse student) {
        double subAverageWeightedMark = 0.0;
        double subAverageWeight = 0.0;
        if (student.numberOfMarks() <= 0)
            return 0.75;
        for (int i=student.numberOfMarks()-1; i>=0; i--) {
            subAverageWeightedMark += student.getMarkAt(i)*student.getWeightAt(i);
            subAverageWeight += student.getWeightAt(i);
            if (subAverageWeight >= weightFactor)
                break;
        }
        return subAverageWeightedMark/subAverageWeight; // normalize output
    }
    
    /**
     * 
     * @param students
     * @param courses
     * @param coursePosition  int position of subject course in courses
     * @param courseName  name of course
     * @return double[student relative to course][0,1,2 = mean, SD, m]
     */
    private static double[][] generateCourseStatPairs(ArrayList<Student> students, ArrayList<Course> courses, String courseName, int coursePosition) {
        int hultSize = courses.get(coursePosition).getEnrolledStudentIDs().length;
        int j=0;
        int studentCoursePos;
        double[][] statPairs = new double[hultSize][3];
        for (Student i : students) {
            if (j == hultSize) // hulting gaurd for statPairs
                break;
            studentCoursePos = i.isTakingCourse(courseName);  // assuming that the position of student i in Students is stable onto courses
            if (studentCoursePos >= 0) {
                StudentCourse studentCourse = i.getCourseAt(studentCoursePos);
                statPairs[j][0] = findStudentCourseMean(studentCourse);
                statPairs[j][1] = findStudentCourseSD(studentCourse, statPairs[j][0]);
                statPairs[j][2] = mSolve(studentCourse);
                j++;
            }
        }
        return statPairs;
    }
    
    /** find student course standard deviation
     * consider returning weighted standard deviation of the course
     * @param student
     * @param mean
     * @return standard deviation
     */
    private static double findStudentCourseSD(StudentCourse student, double mean) {
        double mark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            mark += Math.pow(student.getMarkAt(i) - mean, 2); // (x-mu)^2
        }
        return Math.sqrt(mark/(i-1)); // i-1 subject to change depending on Bessel's correction (suspect I should use it since incomplete data results in a statistic?)
    }
    
    /** find student course mean
     * returns the mean of the marks in the course
     * @param student
     * @return mean
     */
    private static double findStudentCourseMean(StudentCourse student) {
        double mark = 0;
        int i;
        for (i=0; i<student.numberOfMarks(); i++) {
            mark += student.getMarkAt(i);
        }
        return mark/i;
    }
}
