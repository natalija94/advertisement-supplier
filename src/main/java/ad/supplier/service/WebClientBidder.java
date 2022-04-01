package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import ad.supplier.util.AdBidComparator;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static ad.supplier.mocks.RequestResponseMock.getMockResponse;

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

    public BidResponse postAdBid(String url, BidRequest bidRequest) {
        //todo handle errors
        log.info("Sending to server: {}", url);
        BidResponse response = buildWebClient(url)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bidRequest)
                .retrieve()
                .bodyToMono(BidResponse.class)
                .block();

        log.info("id: {}, url: {}, response: {}", response.getId(), url, response.getBid());
        return response;
    }

    public BidResponse findTheBestResponse(Flux<BidResponse> responses) {
        //todo
        return getMockResponse();
    }

    public void fetchBidsFromAuctionOneByOne(final BidRequest bidRequest) {
        Mono<Void> logUsers = Flux.fromIterable(bidServers)
                .map(url -> postAdBid(url, bidRequest))
                .doOnNext(bid -> log.info("Do something with response. {} \n", bid.getContent()))
                .then();
        logUsers.subscribe();
    }

    public void fetchBidsFromAuction(BidRequest bidRequest) {
        // return Flux.merge(post_requests);
    }
}
