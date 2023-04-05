package by.urbel.service;

import by.urbel.entity.ProductCategory;
import by.urbel.service.exception.ServiceException;

import java.util.List;

public interface ProductCategoryService {
    void create(ProductCategory category) throws ServiceException;

    List<ProductCategory> readAll() throws ServiceException;

    void update(ProductCategory category) throws ServiceException;

    void delete(long id) throws ServiceException;

}
