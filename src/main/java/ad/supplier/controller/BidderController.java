package ad.supplier.controller;

import ad.supplier.service.AuctionRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author natalija
 */
@RestController
@Slf4j
public class BidderController {
    final AuctionRequestProcessor auctionRequestProcessor;

    public BidderController(AuctionRequestProcessor auctionRequestProcessor) {
        this.auctionRequestProcessor = auctionRequestProcessor;
    }

    @GetMapping("/{id}")
    String getTheBestAdOffer(@PathVariable String id, @RequestParam Map<String, String> allParams) {
        auctionRequestProcessor.processRequestForAuction(id, allParams);
        return "a:55";
    }
}
