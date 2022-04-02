package ad.supplier;

import ad.supplier.enums.BidOperationType;
import ad.supplier.service.AuctionRequestProcessor;
import ad.supplier.service.impl.AuctionRequestProcessorParallel;
import ad.supplier.service.impl.AuctionRequestProcessorSequential;
import ad.supplier.service.impl.WebClientBidder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author natalija
 */
@Configuration
@EnableAsync
@ComponentScan("ad.supplier")
public class ChatConfiguration {
    @Value("${bidOperationType:SEQUENTIAL}")
    BidOperationType bidOperationType;

    @Autowired
    WebClientBidder webClientBidder;

    /**
     * Bean creation based on configuration param: bidOperationType.
     *
     * @return expected processor.
     */
    @Bean
    AuctionRequestProcessor auctionRequestProcessor() {
        switch (bidOperationType) {
            case PARALLEL:
                return new AuctionRequestProcessorParallel(webClientBidder);
            case SEQUENTIAL:
            default:
                return new AuctionRequestProcessorSequential(webClientBidder);
        }

    }
}
