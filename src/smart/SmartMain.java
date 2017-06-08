/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class SmartMain {
    /** SmartMain
     *  main method for running system
     */
    public static void main(String[] args) {
        MarkDataInput md;
        
        if (args.length >= 1 && args[0].equals("test")) {
            md = new TestMarkDataInput();
        } else {
            md = new DBMarkDataInput();
        }
        
        ArrayList<StudentInNeed> orderedStudents = MarkData.analysis(md);
        
        HTMLOutput html = new HTMLOutput(orderedStudents);
        
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("results.html"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(SmartMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SmartMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
