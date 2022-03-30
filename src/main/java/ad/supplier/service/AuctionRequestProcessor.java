package ad.supplier.service;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static ad.supplier.mocks.RequestResponseMock.getMockResponse;

/**
 * @author natalija
 */
@Service
@Log4j2
public class AuctionRequestProcessor {
    @Value("#{'${bidders}'.split(',')}")
    private HashSet<String> bidServers;

    public BidRequest prepareRequest(String id, Map<String, String> attributes) {
        return BidRequest.builder().id(id).attributes(attributes).build();
    }

    public BidResponse sendRequest(BidRequest bidRequest, String address) {
        log.info("START");
        log.info("Sending request {} to the server = > {} ", bidRequest, address);
        return getMockResponse(bidRequest, address);
    }

    public void processRequestForAuction(String id, Map<String, String> allParams) {
        BidRequest bidRequest = prepareRequest(id, allParams);
        sendRequest(bidRequest, bidServers.stream().findFirst().get());

    }

}
