package il.cshaifasweng.OCSFMediatorExample.entities;

public class Order {
    private int id;
    private int totalPrice;
    private String status;
    private boolean isAccepted;
    private List<Item> items;

    public Order(int id, int totalPrice, String status, boolean isAccepted) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.isAccepted = isAccepted;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        totalPrice += item.getPrice();
    }

    public void removeItem(Item item) {
        items.remove(item);
        totalPrice -= item.getPrice();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
    public List<Item> getItems() { return items; }
}
