package com.linq;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * zhihu登录
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class Login {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    public String login(String email, String password) throws IOException {
        if (StringUtils.isBlank(email))
            return "";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.zhihu.com/login");
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
        httpParams.setParameter("Host", "www.zhihu.com");
        client.setParams(httpParams);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("rememberme", "y"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        HttpResponse entity = client.execute(httpPost);
        Header[] headers = entity.getHeaders("Set-Cookie");
        StringBuilder result = new StringBuilder(500);
        for (Header header : headers) {
            for (HeaderElement element : header.getElements()) {
                result.append(String.format("%s=%s;", element.getName(), element.getValue()));
            }
        }

        logger.info("login to zhihu");

        return result.toString();
    }
}
