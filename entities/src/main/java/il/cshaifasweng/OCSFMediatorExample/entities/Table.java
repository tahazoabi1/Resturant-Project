package il.cshaifasweng.OCSFMediatorExample.entities;

public class Table {
    private int id;
    private int capacity;
    private boolean isAvailable;

    public Table(int id, int capacity, boolean isAvailable) {
        this.id = id;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }

    public void reserveTable() { this.isAvailable = false; }
    public void freeTable() { this.isAvailable = true; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

}
