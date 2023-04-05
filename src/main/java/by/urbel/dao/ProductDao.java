package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.Product;

import java.util.List;

public interface ProductDao extends Dao<Product, Long> {
    List<Product> readAllInStock() throws DaoException;

    void changeQuantity(long productId, int quantity) throws DaoException;
}
