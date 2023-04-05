package by.urbel.view;

import by.urbel.entity.*;
import by.urbel.entity.enums.Role;
import by.urbel.entity.enums.StatusName;
import by.urbel.service.*;
import by.urbel.service.exception.BadCredentialsException;
import by.urbel.service.exception.CartNeedUpdateException;
import by.urbel.service.exception.ServiceException;
import by.urbel.service.impl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu {
    private User user;
    private static final Menu INSTANCE = new Menu();
    private final UserService userService = new UserServiceImpl();
    private final ProductService productService = new ProductServiceImpl();
    private final CartService cartService = new CartServiceImpl();
    private final ProductCategoryService categoryService = new ProductCategoryServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    public static Menu getInstance() {
        return INSTANCE;
    }

    public void start() {
        while (true) {
            System.out.println(MenuConstants.START_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> {
                    login();
                    if (user != null) {
                        if (user.getRole().equals(Role.ADMIN)) {
                            startAdminMenu();
                        } else if (user.getRole().equals(Role.CUSTOMER)) {
                            startCustomerMenu();
                        }
                    }
                }
                case 2 -> register();
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void login() {
        System.out.print("Enter email: ");
        String email = ConsoleReader.readNextLine();
        System.out.print("Enter password: ");
        String password = ConsoleReader.readPassword();
        try {
            user = userService.authorize(email, password);
        } catch (ServiceException | BadCredentialsException e) {
            System.out.println(e.getMessage());
        }
    }

    private void register() {
        User tempUser = enterBaseUserInfo();
        System.out.print("Enter phone number: ");
        String phone = ConsoleReader.readNextLine();
        System.out.print("Enter address: ");
        String address = ConsoleReader.readNextLine();
        tempUser.setRole(Role.CUSTOMER);
        tempUser.setPhone(phone);
        tempUser.setAddress(address);
        try {
            userService.register(tempUser);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private User enterBaseUserInfo() {
        System.out.print("Enter email: ");
        String email = ConsoleReader.readNextLine();
        System.out.print("Enter password: ");
        String password = ConsoleReader.readNextLine();
        System.out.print("Repeat password: ");
        String confirmPassword = ConsoleReader.readNextLine();
        while (!password.equals(confirmPassword)) {
            System.out.print("Passwords mismatch: ");
            System.out.print("Repeat password: ");
            confirmPassword = ConsoleReader.readNextLine();
        }
        System.out.print("Enter firstname: ");
        String firstname = ConsoleReader.readNextLine();
        System.out.print("Enter lastname: ");
        String lastname = ConsoleReader.readNextLine();

        User tempUser = new User();
        tempUser.setFirstname(firstname);
        tempUser.setLastname(lastname);
        tempUser.setEmail(email);
        tempUser.setPassword(password);
        return tempUser;
    }

    private void startAdminMenu() {
        while (true) {
            System.out.println(MenuConstants.ADMIN_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> startAdminProductMenu();
                case 2 -> startProductCategoryMenu();
                case 3 -> startOrdersMenu();
                case 4 -> startUsersMenu();
                case 0 -> {
                    user = null;
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void startCustomerMenu() {
        while (true) {
            System.out.println(MenuConstants.CUSTOMER_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> startCustomerProductMenu();
                case 2 -> startCartMenu();
                case 0 -> {
                    user = null;
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private List<Product> showAllProducts() {
        try {
            List<Product> products;
            if (user.getRole().equals(Role.ADMIN)) {
                products = productService.readAll();
            } else {
                products = productService.readInStock();
            }
            if (products.isEmpty()) {
                System.out.println("No products...");
            } else {
                System.out.println("ID;  NAME;  CATEGORY;  QUANTITY;  PRICE");
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
                products.forEach(product -> System.out.printf("%d; %s; %s; %d; %10.2f$ %n",
                        product.getId(),
                        product.getName(),
                        product.getCategory().getName(),
                        product.getQuantity(),
                        product.getPrice() / 100d
                ));
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
            }
            return products;
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    private void startCustomerProductMenu() {
        List<Product> products = showAllProducts();
        if (products.isEmpty()) {
            return;
        }
        while (true) {
            System.out.println(MenuConstants.CUSTOMER_PRODUCT_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> addProductToCart(products);
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void startCartMenu() {
        Cart cart = showAllCartItems();
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return;
        }
        while (true) {
            System.out.println(MenuConstants.CART_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> {
                    try {
                        boolean isOrderCreated = createOrder(cart.getId());
                        if (isOrderCreated) {
                            return;
                        }
                    } catch (CartNeedUpdateException e) {
                        System.out.println("Order rejected. " +
                                "The quantity of products in the cart has been changed based on stock. " +
                                "Check your cart and try again.");
                        return;
                    }
                }
                case 2 -> removeProductFromCart(cart);
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private Cart showAllCartItems() {
        try {
            Cart cart = cartService.readById(user.getCartId());
            List<Item> items = cart.getItems();
            if (items.isEmpty()) {
                System.out.println("No products in cart...");
            } else {
                System.out.println("ID;  NAME;  QUANTITY;  PRICE");
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
                items.forEach(item -> System.out.printf("%d; %s; %d; %10.2f$ %n",
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice() / 100d
                ));
                System.out.printf("Total price: %10.2f%n", cart.getTotalPrice() / 100d);
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
            }
            return cart;
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void addProductToCart(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        System.out.print("Enter quantity: ");
        int quantity = ConsoleReader.readInt();
        Optional<Product> productOptional = products.stream().filter(p -> p.getId().equals(productId)).findFirst();
        if (productOptional.isPresent()) {
            try {
                Item item = new Item(productOptional.get(), quantity);
                cartService.addProductToCart(item, user.getCartId());
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid product id");
        }
    }

    private boolean createOrder(long cartId) throws CartNeedUpdateException {
        try {
            Cart cart = cartService.readById(cartId);
            Order order = new Order();
            order.setUser(user);
            order.setItems(cart.getItems());
            order.setTotalPrice(cart.getTotalPrice());
            orderService.create(order);
            System.out.println("Order successfully created! Wait for a call");
            return true;
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void removeProductFromCart(Cart cart) {
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        Optional<Item> itemOptional = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (itemOptional.isPresent()) {
            try {
                cartService.removeProductFromCart(itemOptional.get(), cart.getId());
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid product id");
        }
    }

    private void startAdminProductMenu() {
        while (true) {
            System.out.println(MenuConstants.ADMIN_PRODUCT_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> showAllProducts();
                case 3 -> showFullProductInfo();
                case 4 -> deleteProduct();
                case 5 -> updateProduct();
                case 6 -> updateProductQuantity();
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void addProduct() {
        Product product = enterProductData(null);
        if (product != null) {
            try {
                productService.save(product);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showFullProductInfo() {
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        try {
            Product product = productService.readById(productId);
            if (product == null) {
                System.out.println("Product not found.");
            } else {
                System.out.printf("Id: %d%n", product.getId());
                System.out.printf("Name: %s%n", product.getName());
                System.out.printf("Category: %s%n", product.getCategory().getName());
                System.out.printf("Description: %s%n", product.getDescription());
                System.out.printf("Quantity: %d%n", product.getQuantity());
                System.out.printf("Price: %10.2f $%n", product.getPrice() / 100d);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private Product enterProductData(Long productId) {
        List<ProductCategory> categories = showAllProductCategories();
        if (categories.isEmpty()) {
            System.out.println("Add product category first.");
            return null;
        }
        System.out.print(MenuConstants.ENTER_CATEGORY_ID_MESSAGE);
        long categoryId = ConsoleReader.readLong();
        System.out.print("Enter product name: ");
        String name = ConsoleReader.readNextLine();
        System.out.print("Enter product description: ");
        String description = ConsoleReader.readNextLine();
        System.out.print("Enter product quantity: ");
        int quantity = ConsoleReader.readInt();
        System.out.print("Enter product price in cents: ");
        long price = ConsoleReader.readLong();
        Optional<ProductCategory> categoryOptional = categories.stream()
                .filter(category -> category.getId().equals(categoryId)).findFirst();
        if (categoryOptional.isEmpty()) {
            System.out.println("Invalid product category id.");
            return null;
        }
        Product product = new Product();
        if (productId != null) {
            product.setId(productId);
        }
        product.setName(name);
        product.setCategory(categoryOptional.get());
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setPrice(price);
        return product;
    }

    private void deleteProduct() {
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        try {
            productService.delete(productId);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        Product product = enterProductData(productId);
        if (product != null) {
            try {
                productService.update(product);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateProductQuantity() {
        System.out.print(MenuConstants.ENTER_PRODUCT_ID_MESSAGE);
        long productId = ConsoleReader.readLong();
        System.out.print("Enter new product quantity: ");
        int quantity = ConsoleReader.readInt();
        try {
            productService.changeProductQuantity(productId, quantity);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startProductCategoryMenu() {
        while (true) {
            showAllProductCategories();
            System.out.println(MenuConstants.PRODUCT_CATEGORY_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> addProductCategory();
                case 2 -> removeProductCategory();
                case 3 -> updateProductCategory();
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private List<ProductCategory> showAllProductCategories() {
        try {
            List<ProductCategory> categories = categoryService.readAll();
            if (categories.isEmpty()) {
                System.out.println("No product categories");
            } else {
                System.out.println("ID;  NAME");
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
                categories.forEach(category -> System.out.printf("%d; %s%n",
                        category.getId(),
                        category.getName()
                ));
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
            }
            return categories;
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    private void addProductCategory() {
        System.out.print("Enter category name: ");
        String categoryName = ConsoleReader.readNextLine();
        ProductCategory productCategory = new ProductCategory(categoryName);
        try {
            categoryService.create(productCategory);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeProductCategory() {
        System.out.print(MenuConstants.ENTER_CATEGORY_ID_MESSAGE);
        long categoryId = ConsoleReader.readLong();
        try {
            categoryService.delete(categoryId);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProductCategory() {
        System.out.print(MenuConstants.ENTER_CATEGORY_ID_MESSAGE);
        long categoryId = ConsoleReader.readLong();
        System.out.print("Enter new category name: ");
        String categoryName = ConsoleReader.readNextLine();
        ProductCategory productCategory = new ProductCategory(categoryId, categoryName);
        try {
            categoryService.update(productCategory);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startOrdersMenu() {
        while (true) {
            System.out.println(MenuConstants.ORDERS_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> showOrdersByStatus(StatusName.NEW);
                case 2 -> showOrdersByStatus(StatusName.ACCEPTED);
                case 3 -> showOrdersByStatus(StatusName.FINISHED);
                case 4 -> showOrdersByStatus(StatusName.CANCELED);
                case 5 -> changeOrderStatus(StatusName.ACCEPTED);
                case 6 -> changeOrderStatus(StatusName.FINISHED);
                case 7 -> changeOrderStatus(StatusName.CANCELED);
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void showOrdersByStatus(StatusName statusName) {
        try {
            List<Order> orders = orderService.readOrdersByStatusName(statusName);
            if (orders.isEmpty()) {
                System.out.println("No orders.");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy HH:mm");
                System.out.println("ID; CUSTOMER EMAIL; CUSTOMER PHONE; CUSTOMER ADDRESS; ORDER TIME;  TOTAL PRICE;");
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
                orders.forEach(order -> System.out.printf("%d; %s; %s; %s; %s; %10.2f;%n",
                        order.getId(),
                        order.getUser().getEmail(),
                        order.getUser().getPhone(),
                        order.getUser().getAddress(),
                        sdf.format(order.getOrderTime()),
                        order.getTotalPrice() / 100d
                ));
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeOrderStatus(StatusName statusName) {
        try {
            System.out.print("Enter order id: ");
            long orderId = ConsoleReader.readLong();
            orderService.changeOrderStatus(orderId, statusName);
            System.out.printf("Status changed to %s%n", statusName.getName());
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startUsersMenu() {
        while (true) {
            System.out.println(MenuConstants.USERS_MENU);
            int choice = ConsoleReader.readInt();
            switch (choice) {
                case 1 -> createAdmin();
                case 2 -> showAllUsers();
                case 3 -> deleteUser();
                case 0 -> {
                    return;
                }
                default -> System.out.println(MenuConstants.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void createAdmin() {
        User tempUser = enterBaseUserInfo();
        tempUser.setRole(Role.ADMIN);
        try {
            userService.register(tempUser);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAllUsers() {
        try {
            List<User> users = userService.readAll();
            if (users.isEmpty()) {
                System.out.println("No users");
            } else {
                System.out.println("ID;  EMAIL;  FIRSTNAME;  LASTNAME;  PHONE;  ADDRESS");
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
                users.forEach(innerUser -> System.out.printf("%d; %s; %s; %s; %s; %s;%n",
                        innerUser.getId(),
                        innerUser.getEmail(),
                        innerUser.getFirstname(),
                        innerUser.getLastname(),
                        innerUser.getPhone() != null ? innerUser.getPhone() : "N/A",
                        innerUser.getAddress() != null ? innerUser.getAddress() : "N/A"
                ));
                System.out.println(MenuConstants.HORIZONTAL_ROW_MESSAGE);
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("Enter user id: ");
        long userId = ConsoleReader.readLong();
        try {
            userService.delete(userId);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}
