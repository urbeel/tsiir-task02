package by.urbel.service;

import by.urbel.entity.User;
import by.urbel.service.exception.BadCredentialsException;
import by.urbel.service.exception.ServiceException;

import java.util.List;

public interface UserService {
    void register(User user) throws ServiceException;

    void createInitialAdmin() throws ServiceException;

    User authorize(String email, String password) throws ServiceException, BadCredentialsException;

    List<User> readAll() throws ServiceException;

    void delete(long id) throws ServiceException;
}
