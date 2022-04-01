package ad.supplier.businesslogic;

import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.model.BidResponse;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author natalija
 */
@Data
@Log4j2
public class AuctionHandler implements Consumer<BidResponse> {
    String auctionId;
    List<BidResponse> bids = new ArrayList<>();

    public AuctionHandler(String auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public void accept(BidResponse bidResponse) {
        if (bidResponse != null) {
            bids.add(bidResponse);
        }
    }

    public BidResponse getBestOffer() throws NoAvailableBidException {
        if (CollectionUtils.isEmpty(bids)) {
            throw new NoAvailableBidException(auctionId);
        }
        BidResponse response = Collections.max(bids, new AdBidComparator());
        log.info("Best offer for auction {} is => {}.", auctionId, response.getBid());
        return response;
    }
}
