package com.animehost.config.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectFactory {

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection("jdbc:mysql://localhost/bd_anime","root","password");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
