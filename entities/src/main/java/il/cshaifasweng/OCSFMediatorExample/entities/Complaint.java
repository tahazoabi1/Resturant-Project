package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "solution")
    private String solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private ServiceWorker serviceWorker;

    @Column(name = "compensation")
    private Double compensationAmount;

    public Double getCompensationAmount() {
        return compensationAmount;
    }

    public void setCompensationAmount(Double compensationAmount) {
        this.compensationAmount = compensationAmount;
    }

    public Complaint() {}

    public Complaint(String message, String status) {
        this.message = message;
        this.status = status;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getSolution() {
        return solution;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
