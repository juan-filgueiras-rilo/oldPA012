package es.udc.paproject.backend.model.services;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

import es.udc.paproject.backend.model.common.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.Bid;
import es.udc.paproject.backend.model.entities.Bid.BidState;
import es.udc.paproject.backend.model.entities.BidDao;
import es.udc.paproject.backend.model.entities.Product;
import es.udc.paproject.backend.model.entities.ProductDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;

public class BidServiceImpl implements BidService {

	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BidDao bidDao;
	
	@Override
	public Bid createBid(Long userId, Long productId, BigDecimal quantity)
			throws ExpiratedProductDateException, InstanceNotFoundException {
		
		User user = permissionChecker.checkUser(userId);		
		Optional<Product> optProduct;
		Product product;
		Bid newBid, winningBid;
		
		optProduct = productDao.findById(productId);
		try {
			product = optProduct.get();
		} catch (NoSuchElementException e) {
			throw new InstanceNotFoundException("Product not found with id: ", productId);
		}
		
		if (!product.isActive()) {
			throw new ExpiratedProductDateException(productId);
		}
		
		if(product.getUser() == user) {
			//throw new 
		}

		newBid = new Bid(quantity, BidState.WINNING, user, product);

		Optional<Bid> optWinningBid = product.getWinningBid();
		if (optWinningBid.isPresent()) {
			
			winningBid = optWinningBid.get();
			
			BigDecimal productCurrentPrice = product.getCurrentPrice();
			BigDecimal winningQuantity = winningBid.getQuantity();
			BigDecimal newQuantity = newBid.getQuantity();
			
			if (newQuantity.max(productCurrentPrice) != null) {
				if (newQuantity.max(winningQuantity) != null) { 
					winningBid.setState(BidState.LOST);
					product.setWinningBid(newBid);
					if(newQuantity.max(winningQuantity.add(new BigDecimal(0.5))) != null) {
						product.setCurrentPrice(winningQuantity.add(new BigDecimal(0.5)));
					} else {
						product.setCurrentPrice(newQuantity);
					}
				} else {
					newBid.setState(BidState.LOST);
					if(newQuantity == productCurrentPrice) {
						productCurrentPrice = newBid.getQuantity();
					} else {
						productCurrentPrice = newBid.getQuantity().add(new BigDecimal(0.5));
					}
				}
			} else {
				//EXCEPCIÓ
			}
		} else {
			product.setWinningBid(newBid);
		}
		
		//SI NO HAY PUJAS - SE COMPRUEBA CON EL PRECIO MINIMO?

		//Update se hace solo.
	
		//if (newBid.getState()==BidState.WINNING || (newBid.getState()==BidState.LOST && newBid.getQuantity()>product.getCurrentPrice())) {
		bidDao.save(newBid);
		//}
		//Cambiar estado bids y buscar la bid que va ganando para poner que ha perdido 
		/*Comparamos con el precio inicial que sea mayor
		 * Un señor no puede pujar sobre si mismo
		 * Fecha expirada que ya la tenemos*/
		
		//tiempo que dura la puja pero cuando empieza/acaba?
		//version en prod
		//que puja gana? como era esto? lo tengo anotado estado den puja
		
		return newBid;
	}

	@Override
	public Block<Bid> getUserBids(Long userId) throws InstanceNotFoundException {

		permissionChecker.checkUserExists(userId);
		
		Slice<Bid> slice = bidDao.findBidsByUserId(userId);
		
		return new Block<>(slice.getContent(), slice.hasNext());
	}

}
