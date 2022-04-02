package ad.supplier.service.impl;

import ad.supplier.exception.BidRequestException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static ad.supplier.businesslogic.AuctionValidator.isBidRequestValid;

/**
 * @author natalija
 */
@Service
@Log4j2
public class WebClientBidder {
    @Value("#{'${bidders}'.split(',')}")
    private TreeSet<String> bidServers;

    /**
     * Web client instance builder.
     *
     * @param url server.
     * @return webclient instance.
     */
    public WebClient buildWebClient(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must be specified in order to create connection.");
        }

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", url))
                .build();
    }

    /**
     * Prepares and sends the request to one bidder.
     *
     * @param url        Bidder server.
     * @param bidRequest request to be sent.
     * @return bid from Bidder.
     */
    public BidResponse bidRequestBuilder(String url, BidRequest bidRequest) {
        if (StringUtils.isEmpty(url) || !isBidRequestValid(bidRequest)) {
            throw new IllegalArgumentException("Both Url and valid request must be specified in order to send request for auction.");
        }

        return buildWebClient(url)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bidRequest)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
                        clientResponse -> {
                            log.error("ERROR: {} occurred when invoking {}}.", clientResponse.rawStatusCode(), url);
                            return Mono.empty();
                        })
                .bodyToMono(BidResponse.class)
                .onErrorMap(Predicate.not(BidRequestException.class::isInstance),
                        throwable -> {
                            log.error("Unexpected error occurred while trying to get Bid from {}. Please check service configuration.", url);
                            return new BidRequestException(throwable, String.format("Client exception when invoking %s.", url));
                        }
                )
                .block();
    }

    /**
     * Processing of bids: one by one.
     *
     * @param bidRequest     request to be broadcasted.
     * @param handleResponse defined action for received response.
     */
    public void fetchBidsOneByOne(final BidRequest bidRequest, Consumer<BidResponse> handleResponse) {
        if (handleResponse == null || !isBidRequestValid(bidRequest)) {
            throw new IllegalArgumentException("Specified params not defined.");
        }

        Mono<Void> logUsers = Flux.fromIterable(bidServers)
                .map(url -> bidRequestBuilder(url, bidRequest))
                .doOnNext(bid -> {
                    log.info("Received response: {}", bid);
                    handleResponse.accept(bid);
                })
                .then();
        logUsers.subscribe();
    }
}
