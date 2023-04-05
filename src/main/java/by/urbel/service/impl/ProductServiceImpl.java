package by.urbel.service.impl;

import by.urbel.dao.ProductDao;
import by.urbel.dao.exception.DaoException;
import by.urbel.dao.impl.ProductDaoImpl;
import by.urbel.entity.Product;
import by.urbel.service.ProductService;
import by.urbel.service.exception.ServiceException;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> readAll() throws ServiceException {
        try {
            return productDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> readInStock() throws ServiceException {
        try {
            return productDao.readAllInStock();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Product readById(long id) throws ServiceException {
        try {
            return productDao.readById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void save(Product product) throws ServiceException {
        validateProduct(product);
        try {
            productDao.create(product);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            productDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(Product product) throws ServiceException {
        try {
            productDao.update(product);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void changeProductQuantity(long productId, int quantity) throws ServiceException {
        if (quantity < 0) {
            throw new ServiceException("Quantity must be >= 0");
        }
        try {
            productDao.changeQuantity(productId, quantity);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void validateProduct(Product product) throws ServiceException {
        if (product == null) {
            throw new ServiceException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ServiceException("Product name cannot be null or empty");
        }
        if (product.getCategory() == null) {
            throw new ServiceException("Product category cannot be null");
        }
        if (product.getCategory().getId() == null) {
            throw new ServiceException("Product category id cannot be null");
        }
        if (product.getQuantity() < 0) {
            throw new ServiceException("Product quantity cannot be less than 0");
        }
        if (product.getPrice() < 0) {
            throw new ServiceException("Product price cannot be less than 0");
        }
    }
}
