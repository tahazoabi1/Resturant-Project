package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("NetworkManager")
public class NetworkManager extends Worker {

    public NetworkManager() {}

    public NetworkManager(double salary, Branch branch, String name, String phoneNumber, String email, String password) {
        super(salary, branch, name, phoneNumber, email, password);
    }

    @Override
    public String getRole() {
        return "NetworkManager";
    }
}
