package il.cshaifasweng.OCSFMediatorExample.entities;

public class Request {
    private int id;
    private String status;
    private MenuItem item;

    public Request(int id, String status, MenuItem item) {
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
    public MenuItem getItem() { return item; }
    public void setItem(MenuItem item) { this.item = item; }

}
