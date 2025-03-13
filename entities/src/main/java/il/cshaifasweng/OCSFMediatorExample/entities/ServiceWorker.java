package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;
import java.util.ArrayList;

public class ServiceWorker extends Worker {
    private List<Complaint> complaints;

    // Constructor
    public ServiceWorker(int id, String name, int phoneNumber, String email, String password, String position, Branch branch, int hour, int salary) {
//        super(id, name, phoneNumber, email, password, position, branch, hour, salary);
        this.complaints = new ArrayList<>();
    }

    // Method to respond to a complaint
    public void respondToComplaint(Complaint complaint) {
        complaint.setStatus("Resolved");
    }

    // Method to add a complaint
    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }
}
