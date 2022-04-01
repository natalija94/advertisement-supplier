package ad.supplier.service;

import ad.supplier.businesslogic.AuctionHandler;
import ad.supplier.businesslogic.AuctionItemsProcessor;
import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

/**
 * @author natalija
 */
@Service
@Log4j2
public class AuctionRequestProcessor {

    @Value("#{'${bidders}'.split(',')}")
    private HashSet<String> bidServers;

    public final WebClientBidder webClientBidder;
    public final AuctionItemsProcessor auctionItemsProcessor;

    public AuctionRequestProcessor(WebClientBidder webClientBidder, AuctionItemsProcessor auctionItemsProcessor) {
        this.webClientBidder = webClientBidder;
        this.auctionItemsProcessor = auctionItemsProcessor;
    }

    public BidRequest prepareRequest(String id, Map<String, String> attributes) {
        return BidRequest.builder()
                .id(id)
                .attributes(attributes)
                .build();
    }

    public BidResponse processRequestForAuction(String id, Map<String, String> attributes) throws NoAvailableBidException {
        auctionItemsProcessor.processAttributesForAuction(id, attributes);
        BidRequest bidRequest = prepareRequest(id, attributes);
        return broadcastAuctionRequestInParallelStream(bidRequest);
    }

    public BidResponse broadcastAuctionRequestInParallelStream(final BidRequest bidRequest) throws NoAvailableBidException {
        log.info("START AUCTION");
        AuctionHandler auctionHandler = new AuctionHandler(bidRequest.getId());
        bidServers.stream().parallel().forEach(server -> {
            webClientBidder.postAdBid(server, bidRequest, auctionHandler);
        });
        BidResponse bestOffer = auctionHandler.getBestOffer();
        log.info("END OF AUCTION.\n");
        return bestOffer;
    }
}
