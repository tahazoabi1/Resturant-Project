package il.cshaifasweng.OCSFMediatorExample.entities;

//import jakarta.persistence.*;

import javax.persistence.*;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;

@Entity
@DiscriminatorValue("DietitianWorker")
public class Dietitian extends Worker {


    // Constructors
    public Dietitian() {
    }

    public Dietitian(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
    }

    public void sendRequest(MenuItem item) {
        System.out.println("Sending request for item: " + item.getName());
    }
}


