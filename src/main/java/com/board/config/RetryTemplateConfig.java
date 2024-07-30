package com.board.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RetryTemplateConfig {

    //  host(IP와 Port의 조합)당 Connection Pool에 생성 가능한 Connection 수
    private static final int MAX_CONN_PER_ROUTE = 100;

    //  Connection Pool의 수용 가능한 최대 사이즈
    private static final int MAX_CONN_TOTAL = 300;

    private static final int CONN_TIMEOUT = 3000;

    private static final int MAX_ATTEMPTS = 3;

    @Bean
    public RestTemplate retryTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(CONN_TIMEOUT);
        HttpClient httpClient = createHttpClient();
        factory.setHttpClient(httpClient);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(clientHttpRequestInterceptor());
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    private HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(createHttpClientConnectionManager())
                .build();
    }

    private HttpClientConnectionManager createHttpClientConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .build();
    }

    private ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(MAX_ATTEMPTS));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
