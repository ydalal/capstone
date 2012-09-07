package tagcomp.site.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import tagcomp.server.BCrypt;
import tagcomp.server.QuickJDBC;
import tagcomp.site.client.SessionInfo;
import tagcomp.site.interfaces.SessionService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementación del servidor del servicio. Ver PortalService para la documentación de los métodos.
 * TODO Analizar el tema del Context en Tomcat (¿Qué hace? ¿Para qué podría servirme? verificar si mantiene razonablemente la sesión).
 */
@SuppressWarnings("serial")
public class SessionServiceImp extends RemoteServiceServlet implements SessionService
{
	/**
	 * Constructor del servicio que establece la conexión con la base de datos.
	 */
	public SessionServiceImp()
	{
		QuickJDBC.initialize("localhost/capstone", "capstone", "capstone");
	}

	/**
	 * Si la sesión aún es válida la extiende a partir del instante actual.
	 */
	public Integer keepSessionAlive()
	{
		// Las llamadas mismas al servidor ligadas a la misma sesión debieran reactivarla. Verificar si es así o hacer algo aquí con la sesión.
		if(!this.getThreadLocalRequest().isRequestedSessionIdValid())
			return 0;
		
		HttpSession session = this.getThreadLocalRequest().getSession();
		int window = (int)(session.getLastAccessedTime() + 1000*session.getMaxInactiveInterval() - System.currentTimeMillis());
		return window;
	}
	
	/**
	 * Authenticates a user into the system, returning the session data needed by the client.
	 */
	public SessionInfo login(String userId, String pwd) throws Exception
	{
		ResultSet rset = null;
		try
		{
			// Valido el profile -------------------------------------------------------
			rset = QuickJDBC.execute("SELECT u.name, u.password FROM session.users AS u WHERE u.email = " + userId);
			if(!rset.next())
				throw new RuntimeException("Invalid user.");

			String userName = rset.getString(1);
			String bdHash = rset.getString(2);

			if(!BCrypt.checkpw(pwd, bdHash))
				throw new RuntimeException("Invalid password.");
			
			SessionInfo s = new SessionInfo(userId, userName);
			return s; 
		}
		catch (SQLException e)
		{
			throw new RuntimeException("Authentication error, please try later.");
		}
		finally
		{
			if(rset != null)
				QuickJDBC.closeEverything(rset);
		}
	}
	
//	public Session getSession()
//	{
//		// Traigo el objeto almacenado en sesión.
//		HttpSession session = this.getThreadLocalRequest().getSession();
//		Session s = (Session)session.getAttribute("sessionData"); 
//		return s;
		//session.setAttribute("sessionData", s);
//	}
	
	public void changePassword(String newPass) throws Exception
	{
		HttpSession session = this.getThreadLocalRequest().getSession();
//		Session s = (Session)session.getAttribute("sessionData");
	}
}
