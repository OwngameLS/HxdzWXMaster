package com.owngame.utils;

import com.owngame.entity.Function;

import java.sql.*;

/**
 * 专门用于读取与自身不同数据库的工具类
 * Created by Administrator on 2016-10-10.
 */
public class DBUtil {
    public static Connection createConn(Function function) {
        Connection conn = null;
        String fullName = "";
        String url = "";
        String username = function.getUsername();
        String password = function.getPassword();
        String ip = function.getIp();
        String port = function.getPort();
        String dbName = function.getDbname();
        if(function.getDbtype().contains("MySQL")){
            fullName = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://"+ip+":"+port+"/"+dbName;
        }else if(function.getDbtype().contains("Microsoft SQL Server") || function.getDbtype().contains("Sybase") ){
            fullName = "net.sourceforge.jtds.jdbc.Driver";
            url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+dbName;
        }else if(function.getDbtype().contains("Oracle")){// thin模式
            fullName = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
        }else if(function.getDbtype().contains("PostgreSQL")){
            fullName = "org.postgresql.Driver";
            url = "jdbc:postgresql://"+ip+":"+port+"/"+dbName;
        }else if(function.getDbtype().contains("DB2")){
            fullName = "com.ibm.db2.jdbc.app.DB2.Driver";//连接具有DB2客户端的Provider实例
//            fullName = "com.ibm.db2.jdbc.net.DB2.Driver";//连接不具有DB2客户端的Provider实例
            url = "jdbc:db2:://"+ip+":"+port+"/"+dbName;
        }else if(function.getDbtype().contains("Informix")){
            fullName = "com.informix.jdbc.IfxDriver";
            url = "jdbc:Informix-sqli://"+ip+":"+port+"/"+dbName+":INFORMIXSER=myserver";
        }else if(function.getDbtype().contains("JDBC-ODBC")){
            fullName = "sun.jdbc.odbc.JdbcOdbcDriver";
            url = "jdbc:odbc:dbsource";
        }
        try {
            Class.forName(fullName);
            conn = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            conn = null;
            e.printStackTrace();
        }
         System.out.println("createConn");
        return conn;
    }

    public static PreparedStatement prepare(Connection conn, String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println("prepare");
        return ps;
    }

    public static void close(Connection conn) {
        if (conn == null)
            return;
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println("close conn");
    }

    public static void close(PreparedStatement stmt) {
        if (stmt == null)
            return;
        try {
            stmt.close();
            stmt = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        if (rs == null)
            return;
        try {
            rs.close();
            rs = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
