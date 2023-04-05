package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.User;

public interface UserDao extends Dao<User, Long> {
    User readByEmail(String email) throws DaoException;
}
