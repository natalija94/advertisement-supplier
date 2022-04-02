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

import static ad.supplier.businesslogic.AuctionValidator.isBidResponseValid;
import static ad.supplier.businesslogic.Constants.PRICE_ATTRIBUTE_CONST;

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
        if (!isBidResponseValid(bidResponse)) {
            throw new RuntimeException("BidResponse must be specified in order to be considered as one the bids.");
        }
        bids.add(formatBestOffer(bidResponse));
    }

    public BidResponse getBestOffer() throws NoAvailableBidException {
        if (CollectionUtils.isEmpty(bids)) {
            throw new NoAvailableBidException(auctionId);
        }
        BidResponse formattedOffer = formatBestOffer(Collections.max(bids, new AdBidComparator()));
        log.info("Best offer for auction {} is => {}.", auctionId, formattedOffer.getContent());
        return formattedOffer;
    }

    public BidResponse formatBestOffer(BidResponse response) {
        if (!isBidResponseValid(response)) {
            throw new IllegalArgumentException("Response must be specified in order to be formatted.");
        }

        if (response.getContent().contains(PRICE_ATTRIBUTE_CONST)) {
            response.setContent(response.getContent().replace(PRICE_ATTRIBUTE_CONST, String.format("%.0f", response.getBid())));
        }

        return response;
    }
}
