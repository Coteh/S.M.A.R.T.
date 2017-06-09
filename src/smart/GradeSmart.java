/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
/**
 *
 * @author daniel
 */
public class GradeSmart {
    int[] idNumbers = new int[200];
    ArrayList<CourseInfo> rawGradeData = new ArrayList<>();
    int[][] inCourse = new int[200][10];
    String fileLocation = "firstNames.txt";
    String[] nameArray = new String[200];
    
    /**
     * @param args the command line arguments
     */
    public GradeSmart(){
        for(int i = 0; i< 10; i = i + 1){
            CourseInfo newCourse = new CourseInfo("comp " + Integer.toString((i * 10) + 1000));
            newCourse.createWorkSheetData();
            rawGradeData.add(newCourse);
            //newCourse.printData(); 
        }
        for( int i = 0; i < 200; i = i + 1)   {
            for( int j = 0; j < 10; j = j + 1){
                inCourse[i][j] = 0;
            }
        }
        
    }
    public static void main(String[] args) {
        //CourseInfo rawGradeData = new CourseInfo();
        MySQLDatabase currentDatabase = new MySQLDatabase("jdbc:mysql://localhost:3306/smart?autoReconnect=true&useSSL=false", "root", "password");
        GradeSmart newBatch = new GradeSmart();
        newBatch.parseNames();
        newBatch.createCourseSelection();
        newBatch.generateStudentNumber();
        //newBatch.printIds();
        //newBatch.printInCourse();
        newBatch.moveToSql(currentDatabase);
        currentDatabase.closeConnection();
    }
    
    public void generateStudentNumber(){
        for(int i = 0; i < 200; i = i + 1){
           idNumbers[i] = (i * 1000) + (int)(Math.random() * 1000) + 1000000 ;
        }
    }
    
    public void createCourseSelection(){
        DataGenerator generateNewData = new DataGenerator();
        generateNewData.insertNewSetOfData(3, 3, 33);
        generateNewData.insertNewSetOfData(6, 2, 33);
        generateNewData.insertNewSetOfData(9, 2, 33);
        for(int i = 0; i < 200; i = i + 1){
            for(int j = 0; j < 5; j = j + 1){
                int  randomInt = generateNewData.getRandomInt();
                while(inCourse[i][randomInt - 1] == 1){
                    randomInt = generateNewData.getRandomInt();
                }
                 inCourse[i][randomInt - 1] = 1;
            }
            
        }
    }
    
    public void parseNames() {
        StringTokenizer numTokens = null;
        int i = 0;
        Scanner fileInput = null;
        try {
            //asks for file name and and creates file       
            fileInput = new Scanner(new FileInputStream(fileLocation));
            while (fileInput.hasNextLine() && i < 200) {
                numTokens = new StringTokenizer(fileInput.nextLine(), " ");
                if (!(numTokens.hasMoreTokens())) {
                    break;
                }
                nameArray[i] = numTokens.nextToken();
                //sorts through all the types
                i = i + 1;
            }
        } catch (FileNotFoundException f) {
            System.out.println("file not found");
            System.exit(0);
        }
    }
    
    public void moveToSql(MySQLDatabase sqlDatabase){
        int numOfCourses = 0;
        sqlDatabase.connectToDataBase();
        String variables = "studentId INT not NULL, courseId VARCHAR(11), mark DOUBLE, wheight DOUBLE, markTime INT";
        sqlDatabase.createTable("GRADES", variables);
        variables = "studentId INT not NULL, email VARCHAR(25), courseAvg DOUBLE, nameOfStudent VARCHAR(125)";
        sqlDatabase.createTable("STUDENTS", variables);
        Iterator<CourseInfo> i = rawGradeData.iterator();
        while(i.hasNext()){
            CourseInfo currentCourse = i.next();
            for(int j = 0; j < 200; j = j + 1){
                String studentVariable = Integer.toString(idNumbers[j]) + " " + nameArray[j] + " " + Double.toString( Math.random() * 100) +  " " + nameArray[j];
                if(inCourse[j][numOfCourses] == 1){
                    System.out.println("adding to dataBase");
                    currentCourse.moveToSql(sqlDatabase, idNumbers[j], j);
                }
            }
            numOfCourses = numOfCourses + 1;   
        }
        for(int j = 0; j < 200; j = j + 1){
            sqlDatabase.insertInTableStudent("STUDENTS",idNumbers[j],nameArray[j],( Math.random() * 100),nameArray[j] );
        }
        
    }
    
    public void printInCourse(){
        for(int i = 0; i<200; i = i + 1){
            for(int j = 0; j<10; j = j + 1){
                System.out.print(inCourse[i][j]);
            }
            System.out.println(" ");
        }
    }
    
    public void printIds(){
        for(int i = 0; i < 200; i = i + 1){
            System.out.println(idNumbers[i]);
        }
    }
    
    
    
}
