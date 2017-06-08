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
    private static final double aFactor = 0.05;
    private static final double bFactor = 0.5;
    private static final double cFactor = 1.0;
    private static final double weightFactor = 0.15;
    private static final double defaultMark = 0.7;
    
    /** analysis function
     * takes in a spreadsheet file (which is passed off to MarkDataInput)
     * returns ordered ArrayList of Students in need (first element is highest priority)
     * @param 
     * @return ordered ArrayList of Students in need
     */
    public static ArrayList analysis(MarkDataInput dataInput){
        ArrayList<StudentInNeed> unorderedStudents = new ArrayList();
        ArrayList<Course> courses = dataInput.getCourses();
        ArrayList<Student> students = dataInput.getStudents();
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
                if (SCStatPair.length > 1 && classAverageSD != 0.0) {
                    classAverageSD = classAverageSD / (SCStatPair.length-1);
                    classAverageMean = classAverageMean / (SCStatPair.length-1);
                    mClassAverage = mClassAverage / (SCStatPair.length-1);
                    ClassAveragePartialPA += (classAverageMean - mClassAverage) / classAverageSD;
                } else {
                    ClassAveragePartialPA = defaultMark;
                }
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
                if (SCStatPair.length > 1) {
                    classAverageSD = classAverageSD / (SCStatPair.length-1);
                    if (classAverageSD != 0.0)
                        pb += StudentSDinCourse / classAverageSD;
                }
            }
            //System.err.println(pb);
            pb = pb / students.get(i).getCoursesList().size(); // (sum of SDs/SDca)/n
            
            for (int j=0; j<students.get(i).getCoursesList().size(); j++) {
                pc = findStudentCourseWeightedMean(students.get(i).getCourseAt(j));
            }
            pc = students.get(i).getCourseAverage() - (pc / students.get(i).getCoursesList().size()); // (sum of SDs/SDca)/n
            
            //System.out.println(students.get(i).getName() + ": " + pa + ", " + pb + ", " + pc);
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
        if (subAverageWeight <= 0) {
            return 0.0;
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
        int numOfNonZeroWeights = 0;
        double mark = 0.0;
        double weightSum = 0.0;
        double markDif;
        for (int i=0; i<student.numberOfMarks(); i++) {
            if (student.getWeightAt(i) != 0)
                numOfNonZeroWeights++;
            weightSum += student.getWeightAt(i);
            markDif = student.getMarkAt(i) - weightedMean;
            mark += student.getWeightAt(i) * markDif * markDif; // w*(x-mu)^2
        }
        if (numOfNonZeroWeights < 1) {
            return 0.0;
        }
        if (numOfNonZeroWeights == 1) { // if weightSum is zero then numofNonZeroWeights is zero
            return Math.sqrt(mark/weightSum);
        }
        return Math.sqrt((mark/(((double)(numOfNonZeroWeights-1))*weightSum))/(double)numOfNonZeroWeights);
    }      // 0.006454972243679025
    
    /** find student course mean
     * returns the mean of the marks in the course
     * @param student
     * @return mean
     */
    private static double findStudentCourseWeightedMean(StudentCourse student) {
        double mark = 0.0;
        double weightSum = 0.0;
        for (int i=0; i<student.numberOfMarks(); i++) {
            mark += student.getMarkAt(i)*student.getWeightAt(i);
            weightSum += student.getWeightAt(i);
        }
        if (weightSum <= 0.0) {
            return 0.0;
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
    
    public static void main(String[] args) {
        TestMarkDataInput md = new TestMarkDataInput();
        double[][] stuData = generateCourseWeightedStatPairs(md.getStudents(), md.getCourses(), md.courseBStr, 1);
        
        ArrayList<StudentInNeed> sin = MarkData.analysis(md);
        
        double mean = findStudentCourseMean(md.studentC);
        double weightedMean = findStudentCourseWeightedMean(md.studentC);
        System.out.println("Mean: " + mean + ", Weighted mean: " + weightedMean);
        System.out.println("SD: " + findStudentCourseSD(md.studentC, mean) + ", SWD: " + findStudentCourseWeightedSD(md.studentC, mean));
        System.out.println("m: " + mSolve(md.studentC));
        
        System.out.println(stuData[0][0] + ", " + stuData[0][1] + ", " + stuData[0][2]);
        System.out.println(stuData[1][0] + ", " + stuData[1][1] + ", " + stuData[1][2]);
        
        System.out.println(sin.get(0).getName() + " = " + sin.get(0).getPriority() + ", " + sin.get(1).getName() + " = " + sin.get(1).getPriority());
    }
}
