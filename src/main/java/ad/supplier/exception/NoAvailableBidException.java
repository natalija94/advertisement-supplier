package ad.supplier.exception;

/**
 * @author natalija
 */
public class NoAvailableBidException extends Exception{
    public NoAvailableBidException(String auctionId) {
        super(String.format("There are no available bids for auctionId: %s", auctionId));
    }

    public NoAvailableBidException(String auctionId, Throwable cause) {
        super(auctionId, cause);
    }
}
