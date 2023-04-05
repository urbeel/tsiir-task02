package by.urbel.service.impl;

import by.urbel.dao.CartDao;
import by.urbel.dao.exception.DaoException;
import by.urbel.dao.impl.CartDaoImpl;
import by.urbel.entity.Cart;
import by.urbel.entity.Item;
import by.urbel.service.CartService;
import by.urbel.service.exception.ServiceException;

public class CartServiceImpl implements CartService {
    private final CartDao cartDao = new CartDaoImpl();

    @Override
    public void addProductToCart(Item item, long cartId) throws ServiceException {
        if (item == null) {
            throw new ServiceException("Cart item cannot be null");
        }
        if (item.getProduct().getId() == null) {
            throw new ServiceException("Product id in cart item cannot be null");
        }
        if (item.getQuantity() <= 0) {
            throw new ServiceException("Item quantity must be greater than 0");
        }
        if (item.getQuantity() > item.getProduct().getQuantity()) {
            throw new ServiceException("Not enough items in stock. Try with a different quantity.");
        }
        try {
            cartDao.addItemToCart(cartId, item);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void removeProductFromCart(Item item, long cartId) throws ServiceException {
        if (item == null) {
            throw new ServiceException("Cart item cannot be null");
        }
        if (item.getProduct().getId() == null) {
            throw new ServiceException("Product id in item cannot be null");
        }
        try {
            cartDao.removeItemFromCart(cartId, item);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Cart readById(long cartId) throws ServiceException {
        try {
            Cart cart = cartDao.readById(cartId);
            if (cart != null) {
                return cart;
            } else {
                throw new ServiceException(String.format("Cannot find cart with id %d", cartId));
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
