package com.example.httpClinet.controller;

import com.example.httpClinet.service.HttpService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class HttpController {

    @Autowired
    private HttpService httpService;

    /**
     * the method is a sample for how to make a get request
     */
    @GetMapping("/test")
    public String testingHttp() throws IOException, ParseException {
     return  httpService.getExampleData();
    }
    /**
     *sample code for an URI builder and headers
     */
    @GetMapping("/testUri")
    public void testingURI() throws URISyntaxException, IOException {
        httpService.testuri();
    }

    /**
     *
     */
    @GetMapping("/testPostMethod")
    public String testingPost() throws IOException, ParseException {
          return httpService.testingPost();
    }



}
