package ad.supplier.exception;

/**
 * @author natalija
 */

/**
 * Thrown if no available bid found for initial request.
 */
public class NoAvailableBidException extends Exception{
    public NoAvailableBidException(String auctionId) {
        super(String.format("There are no available bids for auctionId: %s", auctionId));
    }

    public NoAvailableBidException(String auctionId, Throwable cause) {
        super(auctionId, cause);
    }
}
