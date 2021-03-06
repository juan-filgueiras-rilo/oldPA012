package es.udc.paproject.backend.model.services;

import java.math.BigDecimal;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Bid;

public interface AuctionService {
	
	Bid createBid(Long id, Long productId, BigDecimal quantity) 
			throws ExpiratedProductDateException, InstanceNotFoundException,
			UnauthorizedBidException, InsufficientBidQuantityException,
			UnauthorizedWinningUserException;
	
	Block<Bid> getUserBids(Long userId, int page, int size) throws InstanceNotFoundException;
}
