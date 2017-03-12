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
public class GetExcel {
    int[][] integerArray = new int[200][5];
    String[] nameArray = new String[200];
    int[] idNumbers = new int[200];
    int[] averageGrade = new int[200];
    int[][] inCourse = new int[200][10];
    String fileLocation = "/media/daniel/dirtyDan/Guelph hacks/firstNames.txt";
    FileOutputStream currentFileStream;
    public GetExcel(){
        for( int i = 0; i < 200; i = i + 1)   {
            for( int j = 0; j < 10; j = j + 1){
                inCourse[i][j] = 0;
            }
        }
    }
    
    private void writeFile(){
        try{
            currentFileStream = new FileOutputStream(new File(fileLocation));
        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }
    
    private void openWorkSheet(){
        
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
    
    public void generateStudentNumber(){
        for(int i = 1; i < 200; i = i + 1){
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
                integerArray[i][j] = generateNewData.getRandomInt();
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
                    if (integerArray[i][p] > integerArray[j][p]) {
                        int swap = integerArray[i][p];
                        integerArray[i][p] = integerArray[j][p];
                        integerArray[j][p] = swap;
                    }
                }
            }
            
            for (int i = 0; i < 200; i = i + 2) {
               // System.out.println(integerArray[i][p]);
                int numToDisplay = integerArray[i][p] / 10;
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
    
    public void moveToSql(){
        
    }
    
    
    
}

