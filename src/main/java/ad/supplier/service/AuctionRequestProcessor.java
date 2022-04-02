package ad.supplier.service;

import ad.supplier.businesslogic.AuctionHandler;
import ad.supplier.businesslogic.AuctionItemsProcessor;
import ad.supplier.enums.BidOperationType;
import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;

import static ad.supplier.businesslogic.AuctionValidator.isBidRequestValid;

/**
 * @author natalija
 */
@Service
@Log4j2
public class AuctionRequestProcessor {

    public final WebClientBidder webClientBidder;
    public final AuctionItemsProcessor auctionItemsProcessor;
    @Value("${bidOperationType:SEQUENTIAL}")
    BidOperationType bidOperationType;
    @Value("#{'${bidders}'.split(',')}")
    private TreeSet<String> bidServers;

    public AuctionRequestProcessor(WebClientBidder webClientBidder, AuctionItemsProcessor auctionItemsProcessor) {
        this.webClientBidder = webClientBidder;
        this.auctionItemsProcessor = auctionItemsProcessor;
    }

    public BidRequest prepareRequest(String id, Map<String, String> attributes) {
        if (StringUtils.isEmpty(id) || attributes == null || attributes.isEmpty()) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }
        return BidRequest.builder().id(id).attributes(attributes).build();
    }

    public BidResponse processRequestForAuction(String id, Map<String, String> attributes) throws NoAvailableBidException {
        if (StringUtils.isEmpty(id) || attributes == null || attributes.isEmpty()) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }

        BidRequest bidRequest = prepareRequest(id, attributes);
        auctionItemsProcessor.processAttributesForAuction(id, attributes);

        switch (bidOperationType) {
            case PARALLEL:
                return broadcastAuctionRequestInParallel(bidRequest);
            case SEQUENTIAL:
            default:
                return broadcastAuctionRequestSequentially(id, attributes);
        }
    }

    /*
     * Broadcast messages sequentially.
     */
    public BidResponse broadcastAuctionRequestSequentially(String id, Map<String, String> attributes) throws NoAvailableBidException {
        if (StringUtils.isEmpty(id) || attributes == null || attributes.isEmpty()) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }

        auctionItemsProcessor.processAttributesForAuction(id, attributes);
        BidRequest bidRequest = prepareRequest(id, attributes);
        AuctionHandler auctionHandler = new AuctionHandler(bidRequest.getId());
        webClientBidder.fetchBidsOneByOne(bidRequest, auctionHandler);
        return auctionHandler.getBestOffer();
    }

    /*
     * Broadcast messages in parallel.
     * If expected.result is considered this isn't requested.
     */
    public BidResponse broadcastAuctionRequestInParallel(final BidRequest bidRequest) throws NoAvailableBidException {
        if (!isBidRequestValid(bidRequest)) {
            throw new RuntimeException("BidRequest must be valid.");
        }

        log.info("START AUCTION");
        AuctionHandler auctionHandler = new AuctionHandler(bidRequest.getId());
        bidServers.stream().parallel().forEach(server -> {
            postAdBid(server, bidRequest, auctionHandler);
        });
        BidResponse bestOffer = auctionHandler.getBestOffer();
        log.info("END OF AUCTION.\n");
        return bestOffer;
    }


    public void postAdBid(String url, BidRequest bidRequest, Consumer<BidResponse> handleResponse) {
        if (StringUtils.isEmpty(url) || !isBidRequestValid(bidRequest)) {
            throw new RuntimeException("Url and request must be valid in order to send one bid request.");
        }

        log.info("Sending to server: {}", url);
        BidResponse response = webClientBidder.bidRequestBuilder(url, bidRequest);
        handleResponse.accept(response);
        log.info("Bid Response: id: {}, url: {}, response: {}", response.getContent(), url, response.getBid());
    }
}
