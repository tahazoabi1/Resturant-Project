package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "restaurant_tables")  // Maps the entity to the 'restaurant_tables' table in the database
public class Tables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int capacity; // Number of seats at the table

    private boolean reserved = false; // Table reservation status

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch; // Foreign key to the hosting area this table belongs to

    @ManyToOne
    @JoinColumn(name = "hosting_area_id")
    private HostingArea hostingArea; // Foreign key to the hosting area this table belongs to

    @ManyToOne
    @JoinColumn(name = "customer_id")  // Foreign key to the customer who reserved the table
    private Customer customer;  // The customer who reserved the table

    // Constructors, getters, setters
    public Tables() {}

    public Tables(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public HostingArea getHostingArea() {
        return hostingArea;
    }

    public void setHostingArea(HostingArea hostingArea) {
        this.hostingArea = hostingArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
