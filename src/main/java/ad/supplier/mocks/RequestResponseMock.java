package ad.supplier.mocks;

import ad.supplier.model.BidRequest;
import ad.supplier.model.BidResponse;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author natalija
 */

/**
 * Mocks provider, purpose process of development.
 */
@Log4j2
public class RequestResponseMock {

    public static BidResponse getMockResponse(BidRequest bidRequest) {
        double price = getPriceForAuction();
        BidResponse response = BidResponse.builder()
                .id(bidRequest.getId())
                .bid(price)
                .content(String.format("%s:%s", bidRequest.getId(), price))
                .build();
        log.info("a complete response from server => {}", response);
        log.info("END.");
        return response;
    }

    public static BidResponse getMockResponse() {
        double price = getPriceForAuction();
        BidResponse response = BidResponse.builder()
                .id(123)
                .bid(price)
                .content(String.format("%s:%s", 123, price))
                .build();
        log.info("MOCK RETURNED");
        return response;
    }

    public static double getPriceForAuction() {
        BigDecimal randFromDouble = new BigDecimal(1000 * Math.random());
        return randFromDouble.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
