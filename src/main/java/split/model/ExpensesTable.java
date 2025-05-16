package split.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity //used for mapping bw db and java objects
@Table //optional and needed to define a table name
public class ExpensesTable {



    private String expenseId;  // Uses UUID as primary key for event_id

   @Column(nullable = false)
    private String groupId; //event_id

    @Column(nullable = false)
    private String creatorId;

    @Column(nullable = false)
    private String userId; //user_id

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    double amount;





}
