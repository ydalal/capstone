package tagcomp.site.interfaces;

import tagcomp.site.client.SessionInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that includes remote calls for starting and fetching information for a user session.
 * @author Daniel
 */
@RemoteServiceRelativePath("site")
public interface SessionService extends RemoteService
{
	SessionInfo login(String user, String pwd) throws Exception;
	
	void changePassword(String newPass) throws Exception;
}
