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
}
