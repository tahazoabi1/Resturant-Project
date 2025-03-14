package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name="complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name="message")
    private String message;

    @Column(name="status")
    private String status;

    @Column(name="solution")
    private String solution;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private ServiceWorker serviceWorker;

    public Complaint() {}

    public Complaint(int id, Customer customer, String message, String status, String solution) {
        this.id = id;
        this.customer = customer;
        this.message = message;
        this.status = status;
        this.solution = solution;
    }

    public void giveSolution(String solution) { this.solution = solution; }
    public void sendRequest() { this.status = "Sent"; }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ServiceWorker getServiceWorker() {
        return serviceWorker;
    }

    public void setServiceWorker(ServiceWorker serviceWorker) {
        this.serviceWorker = serviceWorker;
    }
}
