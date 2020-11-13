package co.odin.transformers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.springframework.web.multipart.MultipartFile;


public class URLRequestTransformer extends ProxyRequestTransformer
{
	private String serviceUrl;

	public URLRequestTransformer() {    
	}

	@Override
	public RequestBuilder transform(HttpServletRequest request, MultipartFile file) throws URISyntaxException {
		
	    URI uri;
	    if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
	      uri = new URI(serviceUrl + "?" + request.getQueryString());
	    } else {
	      uri = new URI(serviceUrl);
	    }
	
	    RequestBuilder rb = RequestBuilder.create(request.getMethod());
	    rb.setUri(uri);
	    return rb;
	}

	public void setServiceUrl(String serviceUrll) {
		serviceUrl = serviceUrll;
	}
}
