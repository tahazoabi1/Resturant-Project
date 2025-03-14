package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Worker")  // This value is stored in the 'discriminator' column in the 'users' table
@Table()
public class Worker extends User implements Serializable {

    @Column(name = "salary")
    private double salary;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public Worker() {}

    public Worker(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);  // Call the parent constructor
        this.salary = salary;
        this.branch = branch;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
