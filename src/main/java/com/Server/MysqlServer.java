package com.Server;

import com.Utils.MysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlServer {
    public static void main(String[] args) {
        String sql="insert into t_user(name,password,sex,email,age) values(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement statement=null;
        try {
            conn=MysqlUtil.getConnection();
            statement=conn.prepareStatement(sql);
            statement.setString(1,"lisi");
            statement.setString(2,"123");
            statement.setString(3,"男");
            statement.setString(4,"qq.com");
            statement.setInt(5,21);
            int index = statement.executeUpdate();
            if(index>0){
                System.out.println("成功！");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            MysqlUtil.closeConnection(conn);
            MysqlUtil.closeStatement(statement);
        }
    }
}
