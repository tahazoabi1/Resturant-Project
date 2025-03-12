package il.cshaifasweng.OCSFMediatorExample.entities;

public class Request {
    private int id;
    private String status;
    private Item item;

    public Request(int id, String status, Item item) {
        this.id = id;
        this.status = status;
        this.item = item;
    }

    public void acceptRequest() { this.status = "Accepted"; }
    public void declineRequest() { this.status = "Declined"; }
    public void sendRequest() { this.status = "waiting"; }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

}
