package co.odin.transformers;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.RequestBuilder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;


@SuppressWarnings("deprecation")
public abstract class ProxyRequestTransformer {

  protected ProxyRequestTransformer predecessor;

  public abstract RequestBuilder transform(HttpServletRequest request, MultipartFile file) throws NoSuchRequestHandlingMethodException, URISyntaxException, IOException;

  public void setPredecessor(ProxyRequestTransformer transformer) {
    this.predecessor = transformer;
  }

}