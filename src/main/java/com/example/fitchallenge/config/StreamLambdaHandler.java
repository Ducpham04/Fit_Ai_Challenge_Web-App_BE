package com.example.fitchallenge.config;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.example.fitchallenge.FitChallengeApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            System.out.println("Initializing Lambda Handler...");
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(FitChallengeApplication.class);
            System.out.println("Handler initialized successfully!");
        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Cannot initialize Spring Boot", e);
        }
    }


    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);
    }
}
