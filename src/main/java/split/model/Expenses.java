package split.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@Entity
public class Expenses {

    @Id
    String id; //will manually provide for now testing purpose

    @Column
    String expenseName;

    @Column
    String expenseDescription;

    @Column
    String creatorId;

    @Column
    String groupId;

    @Column
    Double amount;

}
