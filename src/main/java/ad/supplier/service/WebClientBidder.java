package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * @author natalija
 */
@Service
@Log4j2
public class WebClientBidder {
    @Value("#{'${bidders}'.split(',')}")
    private HashSet<String> bidServers;

    public WebClient buildWebClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", url))
                .build();
    }

    public void postAdBid(String url, BidRequest bidRequest, Consumer<BidResponse> handleResponse) {
        //todo handle errors
        log.info("Sending to server: {}", url);
        BidResponse response = bidRequestBuilder(url, bidRequest).block();
        handleResponse.accept(response);

        log.info("Bid Response: id: {}, url: {}, response: {}", response.getId(), url, response.getBid());
    }

    public Mono<BidResponse> bidRequestBuilder(String url, BidRequest bidRequest) {
        //todo handle errors
        return buildWebClient(url)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bidRequest)
                .retrieve()
                .bodyToMono(BidResponse.class);
    }

    public void fetchBidsFromAuctionOneByOne(final BidRequest bidRequest, Consumer<BidResponse> handleResponse) {
        Mono<Void> logUsers = Flux.fromIterable(bidServers)
                .map(url -> bidRequestBuilder(url, bidRequest))
                .doOnNext(bid -> log.info("Handle response. {} \n", bid.block().getContent()))
                .then();
        logUsers.subscribe();
    }
}
