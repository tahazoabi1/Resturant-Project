package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String location;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description")  // Added description column
    private String description;

    @Column(name = "image_url")  // Added image_url column
    private String imageUrl;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<HostingArea> hostingAreas;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<Worker> workers;

    @ManyToMany
    @JoinTable(
            name = "branch_menuitem",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> items;

    // Constructors
    public Branch() {}

    public Branch(String name, String location, String description, String imageUrl) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<HostingArea> getHostingAreas() { return hostingAreas; }
    public void setHostingAreas(List<HostingArea> hostingAreas) { this.hostingAreas = hostingAreas; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Branch{id=" + id + ", name='" + name + "', location='" + location + "'}";
    }
}
