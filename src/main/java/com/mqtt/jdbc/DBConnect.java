package com.mqtt.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mqtt.util.SysConfig;


public class DBConnect {
	
    public static Logger log=LogManager.getLogger(DBConnect.class);
	//static Connection con = null;
	static String dbType = "";
	public static final String ORACLE = SysConfig.getSystemConfig("database.type");
//	static {
////		String driver = SystemPropertyUtil.getProperty("driverClass");
////		String url = SystemPropertyUtil.getProperty("url");
////		String user = SystemPropertyUtil.getProperty("username");
////		String password = SystemPropertyUtil.getProperty("password");
////		if (driver.indexOf(ORACLE) != -1) {
////			dbType = ORACLE;
////		}
////		try { 
////		   Class.forName(driver); 
////		   con=DriverManager.getConnection(url, user, password); 
////		} catch (ClassNotFoundException e) { 
////			log.error("create jdbc con error", e);
////		} catch (SQLException e1) { 
////			log.error("create jdbc con error", e1);
////		} 
//	}
	
	public static Connection newConnection()throws SQLException{
		Connection con = null;
		String driver = SysConfig.getSystemConfig("driverClass");
		String url = SysConfig.getSystemConfig("url");
		String user = SysConfig.getSystemConfig("username");
		String password = SysConfig.getSystemConfig("password");
		if (driver.indexOf(ORACLE) != -1) {
			dbType = ORACLE;
		}
		try {

		   Class.forName(driver); 
		   con=DriverManager.getConnection(url, user, password); 
		} catch (ClassNotFoundException e) { 
			log.error("create jdbc con error", e);
		} catch (SQLException e1) { 
			log.error("create jdbc con error", e1);
			throw e1;
		}
		return con;
	}
	

	
	/**
	 * 获取当前数据库序号，并更新一定的范围
	 * @param scope 序列增值
	 * @param seqName 序列名
	 * @return 当前可用序号
	 * @throws SQLException Long
	 */
	public static Long getSequence(int scope, String seqName) throws SQLException {
		Long result = 0L;
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = newConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select t.seq_value from t_seq_portal t where t.seq_name='"+seqName+"'");
			if (rs.next()) {
				result = rs.getLong("seq_value");
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			stmt = con.createStatement();
			stmt.execute("update t_seq_portal t set t.seq_value=t.seq_value+"+scope+" where t.seq_name='"+seqName+"'");
		} catch (SQLException e) {
			log.error("DBConnect getID error", e);
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(con);
		}
		return result;
	}
	
	/**
	 * 获取ID值或记录数，无记录则返回0
	 * @param sql
	 * @return Long
	 */
	public static Long getID(String sql) throws SQLException {
		Long id = 0L;
		Statement stmt = null;
		ResultSet rs = null;
		Connection con =null;
		try {
			con =  newConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			log.error("DBConnect getID error", e);
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(con);
		}
		
		return id;
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs = null;
		}
	}
	
	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stmt = null;
		}
	}
	
	public static void closeStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pstmt = null;
		}
	}
	
	public static void closeStatement(CallableStatement cstmt) {
		if (cstmt != null) {
			try {
				cstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cstmt = null;
		}
	}
	
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = null;
		}
	}
	
//	public static void tranStart() {
////		try {
////			con.setAutoCommit(false);
////		} catch (SQLException e) {
////			// TODO: handle exception
////			e.printStackTrace();
////		}
//	}
	
//	public static void tranCommit() {
////		try {
////			con.commit();
////			con.setAutoCommit(true);
////		} catch (SQLException e) {
////			// TODO: handle exception
////			e.printStackTrace();
////		}
//	}
	
//	public static void tranRollback() {
////		try {
////			con.rollback();
////			con.setAutoCommit(true);
////		} catch (SQLException e) {
////			// TODO: handle exception
////			e.printStackTrace();
////		}
//	}
	public static void main(String[] args){
		try {
			System.out.println(DBConnect.getID("select * from t_business_info"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
