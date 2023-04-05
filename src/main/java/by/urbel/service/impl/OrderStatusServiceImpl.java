package by.urbel.service.impl;

import by.urbel.dao.OrderStatusDao;
import by.urbel.dao.exception.DaoException;
import by.urbel.dao.impl.OrderStatusDaoImpl;
import by.urbel.entity.OrderStatus;
import by.urbel.entity.enums.StatusName;
import by.urbel.service.OrderStatusService;
import by.urbel.service.exception.ServiceException;

public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusDao statusDao = new OrderStatusDaoImpl();

    @Override
    public void initializeStatuses() throws ServiceException {
        for (StatusName statusName : StatusName.values()) {
            try {
                if (statusDao.readByStatusName(statusName.getName()) == null) {
                    statusDao.create(new OrderStatus(null, statusName.getName()));
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
    }
}
