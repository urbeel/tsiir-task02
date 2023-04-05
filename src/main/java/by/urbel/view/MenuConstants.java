package by.urbel.view;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuConstants {
    public static final String START_MENU = """
            Choose:
            1.Login
            2.Sign up
            0.Exit
            """;
    public static final String ADMIN_MENU = """
            Choose:
            1.Products
            2.Product category
            3.Orders
            4.Users
            0.Logout
            """;
    public static final String CUSTOMER_MENU = """
            Choose:
            1.Products
            2.Cart
            0.Logout
            """;
    public static final String ORDERS_MENU = """
            Choose:
            1.Show new orders
            2.Show accepted orders
            3.Show finished orders
            4.Show canceled orders
            5.Accept order
            6.Finish order
            7.Cancel order
            0.Exit
            """;

    public static final String CUSTOMER_PRODUCT_MENU = """
            Choose:
            1.Add product to cart
            0.Exit
            """;

    public static final String CART_MENU = """
            Choose:
            1.Order
            2.Delete product from cart
            0.Exit
            """;

    public static final String PRODUCT_CATEGORY_MENU = """
            Choose:
            1.Add category
            2.Delete category
            3.Update category
            0.Exit
            """;
    public static final String ADMIN_PRODUCT_MENU = """
            Choose:
            1.Add product
            2.Show all products
            3.Show full product info by product id
            4.Delete product
            5.Update product
            6.Change product quantity
            0.Exit
            """;
    public static final String USERS_MENU = """
            Choose:
            1.Create admin
            2.Show all users
            3.Delete user
            0.Exit
            """;
    public static final String INVALID_CHOICE_MESSAGE = "Invalid number! Try again.";
    public static final String ENTER_PRODUCT_ID_MESSAGE = "Enter product id: ";
    public static final String ENTER_CATEGORY_ID_MESSAGE = "Enter product category id: ";
    public static final String HORIZONTAL_ROW_MESSAGE = "--------------------------------------------";

}
