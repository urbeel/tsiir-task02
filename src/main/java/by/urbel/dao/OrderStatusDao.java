package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.OrderStatus;

public interface OrderStatusDao extends Dao<OrderStatus, Long> {
    OrderStatus readByStatusName(String statusName) throws DaoException;
}
