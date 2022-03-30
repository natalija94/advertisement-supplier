package ad.supplier.controller;

import ad.supplier.service.AuctionRequestProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author natalija
 */
@RestController
@Slf4j
@RequestMapping("bid")
public class BidderController {
    final AuctionRequestProcessor auctionRequestProcessor;

    public BidderController(AuctionRequestProcessor auctionRequestProcessor) {
        this.auctionRequestProcessor = auctionRequestProcessor;
    }

    @GetMapping("/{id}")
    Object getTheBestAdOffer(@PathVariable String id, @RequestParam Map<String, String> allParams) {
        auctionRequestProcessor.processRequestForAuction(id, allParams);
        return "success";
    }
}
