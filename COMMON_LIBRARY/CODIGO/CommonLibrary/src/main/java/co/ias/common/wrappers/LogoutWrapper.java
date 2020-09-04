package co.ias.common.wrappers;


public class LogoutWrapper {

	private String accessToken;
	private String scope;
	private String ip;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "LogoutWrapper [accessToken=" + accessToken + ", scope=" + scope
				+ ", ip=" + ip + "]";
	}
	
	
}
