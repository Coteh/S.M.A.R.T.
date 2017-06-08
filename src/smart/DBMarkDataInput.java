/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author James
 */
public class DBMarkDataInput implements MarkDataInput {
    private final ArrayList<Student> studentArray = new ArrayList<>();
    private final ArrayList<Course> courseArray = new ArrayList<>();
    private final MySQLDatabase currentDatabase = new MySQLDatabase("jdbc:mysql://localhost:3306/smart?autoReconnect=true&useSSL=false", "root", "password");
    
    public DBMarkDataInput() {
        this.currentDatabase.connectToDataBase();

        //Populate student array
        ResultSet generalResults = this.currentDatabase.retrieveFromTable("*", "STUDENTS");
        try {
            while (generalResults.next()) {
                String studentId = Integer.toString(generalResults.getInt("studentId"));
                String currentName = generalResults.getString("nameOfStudent");
                String email = generalResults.getString("email") + "@mail.uoguelph.ca";
                Double courseAvg = generalResults.getDouble("courseAvg");
                this.studentArray.add(new Student(currentName, studentId, email, courseAvg));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("an error in the data base occured");
        } catch (Exception t) {
            System.err.println(t);
        }

        //Populate course array
        generalResults = this.currentDatabase.retrieveFromTable("DISTINCT courseId", "GRADES");
        try {
            while (generalResults.next()) {
                String courseId = generalResults.getString("courseId");
                this.courseArray.add(new Course(courseId));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("an error in the data base occured");
        } catch (Exception t) {
            System.err.println(t);
        }

        Iterator<Student> i = this.studentArray.iterator();
//            System.out.println("job done");
        while (i.hasNext()) {
            String currentCourseId = " nothing";
            Student currentStudent = i.next();
            StudentCourse currentCourse = null;
//                System.out.println(currentStudent.getID() + " " + currentStudent.getEmail() + " " + currentStudent.getCourseAverage() + " " + currentStudent.getName());
            generalResults = this.currentDatabase.retrieveFromTable("*", "GRADES", "studentId = " + currentStudent.getID());
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
//                        System.out.println(courseId + " " + studentId + " " + mark + Double.toString(mark) + " " + Double.toString(wheight) + " " + Integer.toString(markTime));
                }
                currentStudent.addCourse(currentCourse);

            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.err.println("an error in the data base occured");
            }
        }
        i = this.studentArray.iterator();

        while (i.hasNext()) {
            String currentCourseId = " nothing";
            Student currentStudent = i.next();
            Course course = null;
            try {
                generalResults = this.currentDatabase.retrieveFromTable("DISTINCT studentId, courseId", "GRADES", "studentId = " + currentStudent.getID());
                while (generalResults.next()) {
                    String studentId = Integer.toString(generalResults.getInt("studentId"));
                    String courseId = generalResults.getString("courseId");
                    if (!courseId.equals(currentCourseId)) {
                        course = this.findCourseFromCourseArray(courseId);
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

//            for (int j = 0; j < instance.courseArray.size(); j++) {
//                System.out.println(instance.courseArray.get(j).getCourseID());
//                String[] studentIDs = instance.courseArray.get(j).getEnrolledStudentIDs();
//                for (int k = 0; k < studentIDs.length; k++) {
//                    System.out.println("-> " + studentIDs[k]);
//                }
//            }
    }
    
    private Course findCourseFromCourseArray(String courseId) {
        for (int i = 0; i < this.courseArray.size(); i++) {
            if (this.courseArray.get(i).getCourseID().equals(courseId)) {
                return this.courseArray.get(i);
            }
        }
        
        return null;
    }
    
    public ArrayList getStudents() {
        return this.studentArray;
    }
    
    public ArrayList getCourses() {
        return this.courseArray;
    }
    
    public static void main(String[] args) {
        MarkDataInput input = new DBMarkDataInput();
        ArrayList<Course> coursesList = input.getCourses();
        for (int i = 0; i < coursesList.size(); i++) {
            System.out.println(coursesList.get(i).getCourseID());
        }
        
        ArrayList<Student> studentsList = input.getStudents();
        for (int i = 0; i < studentsList.size(); i++) {
            System.out.println(studentsList.get(i).getName());
            System.out.println(studentsList.get(i).getID());
            System.out.println(studentsList.get(i).getEmail());
            System.out.println(studentsList.get(i).getCourseAverage());
        }
    }
}
