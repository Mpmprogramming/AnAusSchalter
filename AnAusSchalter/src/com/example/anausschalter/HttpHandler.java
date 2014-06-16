package com.example.anausschalter;


import org.apache.http.client.methods.HttpUriRequest;
import com.example.anausschalter.AsyncHttpTask;

public abstract class HttpHandler {

    public abstract HttpUriRequest getHttpRequestMethod();

    public abstract void onResponse(String result);

    public void execute(){
        new AsyncHttpTask(this).execute();
    } 
}