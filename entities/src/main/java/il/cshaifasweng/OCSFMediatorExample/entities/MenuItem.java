
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id"
    )
    private int id;
    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "ingredients"
    )
    private String ingredients;
    @Column(
            name = "preferences"
    )
    private String preferences;
    @Column(
            name = "price"
    )
    private double price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "items", fetch = FetchType.EAGER)
    private List<Branch> branches;

    @ManyToMany(mappedBy = "menuItems")
    private List<Order> orders;

    public MenuItem() {
    }

    public MenuItem(int id, String name, String ingredients, String preferences,Double price) {
        this.name = name;
        this.ingredients = ingredients;
        this.preferences = preferences;
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getingredients() {
        return this.ingredients;
    }

    public void setingredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setpreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getpreferences() {
        return this.preferences;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(double Price) {
        this.price = Price;
    }

    @Override
    public String toString() {
        return String.format("MenuItem{name='%s', price='%s'}", name, price);  // Ensure all placeholders have corresponding arguments
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public List<Branch> getBranches() { return branches; }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}

