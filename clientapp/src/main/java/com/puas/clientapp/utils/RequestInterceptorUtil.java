package com.puas.clientapp.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;

public class RequestInterceptorUtil implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptorUtil.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        Authentication auth = AuthSessionUtil.getAuthentication();

        String path = request.getURI().getPath();

        if (!path.equals("/auth/login") && !path.equals("/auth/registration")) {
            if (auth != null && auth.getName() != null && auth.getCredentials() != null) {
                request.getHeaders().add("Authorization", "Basic " + BasicHeaderUtil.createBasicToken(
                        auth.getName(), auth.getCredentials().toString()));
            } else {
                logger.warn("Cannot add Authorization header, auth or its properties are null.");
            }
        }

        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

}
