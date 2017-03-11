/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author James
 */
public class MarkDataInput {
    public static MarkData[] getData(String filepath) throws IOException {
        Workbook excelWorkbook;
        FileInputStream inputStream = new FileInputStream(new File(filepath));
        
        excelWorkbook = new XSSFWorkbook(inputStream);
        
        Iterator<Sheet> sheetIterator = excelWorkbook.iterator();
        
        //Iterating through each sheet (each sheet denotes a semester)
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            Iterator<Row> rowIterator = sheet.iterator();
            
            //Iterating through each row (each row denotes a particular student
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.iterator();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Double cellVal = cell.getNumericCellValue();
                    System.out.println(cellVal);
                }
            }
        }
        
        excelWorkbook.close();
        inputStream.close();
        
        return null;
    }
}
