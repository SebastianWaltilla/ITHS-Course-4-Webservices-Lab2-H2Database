package se.iths.TwoToeSebastian.myservice;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.List;

@Data                      // Data for lombok, automatic getters and setters
@Entity                    // This will be an element in autogenerated List
@NoArgsConstructor         // Automatic generation of constructor
public class UserData {

    @Id @GeneratedValue Integer id;       // autogenerate +=1 for each new User
    String userName;
    String realName;
    String city;
    float income;
    boolean inRelationship;

    public UserData(Integer id, String userName, String realName, String city, float income, boolean inRelationship) {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.city = city;
        this.income = income;
        this.inRelationship = inRelationship;
    }

}


