package by.urbel.dao;

import by.urbel.dao.exception.DaoException;
import by.urbel.entity.enums.Role;

public interface RoleDao {
    void create(Role role) throws DaoException;

    Long readRoleIdByName(String name) throws DaoException;
}
