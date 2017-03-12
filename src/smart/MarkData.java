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
        ArrayList<Double> weights = new ArrayList<>();
        ArrayList<Double> marks = new ArrayList<>();
        StudentCourse student = new StudentCourse("Shit course");
        student.addMark(0.0, 0.02);
        student.addMark(0.7, 0.3);
        student.addMark(0.8, 0.2);
        student.addMark(0.3, 0.1);
        student.addMark(0.01, 0.02);
        
        double mean = findStudentCourseMean(student);
        double weightedMean = findStudentCourseWeightedMean(student);
        System.out.println("Mean: " + mean + ", Weighted mean: " + weightedMean);
        System.out.println("SD: " + findStudentCourseSD(student, mean) + ", SWD: " + findStudentCourseWeightedSD(student, mean));
        System.out.println("m: " + mSolve(student));
    }
    
    /** analysis function
     * takes in a spreadsheet file (which is passed off to MarkDataInput)
     * returns ordered ArrayList of Students in need (first element is highest priority)
     * @param 
     * @return ordered ArrayList of Students in need
     */
    public static ArrayList analysis(){
        ArrayList<StudentInNeed> unorderedStudents = new ArrayList();
        ArrayList<Course> courses = MarkDataInput.getCourses();
        ArrayList<Student> students = MarkDataInput.getStudents();
        double pa, pb=0.0, pc=0.0;
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
                
                double[][] SCStatPair = generateCourseWeightedStatPairs(students, courses, courseName, coursePosition);
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
                
                double[][] SCStatPair = generateCourseWeightedStatPairs(students, courses, courseName, coursePosition);
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
                pc = findStudentCourseWeightedMean(students.get(i).getCourseAt(j));
            }
            pc = students.get(i).getCourseAverage() - (pc / students.get(i).getCoursesList().size()); // (sum of SDs/SDca)/n
            
            
            try {
                unorderedStudents.add(new StudentInNeed(students.get(i), pa*aFactor, pb*bFactor, pc*cFactor));
            } catch (Exception e) { // discard student
                System.err.println("analysis error: student " + students.get(i).getID() + " had to be discarded");
            }
        }
        
        ArrayList<StudentInNeed> orderedS = new ArrayList<>();
        while (unorderedStudents.size() > 0) {
            int heighest = 0;
            for (int i=1; i<unorderedStudents.size(); i++) {
                if (unorderedStudents.get(heighest).getPriority() < unorderedStudents.get(i).getPriority())
                    heighest = i;
            }
            orderedS.add(unorderedStudents.get(heighest));
            unorderedStudents.remove(heighest);
        }
        return orderedS;
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
    
    private static double[][] generateCourseWeightedStatPairs(ArrayList<Student> students, ArrayList<Course> courses, String courseName, int coursePosition) {
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
                statPairs[j][0] = findStudentCourseWeightedMean(studentCourse);
                statPairs[j][1] = findStudentCourseWeightedSD(studentCourse, statPairs[j][0]);
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
    private static double findStudentCourseWeightedSD(StudentCourse student, double weightedMean) {
        int NumOfNonZeroWeights = 0;
        double mark = 0;
        double weightSum = 0;
        for (int i=0; i<student.numberOfMarks(); i++) {
            if (student.getWeightAt(i) != 0)
                NumOfNonZeroWeights++;
            weightSum += student.getWeightAt(i);
            mark += student.getWeightAt(i) * Math.pow(student.getMarkAt(i) - weightedMean, 2); // w*(x-mu)^2
        }
        return Math.sqrt((mark/((NumOfNonZeroWeights-1)*weightSum))/(double)NumOfNonZeroWeights);
    }
    
    /** find student course mean
     * returns the mean of the marks in the course
     * @param student
     * @return mean
     */
    private static double findStudentCourseWeightedMean(StudentCourse student) {
        double mark = 0;
        double weightSum = 0;
        for (int i=0; i<student.numberOfMarks(); i++) {
            mark += student.getMarkAt(i)*student.getWeightAt(i);
            weightSum += student.getWeightAt(i);
        }
        return mark/weightSum;
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
