package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "managers")
public class Manager extends Worker {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "managed_branch_id")
    private Branch managedBranch;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Worker> managedWorkers;

    public Manager() {}

    public Manager(int employeeID, String position, double salary, int branchID, Branch managedBranch, List<Worker> managedWorkers) {
        super(employeeID, position, salary, branchID);
        this.managedBranch = managedBranch;
        this.managedWorkers = managedWorkers;
    }

    public Branch getManagedBranch() {
        return managedBranch;
    }

    public void setManagedBranch(Branch managedBranch) {
        this.managedBranch = managedBranch;
    }

    public List<Worker> getManagedWorkers() {
        return managedWorkers;
    }

    public void setManagedWorkers(List<Worker> managedWorkers) {
        this.managedWorkers = managedWorkers;
    }
}
