package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Worker")  // This value is stored in the 'discriminator' column in the 'users' table
public class Worker extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "employee_id", unique = true, nullable = false)
    private int employeeID;

    @Column(name = "position")
    private String position;  // Example: "Waiter", "Chef"

    @Column(name = "salary")
    private double salary;

    @Column(name = "branch_id")
    private int branchID;

    public Worker() {}

    public Worker(int employeeID, String position, double salary, int branchID, String name, String phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);  // Call the parent constructor
        this.employeeID = employeeID;
        this.position = position;
        this.salary = salary;
        this.branchID = branchID;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }
}
