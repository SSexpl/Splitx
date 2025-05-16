package split.model.DTO;

import lombok.Data;
import split.model.Expenses;

import java.util.Map;

@Data
public class ExpenseDTO {



    Expenses expense;

    Map<String,Double> share;

}
