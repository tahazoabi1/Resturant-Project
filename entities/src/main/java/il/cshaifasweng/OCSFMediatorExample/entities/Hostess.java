package il.cshaifasweng.OCSFMediatorExample.entities;

//import jakarta.persistence.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@DiscriminatorValue("HostessWorker")
public class Hostess extends Worker implements Reservation {

    public Hostess(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
    }

    public Hostess() {

    }

    @Override
    public void reserveTable(int capacity, Customer customer) {
        System.out.println("Reserving a table for " + capacity + " guests.");
    }

    @Override
    public void freeTable(Tables table) {
        System.out.println("Table " + table.getId() + " is now free.");
        table.setReserved(false);
    }
}

