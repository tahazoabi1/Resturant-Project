package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;  // Changed from BigDecimal to double

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "is_accepted", nullable = false)
    private boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;  // Foreign key to the branch

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;  // Foreign key to the customer

    @ManyToMany
    @JoinTable(
            name = "order_menu_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems = new ArrayList<>();  // List of MenuItems in this order

    public Order() {}

    public Order(int id, double totalPrice, String status, boolean isAccepted, Branch branch, Customer customer) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.isAccepted = isAccepted;
        this.branch = branch;
        this.customer = customer;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<MenuItem> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItem> menuItems) { this.menuItems = menuItems; }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        totalPrice += menuItem.getPrice();  // Add price to total price
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        totalPrice = Math.max(0, totalPrice - menuItem.getPrice());  // Subtract price from total price, prevent negative value
    }
}
