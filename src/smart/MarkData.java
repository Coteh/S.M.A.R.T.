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
    private ArrayList<Integer> listOfMarks;
    private ArrayList<Integer> listOfClassMarks;
    private int aScore;
    private int bScore;
    
    public ArrayList<Integer> getListOfMarks() {
        return this.listOfMarks;
    }
    
    public ArrayList<Integer> getListOfClassMarks() {
        return this.listOfClassMarks;
    }
    
    public int getAScore() {
        return this.aScore;
    }
    
    public int getBScore() {
        return this.bScore;
    }
}
