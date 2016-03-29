import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.pcollections.PSequence;
import raylan.meal.expense.api.MealExpense;
import raylan.meal.expense.api.MealExpenseService;

/**
 * Created by spetit on 29/03/2016.
 */
public class MealExpenseServiceImpl implements MealExpenseService {

    @Override
    public ServiceCall<NotUsed, MealExpense, NotUsed> addExpense() {
        return null;
    }

    @Override
    public ServiceCall<String, NotUsed, PSequence<String>> getMonthExpenses() {
        return null;
    }

}
