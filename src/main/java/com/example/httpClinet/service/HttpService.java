package com.example.httpClinet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HttpService {

    @Autowired
    private CloseableHttpClient httpClient;

    /**
     * the method is a sample for how to make a get request
     * and how to use reponse handler
     */
    public String getExampleData() throws IOException, ParseException {
        HttpGet httpGet = new HttpGet("https://mitanks.vassarlabs.com/api/mitank/main/structuredataIwm");
        HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {
            @Override
            public String handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
                int status = classicHttpResponse.getCode();
                HttpEntity entity = classicHttpResponse.getEntity();
                if (status >= 200 && status < 300) {
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (ParseException e) {
                        throw new IOException("Failed to parse the response entity", e);
                    }
                }else{
                    throw new HttpResponseException(status, "Unexpected response status: " + status);
                }
            }
        };
            String httpResponse = null;
        try{
            httpResponse = httpClient.execute((ClassicHttpRequest) httpGet,responseHandler);

        }catch (Exception e){
             return "exception occuring "+ e;
        }

        return httpResponse;
    }


    /**
     *sample code for an URI builder
     */
    public void testuri() throws URISyntaxException, IOException {
        URI uri = new URIBuilder().setScheme("https").setHost("www.google.com").setPath("/search")
                .setParameter("tag","self learning").build();
        //printing the createde uri
        System.out.println(uri);
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("content-type", "application/json");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        printResponse(httpResponse);
        //headers
        httpResponse.close();
    }

    private void printResponse(HttpResponse response) {
        System.out.println(response.getVersion());
        System.out.println(response.getCode());
        System.out.println(response.getReasonPhrase());
        System.out.println(response.toString());
        response.setHeader("content-type","it is meee");
        Header[] headers= response.getHeaders();
        for (int i=0;i<headers.length;i++){
            System.out.println("maxx :: headers ::"+ headers[i].getName() +"::"+headers[i].getValue());
        }
    }

    public String testingPost() throws IOException, ParseException {
        HttpPost post = new HttpPost( "https://api.restful-api.dev/objects");
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Apple MacBook Pro 16");
        Map<String, Object> data = new HashMap<>();
        data.put("year", 2019);
        data.put("price", 1849.99);
        data.put("CPU model", "Intel Core i9");
        data.put("Hard disk size", "1 TB");
        payload.put("data",data);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(payload);
        StringEntity entity = new StringEntity(jsonPayload);
        post.setEntity(entity);
        post.setHeader("content-type", "application/json");
        CloseableHttpResponse response= httpClient.execute(post);
       return EntityUtils.toString(response.getEntity());
    }
}
