package com.k11.testautomation.apitesting_lessons.apitesting_httpClientDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k11.testautomation.apitesting_lessons.apitesting_httpClientDemo.pojo.APOD;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;

public class ApacheHttpClientDemo {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");

            APOD response = client.execute(request, httpResponse ->
                mapper.readValue(httpResponse.getEntity().getContent(), APOD.class));

            System.out.println(response.title);
        }

    }

}