package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("ManagerWorker")
public class Manager extends Worker {

    private List<Request> requests;

    @OneToMany
    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }


    public Manager() {}

    public Manager(int employeeID, double salary, Branch branch, String name, String phoneNumber, String email, String password, Branch managedBranch, List<Worker> managedWorkers) {
        super(salary, branch, name, phoneNumber, email, password);
    }

}
