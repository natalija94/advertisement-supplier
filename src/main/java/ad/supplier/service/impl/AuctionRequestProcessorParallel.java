package ad.supplier.service.impl;

import ad.supplier.businesslogic.AuctionHandler;
import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import ad.supplier.service.AuctionRequestProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.TreeSet;
import java.util.function.Consumer;

import static ad.supplier.businesslogic.AuctionValidator.isBidRequestValid;

/**
 * @author natalija
 */
@Log4j2
public class AuctionRequestProcessorParallel implements AuctionRequestProcessor {

    public final WebClientBidder webClientBidder;

    @Value("#{'${bidders}'.split(',')}")
    private TreeSet<String> bidServers;

    public AuctionRequestProcessorParallel(WebClientBidder webClientBidder) {
        this.webClientBidder = webClientBidder;
    }

    /**
     * Broadcast messages in parallel.
     * If initial expected.result is considered this isn't requested.
     *
     * @param bidRequest bid request to be sent.
     * @return best offer from Bidders.
     * @throws NoAvailableBidException if no available/proper offers.
     */
    @Override
    public BidResponse processRequestForAuction(BidRequest bidRequest) throws NoAvailableBidException {
        if (!isBidRequestValid(bidRequest)) {
            throw new RuntimeException("BidRequest must be valid.");
        }
        log.debug("START AUCTION");
        AuctionHandler auctionHandler = new AuctionHandler(bidRequest.getId());
        bidServers.stream().parallel().forEach(server -> {
            postAdBid(server, bidRequest, auctionHandler);
        });
        BidResponse bestOffer = auctionHandler.getBestOffer();
        log.debug("END OF AUCTION.\n");
        return bestOffer;
    }

    private void postAdBid(String url, BidRequest bidRequest, Consumer<BidResponse> handleResponse) {
        if (StringUtils.isEmpty(url) || !isBidRequestValid(bidRequest)) {
            throw new RuntimeException("Url and request must be valid in order to send one bid request.");
        }
        log.info("Sending to server: {}", url);
        BidResponse response = webClientBidder.bidRequestBuilder(url, bidRequest);
        handleResponse.accept(response);
        log.info("Bid Response: id: {}, url: {}, response: {}", response.getContent(), url, response.getBid());
    }
}
