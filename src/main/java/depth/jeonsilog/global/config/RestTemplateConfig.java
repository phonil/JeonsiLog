package depth.jeonsilog.global.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 커넥션 풀 설정
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200); // 전체 커넥션 수
        connectionManager.setDefaultMaxPerRoute(50); // 호스트별 커넥션 수

        // Request 설정
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(5000)) // 연결 시도 최대 x초
                .setResponseTimeout(Timeout.ofMilliseconds(5000)) // 응답 대기 최대 x초
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(1000)) // 풀에서 커넥션 가져오기 대기 최대 x초
                .build();

        // HttpClient 생성
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .evictIdleConnections(TimeValue.ofSeconds(30));
        HttpClient httpClient = builder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }
}
