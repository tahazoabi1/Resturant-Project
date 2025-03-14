package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("ServiceWorker")
public class ServiceWorker extends Worker {

    @OneToMany(mappedBy = "serviceWorker")
    private List<Complaint> complaints;

    // Constructor
    public ServiceWorker(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
        this.complaints = new ArrayList<>();
    }

    public ServiceWorker() {

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
