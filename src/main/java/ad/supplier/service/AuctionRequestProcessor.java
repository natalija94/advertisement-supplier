package ad.supplier.service;

import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;

import java.util.Map;

/**
 * @author natalija
 */

/**
 * Auction processor.
 */
public interface AuctionRequestProcessor {
    /**
     * Default request processor method.
     *
     * @param id         identifies auction. Path variable from request.
     * @param attributes params from request.
     * @return created BidRequest based on request.
     */
    default BidRequest prepareRequest(int id, Map<String, String> attributes) {
        return BidRequest.builder().id(id).attributes(attributes).build();
    }

    /**
     * Process request for auction. Sends request to bidders.
     *
     * @param id         identifies auction. Path variable from request.
     * @param attributes params from request.
     * @return best offer from Bidders.
     * @throws NoAvailableBidException if no available/proper offers.
     */
    default BidResponse processRequestForAuction(int id, Map<String, String> attributes) throws NoAvailableBidException {
        BidRequest bidRequest = prepareRequest(id, attributes);
        return processRequestForAuction(bidRequest);
    }

    /**
     * BidOperationType dependent processing.Sends request to bidders.
     *
     * @param bidRequest formatted request ready to be sent to bidders.
     * @return best offer from Bidders.
     * @throws NoAvailableBidException if no available/proper offers.
     */
    BidResponse processRequestForAuction(BidRequest bidRequest) throws NoAvailableBidException;
}
