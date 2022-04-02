package ad.supplier.service;

import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author natalija
 */
public interface AuctionRequestProcessor {

    default BidRequest prepareRequest(String id, Map<String, String> attributes) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }
        return BidRequest.builder().id(id).attributes(attributes).build();
    }

    default BidResponse processRequestForAuction(String id, Map<String, String> attributes) throws NoAvailableBidException{
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("BidRequest must have specified id and attributes.");
        }

        BidRequest bidRequest = prepareRequest(id, attributes);
        return processRequestForAuction(bidRequest);
    }

    BidResponse processRequestForAuction(BidRequest bidRequest) throws NoAvailableBidException;
}
