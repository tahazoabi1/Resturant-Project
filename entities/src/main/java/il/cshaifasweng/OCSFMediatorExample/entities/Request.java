package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "change_requests")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status; // Waiting, Accept, Reject

    @Column(name = "new_price")
    private double newPrice;

    @ManyToOne
    @JoinColumn(name = "changed_item_id")
    private MenuItem changedItem;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    // Constructors
    public Request() {
    }

    public Request(MenuItem item , double newPrice, User manager) {
        this.changedItem = item;
        this.status = "Waiting";
        this.newPrice = newPrice;
        this.manager = (Manager)manager;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public MenuItem getChangedItem() {
        return changedItem;
    }

    public void setChangedItem(MenuItem changedItem) {
        this.changedItem = changedItem;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", newPrice=" + newPrice +
                ", changedItem=" + (changedItem != null ? changedItem.getName() : "null") +
                '}';
    }
}
