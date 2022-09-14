package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.javagui.App;
import com.javagui.PrimaryController;

import javafx.fxml.FXMLLoader;

public class DataBaseController {
    
    private String query;
    private Connection connection;
    private Statement st;
    private PrimaryController pc;
    private App app;

    ArrayList<String> al = new ArrayList<String>();

    public DataBaseController(String uName, String url, String password)
    {
        try 
        {
            //this.query = query;
            connection = DriverManager.getConnection(url, uName, password);
            st = connection.createStatement();
        } 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println("Error connecting to database");
        }
        
    }

    public void pushComponentData(String query) throws SQLException
    {
        Statement st = connection.createStatement();
        ResultSet rs;

    }

    public void pullComponentData() throws SQLException
    {
        pc = (PrimaryController) App.getFxmlLoader().getController();
        ResultSet rs;
        String query = "SELECT * FROM components_data.components";
        

        rs = st.executeQuery(query);
        
        if(st.execute(query))
        {
            rs = st.getResultSet();
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int numOfColumns = rsmd.getColumnCount();

        System.out.println("numOfColumns: " + numOfColumns);
        while(rs.next())
        {
            int i = 1;
            while(i <= numOfColumns)
            {
                al.add(rs.getString(i++));
            }

            pc.updateComboBox(rs.getString("FunctionDescription"), al);
            System.out.println("Column value " + rs.getString("FunctionDescription"));

        }
    }

    public ArrayList<String> getArrayList()
    {
        return al;
    }



}

