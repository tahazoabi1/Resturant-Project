package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.Manager;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "reports")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Transient
    private int deliveryCount;

    @Transient
    private Map<LocalDate, Integer> visitorsPerDay;

    @Transient
    private Map<String, Integer> complaintsHistogram;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public Report() {}

    public Report(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Report(String title, int deliveryCount, Map<LocalDate, Integer> visitorsPerDay, Map<String, Integer> complaintsHistogram) {
        this.title = title;
        this.deliveryCount = deliveryCount;
        this.visitorsPerDay = visitorsPerDay;
        this.complaintsHistogram = complaintsHistogram;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getDeliveryCount() { return deliveryCount; }
    public void setDeliveryCount(int deliveryCount) { this.deliveryCount = deliveryCount; }

    public Map<LocalDate, Integer> getVisitorsPerDay() { return visitorsPerDay; }
    public void setVisitorsPerDay(Map<LocalDate, Integer> visitorsPerDay) { this.visitorsPerDay = visitorsPerDay; }

    public Map<String, Integer> getComplaintsHistogram() { return complaintsHistogram; }
    public void setComplaintsHistogram(Map<String, Integer> complaintsHistogram) { this.complaintsHistogram = complaintsHistogram; }

    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }
}
