package com.interview.assignment.distributedworker.service;

import okhttp3.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//Turn this on before running the test
//@Profile("test")
@Service
public class HttpService  {

    public Map<String,String> fetch(String url){
        OkHttpClient client = null;
        Map<String,String> store = new HashMap<String,String>();
        Response response = null;

        try  {
            client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();

            response = client.newCall(request).execute();

            store.put("http_code",String.valueOf(response.code()));

        } catch (Exception e){
            store.put("http_code", "ERROR");

        } finally {
            //I am handling the error here and not only in workerRunner to make sure the resources are freed!
            response.close();
        }
        return store;
    }
}
