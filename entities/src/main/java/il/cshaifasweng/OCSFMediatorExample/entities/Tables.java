package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "restaurant_tables")
public class Tables implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "reserved")
    private boolean reserved = false;

    @Column(name = "table_name")
    private String tableName;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "hosting_area", nullable = false)
    private String hostingArea;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    private static final Duration RESERVATION_DURATION = Duration.ofMinutes(90);

    public Tables() {}

    public Tables(int capacity, String tableName) {
        this.capacity = capacity;
        this.tableName = tableName;
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

    public void setReserved() {
        this.reserved = !this.reserved;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getHostingArea() {
        return hostingArea;
    }

    public void setHostingArea(String hostingArea) {
        this.hostingArea = hostingArea;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isTableAvailable(LocalDateTime time) {
        if (reservations == null) return true;

        return reservations.stream().noneMatch(res ->
                res.getReservationStartTime().isBefore(time.plusMinutes(90)) &&
                        res.getReservationEndTime().isAfter(time));
    }
}
