package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author natalija
 */
@Service
public class WebClientBidder<T> {

    public HttpClient timeoutHandler() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
    }

    public WebClient buildWebClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", url))
                .build();


        //with timeout
        //WebClient client = WebClient.builder()
        //  .clientConnector(new ReactorClientHttpConnector(httpClient))
        //  .build();
    }

    public void createPostRequest(WebClient webClient, T requestBody) {
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.method(HttpMethod.POST);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(URI.create("/resource"));

        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(Mono.just(requestBody), requestBody.getClass());

        WebClient.ResponseSpec responseSpec = headersSpec.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();

    }

    public void postAdBid(String url, BidRequest bidRequest) {
        buildWebClient(url)
                .post()
                .uri("/bid/ad/{id}", 55)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bidRequest)
                .retrieve().bodyToMono(BidResponse.class).doOnError(throwable -> System.out.println(throwable));
    }

    public void postMock(String url, BidRequest bidRequest) {
        buildWebClient("http://localhost:8085/bid/ad")
                .post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(bidRequest), BidRequest.class)
                .retrieve().bodyToMono(BidResponse.class).block();


        buildWebClient("http://localhost:8081")
                .post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(bidRequest), BidRequest.class)
                .retrieve().bodyToMono(BidResponse.class).block();
    }


}
