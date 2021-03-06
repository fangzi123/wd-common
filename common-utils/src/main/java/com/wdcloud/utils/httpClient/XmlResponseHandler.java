package com.wdcloud.utils.httpClient;

import com.wdcloud.utils.XMLConverUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class XmlResponseHandler {
    private static Logger logger = LoggerFactory.getLogger(XmlResponseHandler.class);

    private static Map<String, ResponseHandler<?>> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz) {
        if (map.containsKey(clazz.getName())) {
            return (ResponseHandler<T>) map.get(clazz.getName());
        } else {
            ResponseHandler<T> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                String payload = null;

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String charset = HttpClientUtils.getResponseContentCharsetName(response);
                    payload = new String(EntityUtils.toString(entity).getBytes(charset), "UTF-8");
                }

                logger.info("[XMLResponseHandler] receive json response. status={}, payload={}", status, payload);

                if (status >= 200 && status < 300) {
                    return XMLConverUtil.convertToObject(clazz, payload);
                } else {
                    logger.warn("[XMLResponseHandler] receive a unexpected response code. code={}, payload={}", status, payload);
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            };
            map.put(clazz.getName(), responseHandler);
            return responseHandler;
        }
    }

}
