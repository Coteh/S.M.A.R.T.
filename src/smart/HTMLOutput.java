/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class HTMLOutput {
    public HTMLOutput(ArrayList<StudentInNeed> studentsInNeed) {
        String html;
        
        int limit = (studentsInNeed.size() < 5) ? studentsInNeed.size() : 5;
        
        PrintWriter writer;
        try {
            writer = new PrintWriter("results.html", "UTF-8");
            writer.println("<!DOCTYPE html><html>\n");
            writer.println("    <head>\n");
            writer.println("        <meta charset=\"utf-8\">\n");
            writer.println("        <title>Generated Priorities</title>\n");
            writer.println("\n");
            writer.println("    </head>\n");
            writer.println("    <body>");
            writer.println("    <h3>Students that are most in need (Sample Results)</h3>");
            for (int i = 0; i < limit; i++) {
                StudentInNeed student = studentsInNeed.get(i);
                writer.println("<li>" + student.getName() + " (" + student.getID() + ") (" + student.getEmail() + ") " + student.getCourseAverage().toString());
            }
            writer.println("\n\n\n\n\n\n\n");
            writer.println("<h4>Mini Sample Results</h4>\n");
            writer.println("<h5>Priority Values</h5>");
            writer.println("<span>Stu = -12.508187877277747, Joe = -25.244871928039156</span>\n");
            writer.println("<h5>Course Marks</h5>");
            writer.println("<table style=\"width:60%\">\n");
            writer.println("<tr>\n" +
                            "    <th></th>" +
                            "    <th>Course A</th>\n" +
                            "    <th>Course B</th> \n" +
                            "  </tr>");
            writer.println("<tr>\n" +
                            "    <th>Stu</th>" +
                            "    <th>(0.0, 0.02), (0.7, 0.3), (0.8, 0.2), (0.3, 0.1), (0.01, 0.02)</th>\n" +
                            "    <th>(0.6, 0.1), (0.7, 0.3), (0.8, 0.2), (0.4, 0.1), (0.85, 0.2)</th> \n" +
                            "  </tr>");
            writer.println("<tr>\n" +
                            "    <th>Joe</th>" +
                            "    <th>Not Enrolled</th>\n" +
                            "    <th>(0.7, 0.1), (0.7, 0.3), (0.7, 0.2), (0.75, 0.1), (0.65, 0.2)</th> \n" +
                            "  </tr>");
            writer.println("</table>\n");
            writer.println("\n\n\n\n");
            writer.println("</body>");
            writer.println("</html>");
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HTMLOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HTMLOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
    