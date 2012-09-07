package tagcomp.server;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Convenience class to be able to execute queries against a single database with low boilerplate. 
 * It's not a silver-bullet solution if requirements include multiple databases, authentications, etc.
 * @author Daniel
 *
 * TODO Currently limited to Postgres connectivity.
 * TODO Currently receives external configuration programatically. Implement config.
 */
public class QuickJDBC
{
	private static ComboPooledDataSource cpds = null;
	
	/**
	 * Initializes a datastore using a JDBC connection pool. 
	 * @param url Url where the database is located. E.g: localhost/capstone
	 * @param user User to access the database.
	 * @param password User password.
	 * @param reset If a datastore has already been initialized this method is a no-op. Use this flag to force a reset.
	 */
	public static void initialize(String url, String user, String password, boolean reset)
	{
		cpds = new ComboPooledDataSource();
		try
		{
			cpds.setDriverClass( "org.postgresql.Driver" );
			cpds.setJdbcUrl("jdbc:postgresql://" + url);
			cpds.setUser(user);                                  
			cpds.setPassword(password);
		} 
		catch (PropertyVetoException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void initialize(String url, String user, String password)
	{
		initialize(url, user, password, false);
	}
	
	/**
	 * Asks for a new connection and executes a query directly.
	 * @param query SQL to prepare for execution.
	 * @return the results from the query.
	 * @throws SQLException if a database access error occurs or this method is called on a closed connection.
	 */
	public static ResultSet execute(String query) throws SQLException
	{
		return cpds.getConnection().createStatement().executeQuery(query);
	}
	
	/**
	 * Prepares an SQL statement. 
	 * @param query SQL to prepare for execution.
	 * @return a prepared statement, which can be saved and used multiple times with different parameters.
	 * @throws SQLException if a database access error occurs or this method is called on a closed connection.
	 */
	public static PreparedStatement prepareStatement(String query) throws SQLException
	{
		return cpds.getConnection().prepareStatement(query);
	}

	/**
	 * Closes the result set, it's statement and it's connection.
	 * The connection is actually recycled in the underlying connection pool.
	 * @param rs A valid resultset.
	 */
	public static void closeEverything(ResultSet rs)
	{
		try
		{
			Statement stmt = rs.getStatement();
			if(stmt != null)
			{
				Connection con = stmt.getConnection();
				if(con != null)
					con.close();
				
				stmt.close();	// ALso closes all ResultSets 
			}
		} 
		catch (SQLException e)
		{
			// No access to the db. Not much to do here.
		}
	}

	/**
	 * Executes the SQL query.
	 * @param query A query that does not return values.
	 * @throws SQLException if a database access error occurs or this method is called on a closed connection.
	 */
	public static void executeUpdate(String query) throws SQLException
	{
		cpds.getConnection().createStatement().executeUpdate(query);
	}
}