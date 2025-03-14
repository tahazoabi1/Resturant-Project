package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.nio.MappedByteBuffer;
import java.util.List;

@Entity
@Table(name = "change_requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate primary key
    @Column(name = "id")  // Name the column in the table as 'id'
    private Long id;  // or int, depending on your requirement

    @Column(name = "description")
    private String  Description;

    @Column(name = "status")
    private String status; // Waiting, Accept, Reject

    @ManyToOne()
    private MenuItem changedItem;

    @ManyToOne
    private Manager manager;
}
