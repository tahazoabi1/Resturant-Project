package il.cshaifasweng.OCSFMediatorExample.entities;

public interface Reservation {

    abstract void reserveTable(int capacity);
    abstract void freeTable(Tables table);

}
