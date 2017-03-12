/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.util.ArrayList;


/**
 * Students in Need of Help
 * @author James
 */
public class StudentInNeed extends Student {
    private final double priority;
    private final double pa; // a type priority
    private final double pb;
    private final double pc;
    
    public StudentInNeed(Student old, double pa, double pb, double pc) throws Exception {
        this(old.getName(), old.getID(), old.getEmail(), old.getCourseAverage(), old.getCoursesList(), pa, pb, pc);
    }
    
    public StudentInNeed(String name, String id, String email, double courseAverage, ArrayList<StudentCourse> coursesList, double pa, double pb, double pc) throws Exception{
        super(name, id, email, courseAverage, coursesList);
        
        this.pa = pa;
        this.pb = pb;
        this.pc = pc;
        this.priority = pa + pb + pc;
    }
    
    public double getPriority() {
        return priority;
    }
    
    public double getPriorityA() {
        return pa;
    }
    
    public double getPriorityB() {
        return pb;
    }
    
    public double getPriorityC() {
        return pc;
    }
}
