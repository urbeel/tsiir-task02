package by.urbel.service;

import by.urbel.service.exception.ServiceException;

public interface OrderStatusService {
    void initializeStatuses() throws ServiceException;
}
