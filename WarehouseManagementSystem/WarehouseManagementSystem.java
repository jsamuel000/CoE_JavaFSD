import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Product {
    private String productID;
    private String name;
    private int quantity;
    private Location location;

    public Product(String productID, String name, int quantity, Location location) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return name + " (ID: " + productID + ") - Quantity: " + quantity + " @ " + location;
    }
}

class Location {
    private int aisle;
    private int shelf;
    private int bin;

    public Location(int aisle, int shelf, int bin) {
        this.aisle = aisle;
        this.shelf = shelf;
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "Aisle " + aisle + ", Shelf " + shelf + ", Bin " + bin;
    }
}

class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}

class Order implements Comparable<Order> {
    private String orderID;
    private List<String> productIDs;
    private Priority priority;

    public enum Priority {
        STANDARD, EXPEDITED
    }

    public Order(String orderID, List<String> productIDs, Priority priority) {
        this.orderID = orderID;
        this.productIDs = productIDs;
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<String> getProductIDs() {
        return productIDs;
    }

    @Override
    public int compareTo(Order other) {
        return this.priority.compareTo(other.priority);
    }

    @Override
    public String toString() {
        return "Order ID: " + orderID + ", Priority: " + priority;
    }
}

class InventoryManager {
    private ConcurrentHashMap<String, Product> products;
    private PriorityQueue<Order> orderQueue;
    private ExecutorService executor;

    public InventoryManager() {
        products = new ConcurrentHashMap<>();
        orderQueue = new PriorityQueue<>();
        executor = Executors.newFixedThreadPool(3);
    }

    public synchronized void addProduct(Product product) {
        products.put(product.getProductID(), product);
        System.out.println("Added: " + product);
    }

    public synchronized void placeOrder(Order order) {
        orderQueue.offer(order);
        System.out.println("Order placed: " + order);
    }

    public void processOrders() {
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            executor.execute(() -> {
                try {
                    fulfillOrder(order);
                } catch (OutOfStockException e) {
                    System.err.println("Error processing order: " + e.getMessage());
                }
            });
        }
    }

    private synchronized void fulfillOrder(Order order) throws OutOfStockException {
        System.out.println("Processing " + order);

        for (String productID : order.getProductIDs()) {
            Product product = products.get(productID);
            if (product == null || product.getQuantity() <= 0) {
                throw new OutOfStockException("Product " + productID + " is out of stock.");
            }
            product.setQuantity(product.getQuantity() - 1);
        }

        System.out.println("Order fulfilled: " + order);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }
}

public class WarehouseManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryManager inventoryManager = new InventoryManager();

        System.out.println("===== WAREHOUSE MANAGEMENT SYSTEM =====");

        System.out.print("Enter number of products to add: ");
        int numProducts = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numProducts; i++) {
            System.out.print("Enter Product ID: ");
            String productID = scanner.nextLine();

            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter Aisle Number: ");
            int aisle = scanner.nextInt();

            System.out.print("Enter Shelf Number: ");
            int shelf = scanner.nextInt();

            System.out.print("Enter Bin Number: ");
            int bin = scanner.nextInt();
            scanner.nextLine();

            inventoryManager.addProduct(new Product(productID, name, quantity, new Location(aisle, shelf, bin)));
        }

        System.out.print("\nEnter number of orders to place: ");
        int numOrders = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numOrders; i++) {
            System.out.print("Enter Order ID: ");
            String orderID = scanner.nextLine();

            System.out.print("Enter number of products in order: ");
            int numOrderProducts = scanner.nextInt();
            scanner.nextLine();

            List<String> productIDs = new ArrayList<>();
            for (int j = 0; j < numOrderProducts; j++) {
                System.out.print("Enter Product ID: ");
                productIDs.add(scanner.nextLine());
            }

            System.out.print("Enter Priority (STANDARD/EXPEDITED): ");
            String priorityStr = scanner.nextLine().toUpperCase();
            Order.Priority priority = Order.Priority.valueOf(priorityStr);

            inventoryManager.placeOrder(new Order(orderID, productIDs, priority));
        }

        inventoryManager.processOrders();
        inventoryManager.shutdown();
        inventoryManager.displayInventory();
        scanner.close();
    }
}

