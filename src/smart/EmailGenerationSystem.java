/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class EmailGenerationSystem {
    private SNHList snhList;
    private String helperEmail;

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("usage: SMART [WORKBOOK_FILE]");
            System.exit(1);
        }
        try {
            MarkDataInput.getData(args[0]);
        } catch (IOException ex) {
            Logger.getLogger(EmailGenerationSystem.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
