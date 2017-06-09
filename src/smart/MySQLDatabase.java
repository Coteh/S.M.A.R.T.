/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author daniel
 */
public class MySQLDatabase {
    
    String  location;
    String userName;
    String password;
    Connection dataBaseConnection;
    
    public MySQLDatabase(String newLocation, String newUserName, String newPassword){
        location = newLocation;
        userName = newUserName;
        password = newPassword;
        dataBaseConnection = null;
    }
            
    /**
     * @param args the command line arguments
     */
    public void connectToDataBase(){
        try{
            dataBaseConnection = DriverManager.getConnection(location, userName, password);
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            dataBaseConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void createTable(String tableName, String variables){
        String sqlStatement = "CREATE TABLE "+ tableName + " (" + variables + " )";
        System.out.println(sqlStatement);
        try{
            Statement newStatement = dataBaseConnection.createStatement();
            newStatement.executeUpdate(sqlStatement);
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void insertInTableGrades(String tableName, int studentId, String courseId, double mark, double wheight,int markTime){
        String variables = "studentId INT, courseId VARCHAR(11), mark DOUBLE, wheight DOUBLE, markTime INT";
        String sqlStatement = "INSERT INTO GRADES (studentId, courseId, mark, wheight, markTime) values ( ?,?,?,?,? )";
        try{
            PreparedStatement preparedStmnt = dataBaseConnection.prepareStatement(sqlStatement);
            preparedStmnt.setInt(1,studentId);
            preparedStmnt.setString(2,courseId);
            preparedStmnt.setDouble(3,mark);
            preparedStmnt.setDouble(4,wheight);
            preparedStmnt.setInt(5,markTime);
            preparedStmnt.execute();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void insertInTableStudent(String tableName, int studentId, String email, double courseAvg, String nameOfStudent){
      
        String sqlStatement = "INSERT INTO STUDENTS (studentId, email, courseAvg, nameOfStudent) values ( ?,?,?,? )";
        
        System.out.println(sqlStatement);
        try{
            PreparedStatement preparedStmnt = dataBaseConnection.prepareStatement(sqlStatement);
            preparedStmnt.setInt(1,studentId);
            preparedStmnt.setString(2,email);
            preparedStmnt.setDouble(3,courseAvg);
            preparedStmnt.setString(4,nameOfStudent);
            preparedStmnt.execute();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
     public void insertInTable(String tableName, String variablesToInsert, String id, String foriegnTable){
        String sqlStatement = "INSERT INTO "+tableName +  " VARIABLES("+ variablesToInsert + "PRIMARYKEY(" + id +") FORIEGN KEY( "+foriegnTable+" )";
        try{
            Statement newStatement = dataBaseConnection.createStatement();
            newStatement.executeUpdate(sqlStatement);
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
     public ResultSet retrieveFromTable( String select, String from, String where ){
         String sqlStatement = "Select "+select + " From "+ from + " Where " + where;
         ResultSet currentResults = null;
         try{
            Statement newStatement = dataBaseConnection.createStatement();
            currentResults = newStatement.executeQuery(sqlStatement); 
         }catch(SQLException e){
             System.err.println(e.getMessage());
         }
         return currentResults;
     }
      public ResultSet retrieveFromTable( String select, String from){
         String sqlStatement = "select "+select + " from "+ from;
         ResultSet currentResults = null;
         try{
            Statement newStatement = dataBaseConnection.createStatement();
            currentResults = newStatement.executeQuery(sqlStatement); 
         }catch(SQLException e){
             System.err.println(e.getMessage());
         }
         return currentResults;
     }
    
}
