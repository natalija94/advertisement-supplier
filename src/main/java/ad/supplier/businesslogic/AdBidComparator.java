package ad.supplier.businesslogic;

import ad.supplier.model.BidResponse;

import java.util.Comparator;

/**
 * @author natalija
 */
public class AdBidComparator implements Comparator<BidResponse> {
    @Override
    public int compare(BidResponse response1, BidResponse response2) {
        if (response1 == null || response1 == null) {
            throw new IllegalArgumentException("In order to compare bids for auction bids must be specified.");
        }
        return Double.compare(response1.getBid(), response2.getBid());
    }
}
