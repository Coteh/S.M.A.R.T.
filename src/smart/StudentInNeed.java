/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;


/**
 * Students in Need of Help
 * @author James
 */
public class StudentInNeed {
    private final String name;
    private final String id;
    private final String email;
    private final double priority;
    private final double pa; // a type priority
    private final double pb;
    private final double pc;
    
    public StudentInNeed(String name, String id, String email, double pa, double pb, double pc) throws Exception{
        if (name.isEmpty() || id.isEmpty())
            throw new Exception("SIN: empty value(s)");
        if (id.length() != 7)
            throw new Exception("SIN: wrong number of digits");
        
        this.name = name;
        this.id = id;
        this.email = email;
        this.pa = pa;
        this.pb = pb;
        this.pc = pc;
        this.priority = pa + pb + pc;
    }
    
    public String getName() {
        return name;
    }
    
    public String getID() {
        return id;
    }
    
    public String getEmail() {
        return email;
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
