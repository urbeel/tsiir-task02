package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.Cart;
import by.urbel.entity.Item;

public interface CartDao {
    void create(Cart cart) throws DaoException;

    Cart readById(long id) throws DaoException;

    void delete(long id) throws DaoException;

    void addItemToCart(long cartId, Item item) throws DaoException;

    void removeItemFromCart(long cartId, Item item) throws DaoException;
}
