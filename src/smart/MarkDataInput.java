/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class MarkDataInput {
    private static final String apiURL = "http://localhost:3000";
    
    public static ArrayList getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        
        HttpURLConnection connection = null;
        
        try {
            URL url = new URL(apiURL + "/students");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            
            InputStream response = connection.getInputStream();
            InputStreamReader resReader = new InputStreamReader(response);
            BufferedReader bufReader = new BufferedReader(resReader);
            String strRes = bufReader.readLine();
            while (strRes != null) {
                String[] strSplit = strRes.split(",");
                if (strSplit.length != 4) {
                    throw new Exception("Expected 4 but got " + strSplit.length + ".");
                }
                Student student = new Student(strSplit[3], strSplit[0], strSplit[1], Double.parseDouble(strSplit[2]));
                students.add(student);
                strRes = bufReader.readLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MarkDataInput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MarkDataInput.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return students;
    }
    
    public static ArrayList getCourses() {
        ArrayList<Course> courses = new ArrayList();
        
        HttpURLConnection connection = null;
        
        try {
            URL url = new URL(apiURL + "/courses");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            
            InputStream response = connection.getInputStream();
            InputStreamReader resReader = new InputStreamReader(response);
            BufferedReader bufReader = new BufferedReader(resReader);
            String strRes = bufReader.readLine();
            while (strRes != null) {
                Course course = new Course(strRes);
                courses.add(course);
                strRes = bufReader.readLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MarkDataInput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MarkDataInput.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return courses;
    }
    
    public static void main(String[] args) {
        ArrayList<Course> coursesList = getCourses();
        for (int i = 0; i < coursesList.size(); i++) {
            System.out.println(coursesList.get(i).getCourseID());
        }
        
        ArrayList<Student> studentsList = getStudents();
        for (int i = 0; i < studentsList.size(); i++) {
            System.out.println(studentsList.get(i).getName());
            System.out.println(studentsList.get(i).getID());
            System.out.println(studentsList.get(i).getEmail());
            System.out.println(studentsList.get(i).getCourseAverage());
        }
    }
}
