package il.cshaifasweng.OCSFMediatorExample.entities;

public class Complaint {
    private int id;
    private Customer customer;
    private String message;
    private String status;
    private String solution;

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
}
