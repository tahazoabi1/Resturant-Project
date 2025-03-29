package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("Customer")  // This value is stored in the 'discriminator' column in the 'users' table
public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "address")
    private String address;

    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Tables> tableReserved;  // List of tables reserved by the customer

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orderHistory;  // List of orders placed by the customer

    @OneToMany(mappedBy = "customer")
    private List<ReservationReport> reservationReports;

//    @OneToMany(mappedBy = "customer")
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    public Customer() {}

    public Customer(String address, String preferredPaymentMethod, String name, String phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);  // Call the parent constructor
        this.address = address;
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredPaymentMethod() {
        return preferredPaymentMethod;
    }

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public List<Tables> getTablesReserved() {
        return tableReserved;
    }

    public void setTablesReserved(List<Tables> tableReserved) {
        this.tableReserved = tableReserved;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

}
