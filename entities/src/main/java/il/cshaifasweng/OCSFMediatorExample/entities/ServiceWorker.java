package il.cshaifasweng.OCSFMediatorExample.entities;

public class ServiceWorker extends Worker {
    private List<Complaint> complaints;

    public ServiceWorker(int id, String name, int phoneNumber, String email, String password, String position, Branch branch, int hour, int salary) {
        super(id, name, phoneNumber, email, password, position, branch, hour, salary);
        this.complaints = new ArrayList<>();
    }

    public void respondToComplaint(Complaint complaint) {
        complaint.setStatus("Resolved");
    }
}
