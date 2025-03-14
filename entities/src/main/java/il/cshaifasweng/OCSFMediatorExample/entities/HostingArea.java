package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "hosting_areas")  // This will map the entity to the 'hosting_areas' table in the database
public class HostingArea{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name; // e.g., "inside" or "outside"

    @Column(name = "table_count")
    private int tableCount; // Number of table in the area

    @OneToMany(mappedBy = "hostingArea")
    private List<Tables> table; // List of table in this hosting area

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch; // Each hosting area is associated with one branch

    // Constructors, getters, setters

    public HostingArea() {}

    public HostingArea(String name, int tableCount, Branch branch) {
        this.name = name;
        this.tableCount = tableCount;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public List<Tables> getTables() {
        return table;
    }

    public void setTables(List<Tables> table) {
        this.table = table;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
