package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.Order;

import java.util.List;

public interface OrderDao {
    void create(Order order) throws DaoException;

    void update(Order order) throws DaoException;

    void delete(long id) throws DaoException;

    List<Order> readOrdersByStatusName(String statusName) throws DaoException;

    void changeOrderStatus(long orderId, String statusName) throws DaoException;
}
