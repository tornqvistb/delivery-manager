package se.lanteam.ws;

public class WSConfig {

	public WSConfig(String endPoint, String userName, String password) {
		super();
		this.endPoint = endPoint;
		this.userName = userName;
		this.password = password;
	}
	private String endPoint;
	private String userName;
	private String password;
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
