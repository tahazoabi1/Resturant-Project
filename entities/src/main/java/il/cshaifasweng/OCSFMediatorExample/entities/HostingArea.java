package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "hosting_areas")  // This will map the entity to the 'hosting_areas' table in the database
public class HostingArea implements Reservation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name; // e.g., "inside" or "outside"

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

    public List<Tables> gettable() {
        return table;
    }

    public void settable(List<Tables> table) {
        this.table = table;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // Implementing reservation logic
    @Override
    public void reserveTable(int capacity) {
        int availabletable = (int) table.stream().filter(table -> table.getCapacity() >= capacity && !table.isReserved()).count();

        if (availabletable > 0) {
            // Find the first available table and reserve it
            for (Tables table : table) {
                if (table.getCapacity() >= capacity && !table.isReserved()) {
                    table.setReserved(true);
                    break;
                }
            }
            System.out.println("Table reserved for " + capacity + " people.");
        } else {
            System.out.println("No available table for " + capacity + " people.");
        }
    }

    public void freeTable(Tables table) {
        if (this.table.contains(table) && table.isReserved()) {
            table.setReserved(false);
            System.out.println("Table freed.");
        } else {
            System.out.println("This table is not reserved or does not exist.");
        }
    }
}
