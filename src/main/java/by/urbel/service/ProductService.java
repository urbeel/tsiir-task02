package by.urbel.service;

import by.urbel.entity.Product;
import by.urbel.service.exception.ServiceException;

import java.util.List;

public interface ProductService {
    List<Product> readAll() throws ServiceException;

    List<Product> readInStock() throws ServiceException;

    Product readById(long id) throws ServiceException;

    void save(Product product) throws ServiceException;

    void delete(long id) throws ServiceException;

    void update(Product product) throws ServiceException;

    void changeProductQuantity(long productId, int quantity) throws ServiceException;
}
