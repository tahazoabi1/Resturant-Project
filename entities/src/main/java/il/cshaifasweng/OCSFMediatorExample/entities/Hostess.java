package il.cshaifasweng.OCSFMediatorExample.entities;

//import jakarta.persistence.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@DiscriminatorValue("Hostess")
public class Hostess extends Worker {

    public Hostess(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
    }

    public Hostess() {
    }

}

