package tagcomp.site.interfaces;

import tagcomp.site.client.SessionInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionServiceAsync
{
	void login(String user, String pwd, AsyncCallback<SessionInfo> callback) throws Exception;
	void changePassword(String newPass, AsyncCallback<Void> callback);
}
