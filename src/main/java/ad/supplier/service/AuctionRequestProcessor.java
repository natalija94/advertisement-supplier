package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

import static ad.supplier.mocks.RequestResponseMock.getMockResponse;

/**
 * @author natalija
 */
@Service
@Log4j2
public class AuctionRequestProcessor {
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
        sendRequest(bidRequest);
    }

    public BidResponse sendRequest(BidRequest bidRequest) {
        log.info("START");
        //todo
        webClientBidder.fetchBidsFromAuctionOneByOne(bidRequest);
        return getMockResponse();
    }

}
