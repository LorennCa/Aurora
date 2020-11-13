package co.common.wrappers;

public class ResourceAccessWrapper {

	private static ResourceAccessWrapper resourceAccessWrapper;

	private String accessToken;
	private String scope;
	private String ip;
	private String URI;

	public ResourceAccessWrapper(String accessToken, String scope, String URI) {
		this.accessToken = accessToken;
		this.scope = scope;
		this.URI = URI;
	}

	public ResourceAccessWrapper() {

	}

	public static ResourceAccessWrapper getSingletonInstance() {
		if (resourceAccessWrapper == null) {
			resourceAccessWrapper = new ResourceAccessWrapper();
		}
		return resourceAccessWrapper;
	}

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

	public String getURI() {
		return URI;
	}

	public void setURI(String uri) {
		this.URI = uri;
	}

	@Override
	public String toString() {
		return "ResourceAccessWrapper [accessToken=" + accessToken + ", scope="
				+ scope + ", ip=" + ip + ", uri=" + URI + "]";
	}
	
	
}
