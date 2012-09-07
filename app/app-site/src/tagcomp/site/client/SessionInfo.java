package tagcomp.site.client;

public class SessionInfo
{
	private String userName;
	private String email;

	public SessionInfo(String userName, String email)
	{
		super();
		this.userName = userName;
		this.email = email;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getEmail()
	{
		return email;
	}
	
	
}
