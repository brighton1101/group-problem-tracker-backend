package com.pm.backend.security;


import com.pm.backend.security.representations.UserException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pm.backend.security.representations.UserException.REASON.HTTP_GET_FAIL;
import static com.pm.backend.security.representations.UserException.REASON.HTTP_POST_FAIL;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class HttpUtils {

    public static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String postFormRequest(String url, SSLContext sslContext, Map<String, String> formParams) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier())).build();

        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader(CONTENT_TYPE,"application/x-www-form-urlencoded");
        postRequest.addHeader(ACCEPT, "application/json");

        List<NameValuePair> params = new ArrayList<>();
        for(String key : formParams.keySet()) {
            params.add(new BasicNameValuePair(key, formParams.get(key)));
        }
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != 200) {
                logger.error("Error in postForm: {}", response.getStatusLine().getReasonPhrase());
                throw new UserException(HTTP_POST_FAIL);
            }

            httpClient.close();
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new UserException(e, HTTP_POST_FAIL);
        }
    }

    public static String getRequest(String url, SSLContext sslContext) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier())).build();

        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader(ACCEPT, "application/json");

        try {
            HttpResponse response = httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode != 200) {
                logger.error("Error in postForm: {}", response.getStatusLine().getReasonPhrase());
                throw new UserException(HTTP_GET_FAIL);
            }
            httpClient.close();
            return EntityUtils.toString(response.getEntity());
        }catch (Exception e) {
            throw new UserException(e, HTTP_GET_FAIL);
        }
    }
}
