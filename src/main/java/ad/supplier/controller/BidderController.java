package ad.supplier.controller;

import ad.supplier.exception.NoAvailableBidException;
import ad.supplier.service.AuctionRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            return String.format("%s:%s", id, auctionRequestProcessor.processRequestForAuction(id, allParams).getBid());
        } catch (NoAvailableBidException e) {
            throw new ResponseStatusException(HttpStatus.SEE_OTHER, String.format("There are no available bids for auction: %s", id), e);
        }
    }
}
