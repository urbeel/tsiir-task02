package by.urbel.service;

import by.urbel.entity.Cart;
import by.urbel.entity.Item;
import by.urbel.service.exception.ServiceException;

public interface CartService {
    void addProductToCart(Item item, long cartId) throws ServiceException;

    void removeProductFromCart(Item item, long cartId) throws ServiceException;

    Cart readById(long cartId) throws ServiceException;
}
