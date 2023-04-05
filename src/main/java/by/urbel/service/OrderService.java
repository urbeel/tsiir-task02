package by.urbel.service;

import by.urbel.entity.Order;
import by.urbel.entity.enums.StatusName;
import by.urbel.service.exception.CartNeedUpdateException;
import by.urbel.service.exception.ServiceException;

import java.util.List;

public interface OrderService {
    void create(Order order) throws ServiceException, CartNeedUpdateException;

    List<Order> readOrdersByStatusName(StatusName statusName) throws ServiceException;

    void changeOrderStatus(long orderId, StatusName statusName) throws ServiceException;
}
