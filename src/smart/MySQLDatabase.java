/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradesmart;
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
            System.out.println(e.getMessage());
        }
    }
    
    public void createTable(String tableName, String variables){
        String sqlStatement = "CREATE TABLE "+ tableName + " (" + variables + " )";
        try{
            Statement newStatement = dataBaseConnection.createStatement();
            newStatement.executeUpdate(sqlStatement);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void insertInTable(String tableName, String variablesToInsert){
        String sqlStatement = "INSERT INTO "+tableName +  " VARIABLES( "+ variablesToInsert +" )";
        try{
            Statement newStatement = dataBaseConnection.createStatement();
            newStatement.executeUpdate(sqlStatement);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
     public void insertInTable(String tableName, String variablesToInsert, String id, String foriegnTable){
        String sqlStatement = "INSERT INTO "+tableName +  " VARIABLES("+ variablesToInsert + "PRIMARYKEY(" + id +") FORIEGN KEY( "+foriegnTable+" )";
        try{
            Statement newStatement = dataBaseConnection.createStatement();
            newStatement.executeUpdate(sqlStatement);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
     public ResultSet retrieveFromTable( String select, String from, String where ){
         String sqlStatement = "Select "+select + " From "+ from + " Where " + where;
         ResultSet currentResults = null;
         try{
            Statement newStatement = dataBaseConnection.createStatement();
            currentResults = newStatement.executeQuery(sqlStatement); 
         }catch(SQLException e){
             System.out.println(e.getMessage());
         }
         return currentResults;
     }
    
}
