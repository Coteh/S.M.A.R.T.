/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradesmart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author daniel
 */
public class CourseInfo {
    int[][] gradeArray = new int[200][5];
    int[] averageGrade = new int[200];
    String nameOfCourse;
    FileOutputStream currentFileStream;
    public CourseInfo(String newNameOfCourse){
        
        nameOfCourse = newNameOfCourse;
    }
   
    
    private void openWorkSheet(){
        
    }
    

    public void createWorkSheetData(){
        for(int j = 0; j < 5; j = j + 1){
            DataGenerator generateNewData = new DataGenerator();
            generateNewData.insertNewSetOfData(10, 5, 10);
            generateNewData.insertNewSetOfData(25, 5, 20);
            generateNewData.insertNewSetOfData(35, 5, 70);  
            generateNewData.insertNewSetOfData(45, 5, 100);
            generateNewData.insertNewSetOfData(50, 5, 150);
            generateNewData.insertNewSetOfData(65, 5, 100);
            generateNewData.insertNewSetOfData(75, 5, 200);
            generateNewData.insertNewSetOfData(85, 5, 140);
            generateNewData.insertNewSetOfData(95, 5, 60);
            for(int i = 0; i < 200; i = i + 1){
                gradeArray[i][j] = generateNewData.getRandomInt();
            }
        }
        
    }
    public void generateAverage(){
         DataGenerator generateNewData = new DataGenerator();
         generateNewData.insertNewSetOfData(10, 0, 750);
         generateNewData.insertNewSetOfData(10, 30, 250);
    }
    
    public void printData(){
        for(int p = 0; p < 5; p = p + 1){
            int[] averageInteger = new int[10];
            for (int i = 0; i < 10; i = i + 1) {
                averageInteger[i] = 0;
            }
            for (int i = 0; i < 200; i = i + 1) {
                for (int j = i; j < 200; j = j + 1) {
                    if (gradeArray[i][p] > gradeArray[j][p]) {
                        int swap = gradeArray[i][p];
                        gradeArray[i][p] = gradeArray[j][p];
                        gradeArray[j][p] = swap;
                    }
                }
            }
            
            for (int i = 0; i < 200; i = i + 2) {
                System.out.println(gradeArray[i][p]);
                int numToDisplay = gradeArray[i][p] / 10;
                averageInteger[numToDisplay] = averageInteger[numToDisplay] + 1;
            }
            /*System.out.println(nameArray[0]);
            for (int i = 0; i < 200; i = i + 1) {
                System.out.println(nameArray[i]);
            }*/
            System.out.println("printing out average");
            for (int i = 0; i < 10; i = i + 1) {
                System.out.print(averageInteger[i]);
                System.out.print(" ");
                System.out.println(i + 1);
            }
            /*for (int i = 0; i < 200; i = i + 1) {
                System.out.println(idNumbers[i]);
            }
            for(int i = 0; i < 200; i = i + 1){
                for(int j = 0; j < 10; j = j + 1){
                    System.out.print(inCourse[i][j]);
                    System.out.print(" ");
                }
                System.out.println("");
            }*/
        }
        
    }
    
    public void moveToSql(MySQLDatabase sqlDatabase, int studentId, int currentStudent){
        int[] wheights = new int[5];
        wheights[0] = 5;
        wheights[1] = 10;
        wheights[2] = 15;
        wheights[3] = 20;
        wheights[4] = 50;

        for(int j = 0; j < 5; j = j + 1){
            String variables = Integer.toString(studentId) + " " + nameOfCourse + " " + Integer.toString(gradeArray[currentStudent][j])+ " " + Integer.toString(wheights[j]);
            sqlDatabase.insertInTable("grades", variables);
        }
                
        
    }
    
    
    
}

