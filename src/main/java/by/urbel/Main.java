package by.urbel;

import by.urbel.service.OrderStatusService;
import by.urbel.service.RoleService;
import by.urbel.service.UserService;
import by.urbel.service.exception.ServiceException;
import by.urbel.service.impl.OrderStatusServiceImpl;
import by.urbel.service.impl.RoleServiceImpl;
import by.urbel.service.impl.UserServiceImpl;
import by.urbel.view.Menu;

public class Main {
    public static void main(String[] args) {
        RoleService roleService = new RoleServiceImpl();
        OrderStatusService statusService = new OrderStatusServiceImpl();
        UserService userService = new UserServiceImpl();
        try {
            roleService.initializeRoles();
            statusService.initializeStatuses();
            userService.createInitialAdmin();
        } catch (ServiceException e) {
            System.out.println("Error while starting.");
            return;
        }
        Menu menu = Menu.getInstance();
        menu.start();
    }
}