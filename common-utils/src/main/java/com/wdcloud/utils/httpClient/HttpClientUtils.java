package com.wdcloud.utils.httpClient;

import com.wdcloud.utils.StringUtil;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    protected static HttpClient httpClient = HttpClientFactory.createTLSHttpClient(100, 10);

    private static Map<String, HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();

    public static void init(int maxTotal, int maxPerRoute) {
        httpClient = HttpClientFactory.createTLSHttpClient(maxTotal, maxPerRoute);
    }

    /**
     * 初始化 MCH HttpClient KeyStore
     *
     * @param mch_id
     * @param keyStoreFilePath
     */
    public static void initMchKeyStore(String mch_id, String keyStoreFilePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(keyStoreFilePath));
            keyStore.load(instream, mch_id.toCharArray());
            instream.close();
            HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id,
                    new String[]{"TLSv1"});
            httpClient_mchKeyStore.put(mch_id, httpClient);
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException e) {
            e.printStackTrace();
        }
    }

    public static HttpResponse execute(HttpUriRequest request) {
        try {
            return httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) {
        try {
            return httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据返回自动JSON对象解析
     *
     * @param request
     * @param clazz
     * @return
     */
    public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz) {
        return execute(request, JsonResponseHandler.createResponseHandler(clazz));
    }

    /**
     * 数据返回自动XML对象解析
     *
     * @param request
     * @param clazz
     * @return
     */
    public static <T> T executeXmlResult(HttpUriRequest request, Class<T> clazz) {
        return execute(request, XmlResponseHandler.createResponseHandler(clazz));
    }

    /**
     * MCH keyStore 请求 数据返回自动XML对象解析
     *
     * @param mch_id
     * @param request
     * @param clazz
     * @return
     */
    public static <T> T keyStoreExecuteXmlResult(String mch_id, HttpUriRequest request, byte[] keyStoreData,
                                                 Class<T> clazz) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = new ByteArrayInputStream(keyStoreData);
            keyStore.load(instream, mch_id.toCharArray());
            instream.close();
            HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id,
                    new String[]{"TLSv1"});
            return httpClient.execute(request, XmlResponseHandler.createResponseHandler(clazz));
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendGet(String url, String param) {
        return sendGet(url, param, null);
    }

    public static String sendGet(String url, String param, String respCharset) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(15000);
            connection.connect();
            if (StringUtil.isNotEmpty(respCharset)) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), respCharset));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.debug("Http get request complete, response={}", result);
        } catch (Exception e) {
            logger.warn("Http get catch an exception, error={}", e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sentPost(Map<String, String> reqData, String uriAPI, String encoding) {
        String result = "";
        try {
            HttpPost httpRequst = new HttpPost(uriAPI);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : reqData.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpRequst.setEntity(new UrlEncodedFormEntity(params, encoding));

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(15000)
                    .setConnectTimeout(3000)
                    .build();
//          HttpClient httpclient = HttpClients.createDefault();
            HttpClient httpclient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpResponse httpResponse = httpclient.execute(httpRequst);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity, encoding);
            }
            logger.debug("Http post request complete, status={}, response={}", statusCode, result);
        } catch (Exception e) {
            logger.warn("Http post request catch exception, err={}", e.getMessage(), e);
            result = null;
        }
        return result;
    }

    public static String upload(String uriAPI, InputStream inputStream, String fileName) {
        String result = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(15000).setSocketTimeout(3000).build();
        HttpPost httpPost = new HttpPost(uriAPI);
        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
        multipartEntityBuilder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, fileName);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity responseEntity = httpResponse.getEntity();
                result = EntityUtils.toString(responseEntity, "UTF-8");
            }
            httpClient.close();
            httpResponse.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResponseContentCharsetName(HttpResponse response) {
        String charset = null;
        if (response != null) {
            Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);

            if (headers != null) {
                for (Header header : headers) {
                    if (header.getName().equals(HttpHeaders.CONTENT_TYPE)) {
                        String contentType = header.getValue();
                        String[] split = contentType.split(";");
                        if (split.length > 1) {
                            split = split[1].trim().split("=");
                            if (split.length > 1) {
                                charset = split[1];
                            }
                            break;
                        }
                    }
                }
            }

            if (charset != null) {
                try {
                    Charset.forName(charset);
                    return charset;
                } catch (UnsupportedCharsetException e) {
                    // return http default charset
                }
            }
        }

        return "ISO-8859-1";
    }
}