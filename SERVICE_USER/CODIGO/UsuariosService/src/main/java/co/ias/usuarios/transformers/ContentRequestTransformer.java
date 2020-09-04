package co.ias.usuarios.transformers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;


@SuppressWarnings("deprecation")
public class ContentRequestTransformer extends ProxyRequestTransformer {

	@Override
	public RequestBuilder transform(HttpServletRequest request, MultipartFile file)
			throws NoSuchRequestHandlingMethodException, URISyntaxException, IOException {
		RequestBuilder requestBuilder = predecessor.transform(request, file);

		if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
			String message = "This is a multipart post";
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("file", file.getBytes(), ContentType.DEFAULT_BINARY, file.getName());
			builder.addTextBody("text", message, ContentType.DEFAULT_BINARY);
			
			HttpEntity entityMultipart = builder.build();
			requestBuilder.setEntity(entityMultipart);

			
		} else {

			String requestContent = request.getReader().lines().collect(Collectors.joining(""));
			if (!requestContent.isEmpty()) {
				StringEntity entity = new StringEntity(requestContent, ContentType.APPLICATION_JSON);
				requestBuilder.setEntity(entity);
			}
		}
		return requestBuilder;
	}

}