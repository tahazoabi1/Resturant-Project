package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DiscriminatorValue("ManagerWorker")
public class Manager extends Worker {
    //menu
    @OneToMany(mappedBy = "manager")
    private List<Request> requests;



    @OneToMany(mappedBy = "manager")
    private List<Worker> workers;
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }


    public Manager() {}

    public Manager(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
    }

    public List<Request> getRequests() {
        return requests;
    }


    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
