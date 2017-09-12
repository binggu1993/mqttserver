package com.mqtt.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDao
{
    private static final Logger log=LogManager.getLogger(UserDao.class);
    private Connection conn;
    
    /**
     * 根据推送类型查询。0，特定用户；1，根据用户分组推送；2，根据区域推送；3，全网推送
     * @param queryType
     * @param params
     * @return
     */
    public Set<String> getUsers(int queryType,String params)
    {
        Set<String> userSet = new HashSet<String>();
        String querySql = "";
        String[] param = params.split(",");
        
        Statement stmt = null;
        ResultSet rs = null;
        if (queryType == 1)
        {
            querySql = "select DISTINCT s.STB_CODE from system_t_userinfo s,system_t_usergroup g,system_t_usergroup_userinfo f "
                    + "where s.ID=f.USERINFO_ID and g.ID=f.USERGROUP_ID and g.USERGROUP_CODE in (";
        }
        else if (queryType == 2)
        {
//            querySql = "select DISTINCT t.STB_CODE  from system_t_userinfo t where " + "EXISTS (select * from (select DISTINCT a.AREA_CODE "
//                    + "from system_t_area a where FIND_IN_SET(id, getAllChildNode(";
            querySql="{CALL showChildLst(?)}";
        }
        else if(queryType == 3)
        {
            querySql = "select DISTINCT f.STB_CODE from system_t_userinfo f";
        }
        else if(queryType == 0)
        {
            for (int i = 0; i < param.length; i++)
            {
                userSet.add(param[i]);
            }
            return userSet;
        }
        
        try
        {
            conn = DBConnect.newConnection();
            
            if (queryType == 1)
            {
                for (int i = 0; i < param.length; i++)
                {
                    querySql += "'" + param[i] + "',";
                }
                if (querySql.endsWith(","))
                    ;
                querySql = querySql.substring(0, querySql.lastIndexOf(","));
                querySql += ")";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(querySql);
                while (rs.next())
                {
                    String userCode = rs.getString("stb_code");
                    userSet.add(userCode);
                }
                rs.close();
                rs = null;
                stmt.close();
                stmt = null;
            }
            else if (queryType == 2)
            {
                for (int i = 0; i < param.length; i++)
                {
//                    String querySql2 = querySql + "'" + param[i] + "')))B where t.AREA_CODE=B.AREA_CODE)";
//                    stmt = conn.createStatement();
                    CallableStatement cstm = conn.prepareCall(querySql);
                    cstm.setString(1, param[i]);
                    rs = cstm.executeQuery();
                    while (rs.next())
                    {
                        String userCode = rs.getString("stb_code");
                        userSet.add(userCode);
                    }
                    rs.close();
                    rs = null;
                    cstm.close();
                    cstm = null;
                }
            }
            else if(queryType == 3)
            {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(querySql);
                while (rs.next())
                {
                    String userCode = rs.getString("stb_code");
                    userSet.add(userCode);
                }
                rs.close();
                rs = null;
                stmt.close();
                stmt = null;
            }
            conn.close();
            conn = null;
            
        }
        catch (SQLException e)
        {
            log.error("getUsers error", e);
        }
        finally
        {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection(conn);
            return userSet;
        }
       
    } 
    public static void main(String[] args)
    {
        UserDao dao=new UserDao();
        Set<String> userList= dao.getUsers(1, "110");
        System.out.println(userList.size());
    /*  for(String usr:userList)
      {
          System.out.println(usr);
      }*/
      
    }
}
