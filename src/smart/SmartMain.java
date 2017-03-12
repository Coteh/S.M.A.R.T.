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
public class SmartMain {
    /** main
     *  method for running system
     * @param args  {data file address for students who have been already contacted}
     */
    public static void main(String[] args) {
        ArrayList orderedStudents = MarkData.analysis();
    }
}
