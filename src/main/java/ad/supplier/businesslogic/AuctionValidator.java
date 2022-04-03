package ad.supplier.businesslogic;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author natalija
 */

/**
* Util for auction inputs/results validation.
 */
public class AuctionValidator {
    public static boolean isBidResponseValid(BidResponse bidResponse) {
        return bidResponse != null && StringUtils.isNotEmpty(bidResponse.getContent());
    }

    public static boolean isBidRequestValid(BidRequest bidRequest) {
        return bidRequest != null;
    }
}
