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
public class EmailGenerationSystem {
    private static final String helperEmail = "temp@example.ca";

    /** main
     *  method for running system
     * @param args  {input file address, data file address for students who have been already contacted}
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Main Error: not enough file directories");
            return;
        }
        ArrayList orderedStudents = MarkData.analysis(args[0]);
    }
    
    private void sendEmail(String recieverAddress, StudentInNeed student) {
        //unpack the Student in need and send the email with the students information
    }
}
