package il.cshaifasweng.OCSFMediatorExample.entities;

//import jakarta.persistence.*;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
//@Table(name = "hostesses")
@PrimaryKeyJoinColumn(name = "worker_id")
public class Hostess extends Worker implements Reservation {

//    @Override
    public void reserveTable(int capacity) {
        System.out.println("Reserving a table for " + capacity + " guests.");
    }

//    @Override
    public void freeTable(Tables table) {
        System.out.println("Table " + table.getId() + " is now free.");
    }
}

