package co.odin.transformers;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;


@SuppressWarnings("deprecation")
public class HeadersRequestTransformer extends ProxyRequestTransformer {

  @Override
  public RequestBuilder transform(HttpServletRequest request, MultipartFile file) throws NoSuchRequestHandlingMethodException, URISyntaxException, IOException {
    
	RequestBuilder requestBuilder = predecessor.transform(request, file);
    
    /**Original requester address header added **/
    requestBuilder.addHeader("X-Forwarded-For", request.getRemoteAddr());
        
    return requestBuilder;
  }
}
