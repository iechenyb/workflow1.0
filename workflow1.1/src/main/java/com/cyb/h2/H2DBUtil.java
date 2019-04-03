package com.cyb.h2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
public class H2DBUtil {

/**
 * DataSourceDBConnection.java
 * 功能:获取由JNDI绑定的数据源并创建JDBC连接[需要JNDI的支持]
 * @author boonya
 * @version 1.0 2013-03-11
 * 注意：启动会抛出异常，因为数据源没有加载到运行环境
 * [一般在web项目中使用这种方式获取连接]
 */
	/**
	 * 注册一个JNDI命名调用服务
	 */
	public static void register(){
		 JdbcDataSource ds = new JdbcDataSource();
		 ds.setURL("jdbc:h2:˜/test");
		 ds.setUser("sa");
		 ds.setPassword("123456");
		 Context ctx;
		 try {
			ctx = new InitialContext();
			 try {
					ctx.bind("jdbc/dsName", ds);
				} catch (NamingException e) {
					e.printStackTrace();
				}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用获取数据源建立JDBC连接
	 * @return
	 */
	public static Connection getConnection(){
		Context ctx;
		try {
			ctx = new InitialContext();
			 DataSource ds = (DataSource) ctx.lookup("jdbc/dsName");
			 try {
				Connection conn = ds.getConnection();
				return conn;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static void main(String[] args) {
		H2DBUtil.register();
		System.out.println(H2DBUtil.getConnection());
	}

}