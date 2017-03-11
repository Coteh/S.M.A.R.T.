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
public class StudentCourse {
    private String name;
    private final ArrayList<Double> marksList;
    private final ArrayList<Double> weightsList;
    
    public StudentCourse(String name) {
        this.marksList = new ArrayList<>();
        this.weightsList = new ArrayList<>();
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getMarkAt(int index) {
        if (index < 0 || index >= this.marksList.size()) {
            return 0.0;
        }
        return this.marksList.get(index);
    }
    
    public Double getWeightAt(int index) {
        if (index < 0 || index >= this.weightsList.size()) {
            return 0.0;
        }
        return this.weightsList.get(index);
    }
    
    public void addMark(Double mark, Double weight) {
        this.marksList.add(mark);
        this.weightsList.add(weight);
    }
}
