package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dietitians")
@PrimaryKeyJoinColumn(name = "user_id")
public class Dietitian extends User {

    @OneToMany(mappedBy = "dietitian")
    private List<Item> items;

    // Constructors
    public Dietitian() {}

    public Dietitian(String name, int phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);
    }

    // Methods for item management
    public void changeItemPrice(Item item, double newPrice) {
        item.setPrice(newPrice);
    }

    public void changeItemName(Item item, String newName) {
        item.setName(newName);
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }

    public void sendRequest(Item item) {
        System.out.println("Sending request for item: " + item.getName());
    }

    // Getters and Setters
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
