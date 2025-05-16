package split.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Groups {

    //will have a group id and group name and description
    @Id
    private String id;  // Uses UUID as primary key

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false,  unique = true )
    private String groupDescription;

}
