package il.cshaifasweng.OCSFMediatorExample.entities;

//import jakarta.persistence.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Entity
//@Table(name = "dietitians")
@PrimaryKeyJoinColumn(name = "user_id")
public class Dietitian extends User {

//    @OneToMany(mappedBy = "dietitian")
    private List<MenuItem> items;

    // Constructors
    public Dietitian() {}

    public Dietitian(String name, String phoneNumber, String email, String password) {
        super(name, phoneNumber, email, password);
    }

    // Methods for item management
    public void changeItemPrice(MenuItem item, double newPrice) {
        item.setPrice(newPrice);
    }

    public void changeItemName(MenuItem item, String newName) {
        item.setName(newName);
    }

    public void deleteItem(MenuItem item) {
        items.remove(item);
    }

    public void sendRequest(MenuItem item) {
        System.out.println("Sending request for item: " + item.getName());
    }

    // Getters and Setters
    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}

