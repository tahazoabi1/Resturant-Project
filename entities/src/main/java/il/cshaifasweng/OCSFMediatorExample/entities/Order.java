package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private int id;
    private int totalPrice;
    private String status;
    private boolean isAccepted;
    private List<MenuItem> menuItems;

    // Default Constructor
    public Order() {
        this.menuItems = new ArrayList<>();
    }

    // Parameterized Constructor
    public Order(int id, int totalPrice, String status, boolean isAccepted) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.isAccepted = isAccepted;
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        totalPrice += menuItem.getPrice();
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        totalPrice = (int) Math.max(0, totalPrice - menuItem.getPrice()); // Prevent negative price
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
    public List<MenuItem> getMenuItems() { return menuItems; }
}
