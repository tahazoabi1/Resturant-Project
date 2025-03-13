package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<HostingArea> hostingAreas;

    @OneToMany(mappedBy = "branch")
    private List<MenuItem> items;

    // Constructors
    public Branch() {}

    public Branch(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<HostingArea> getHostingAreas() { return hostingAreas; }
    public void setHostingAreas(List<HostingArea> hostingAreas) { this.hostingAreas = hostingAreas; }

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> items) { this.items = items; }
}

