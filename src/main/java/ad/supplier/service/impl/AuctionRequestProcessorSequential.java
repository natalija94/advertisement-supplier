package ad.supplier.service.impl;

import ad.supplier.businesslogic.AuctionHandler;
import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import ad.supplier.service.AuctionRequestProcessor;
import lombok.extern.log4j.Log4j2;

import static ad.supplier.businesslogic.AuctionValidator.isBidRequestValid;

/**
 * @author natalija
 */
@Log4j2
public class AuctionRequestProcessorSequential implements AuctionRequestProcessor {
    public final WebClientBidder webClientBidder;

    public AuctionRequestProcessorSequential(WebClientBidder webClientBidder) {
        this.webClientBidder = webClientBidder;
    }

    @Override
    public BidResponse processRequestForAuction(BidRequest bidRequest) throws NoAvailableBidException {
        if (!isBidRequestValid(bidRequest)) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }
        return broadcastAuctionRequestSequentially(bidRequest);
    }

    /*
     * Broadcast messages sequentially.
     */
    public BidResponse broadcastAuctionRequestSequentially(BidRequest bidRequest) throws NoAvailableBidException {
        if (!isBidRequestValid(bidRequest)) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }
        AuctionHandler auctionHandler = new AuctionHandler(bidRequest.getId());
        webClientBidder.fetchBidsOneByOne(bidRequest, auctionHandler);
        return auctionHandler.getBestOffer();
    }
}
