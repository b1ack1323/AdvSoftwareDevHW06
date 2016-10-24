package SQL_Manager;

import java.sql.*;
import java.util.ArrayList;
/**
 * Created by tanner on 9/12/16.
 */
public class Database {

    ArrayList<String> urls = new ArrayList<>();
    Connection con;
    String db,table;
    private String[] columnName;

    public Database(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://tdbtech.co:3306/","advsoft","advsoftware");
        }catch(Exception e){ e.printStackTrace();}
    }

    public ArrayList<String> getResults(){
        urls.forEach(System.out::println);
        return urls;
    }
    public ArrayList<String> getSchemas() throws SQLException {
        ResultSet rs;
        ArrayList<String> res = new ArrayList<>();
        rs = con.getMetaData().getCatalogs();

        while(rs.next())
        {
            res.add(rs.getString(1));
            System.out.println(rs.getString(1));
        }

        return res;
    }
    public ArrayList<String> getTables() throws SQLException{
        ArrayList<String> res = new ArrayList<>();
        ResultSet rs = con.getMetaData().getTables(null, null, "%", null);
        while (rs.next()) {
            System.out.println(rs.getString(3));
            res.add(rs.getString(3));
        }

        return res;
    }

    public void CloseCon() throws SQLException {
        con.close();
    }


    public void chooseDB(String db) throws SQLException {
        this.db = db;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://tdbtech.co:3306/"+db,"advsoft","advsoftware");
        }catch(Exception e){ e.printStackTrace();}
    }
    public void chooseTable(String table) throws SQLException {
        this.table = table;
    }
    public ResultSet query(String q) throws SQLException {
        System.out.println(q);
        if(db != null) {
            Statement stmt = con.createStatement();
            return stmt.executeQuery(q);
        }
        return null;
    }
    public void insertQuery(String q) throws SQLException {
        System.out.println(q);
        if(db != null) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(q);
            System.out.println("INSERTED");
        }
    }

    public void updateQuery(String q, String id) throws SQLException {
        System.out.println(q);
        if(db != null) {
            Statement stmt = con.createStatement();
            String query = "UPDATE "+getSelectedTable()+" SET "+columnName[1]+"='"+q+"' WHERE "+columnName[0]+"='"+id+"'";
            System.out.println(query);
            stmt.executeUpdate(query);
            System.out.println("INSERTED");
        }
    }

    public String getSelectedTable() {
        return table;
    }

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }

    public String[] getColumnName() {
        return columnName;
    }
}

