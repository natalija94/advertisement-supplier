package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author natalija
 */
@Service
@Log4j2
public class AuctionRequestProcessor {
    @Value("#{'${bidders}'.split(',')}")
    private HashSet<String> bidServers;

    public final WebClientBidder webClientBidder;

    public AuctionRequestProcessor(WebClientBidder webClientBidder) {
        this.webClientBidder = webClientBidder;
    }

    public BidRequest prepareRequest(String id, Map<String, String> attributes) {
        return BidRequest.builder()
                .id(id)
                .attributes(attributes)
                .build();
    }

    public void processRequestForAuction(String id, Map<String, String> allParams) {
        BidRequest bidRequest = prepareRequest(id, allParams);
        sendRequest(bidServers.stream().findFirst().get(), bidRequest);
    }

    public BidResponse sendRequest(String address, BidRequest bidRequest) {
        log.info("START");
        log.info("Sending request {} to the server = > {} ", bidRequest, address);
        return webClientBidder.postAdBid(address, bidRequest);
    }
}
