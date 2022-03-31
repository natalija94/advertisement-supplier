package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

import static ad.supplier.mocks.RequestResponseMock.getMockResponse;

/**
 * @author natalija
 */
@Service
public class WebClientBidder {
    public WebClient buildWebClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", url))
                .build();
    }

    public BidResponse postAdBid(String url, BidRequest bidRequest) {
        //todo handle errors
        BidResponse response = buildWebClient(url)
                .post()
                .uri("/bid/ad", 55)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bidRequest)
                .retrieve().bodyToMono(BidResponse.class).block();
        return response;
    }

    private BidResponse findTheBestResponse() {
        return getMockResponse();
    }
}
