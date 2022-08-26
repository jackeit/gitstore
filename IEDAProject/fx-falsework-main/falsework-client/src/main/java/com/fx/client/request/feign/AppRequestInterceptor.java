package com.fx.client.request.feign;

import com.fx.client.store.ApplicatonStore;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AppRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {

        template.header("Authorization", ApplicatonStore.getToken());
    }
}