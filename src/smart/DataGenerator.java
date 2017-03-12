/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradesmart;
import java.lang.Math;
/**
 *
 * @author daniel
 */
public class DataGenerator {
    int[] wheightedList = new int[2000];
    int currentNumOfData;
    public DataGenerator(){    
        currentNumOfData = 0;
        for(int i = 0; i<1000; i= i + 1){
            wheightedList[i] = i;
        }
    }
    public void insertNewSetOfData(int dataToEnter, int deviation, int wheight){
        for(int i = currentNumOfData; i < wheight+currentNumOfData; i = i + 1 ){
            int positiveDeviation = 1;
            if(Math.random() < .5){
                positiveDeviation = -1;
            }
            wheightedList[i] = dataToEnter + (int)(Math.random()*deviation*positiveDeviation);
            
        }
        currentNumOfData = currentNumOfData + wheight;
        
    }
    
    public void printWheightedList(){
        for(int i =0; i< currentNumOfData; i = i + 1){
            System.out.println(wheightedList[i]);
        }
        System.out.println("job done");
    }
    

    public int getRandomInt(){
        int key = (int)(Math.random() *(currentNumOfData));
        int generatedNum = wheightedList[key];
        return generatedNum;
    }

    
}
