package by.urbel.service.impl;

import by.urbel.dao.RoleDao;
import by.urbel.dao.exception.DaoException;
import by.urbel.dao.impl.RoleDaoImpl;
import by.urbel.entity.enums.Role;
import by.urbel.service.RoleService;
import by.urbel.service.exception.ServiceException;

public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao = new RoleDaoImpl();

    @Override
    public void initializeRoles() throws ServiceException {
        for (Role role : Role.values()) {
            try {
                if (roleDao.readRoleIdByName(role.name()) == null) {
                    roleDao.create(role);
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
    }
}
