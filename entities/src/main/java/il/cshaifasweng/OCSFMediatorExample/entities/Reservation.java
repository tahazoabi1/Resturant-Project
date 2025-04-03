package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "reservation_start_time", nullable = false)
    private LocalDateTime reservationStartTime;

    @Column(name = "reservation_end_time", nullable = false)
    private LocalDateTime reservationEndTime;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Tables table;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "reserved_for_guests")
    private int reservedForGuests;

    public Reservation() {}

    public Reservation(LocalDateTime reservationStartTime, Tables table, Customer customer, int reservedForGuests) {
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationStartTime.plusMinutes(90); // Default 90-minute reservation
        this.table = table;
        this.customer = customer;
        this.reservedForGuests = reservedForGuests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getReservationStartTime() {
        return reservationStartTime;
    }

    public void setReservationStartTime(LocalDateTime reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public LocalDateTime getReservationEndTime() {
        return reservationEndTime;
    }

    public void setReservationEndTime(LocalDateTime reservationEndTime) {
        this.reservationEndTime = reservationEndTime;
    }

    public Tables getTable() {
        return table;
    }

    public void setTable(Tables table) {
        this.table = table;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getReservedForGuests() {
        return reservedForGuests;
    }

    public void setReservedForGuests(int reservedForGuests) {
        this.reservedForGuests = reservedForGuests;
    }
}
