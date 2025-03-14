package il.cshaifasweng.OCSFMediatorExample.entities;

public interface Reservation {

    abstract void reserveTable(int capacity, Customer customer);
    abstract void freeTable(Tables table);

}
