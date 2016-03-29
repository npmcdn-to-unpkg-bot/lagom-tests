package raylan.meal.expense.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import org.pcollections.PSequence;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

/**
 * Created by spetit on 29/03/2016.
 */
public interface MealExpenseService extends Service {

    ServiceCall<NotUsed, MealExpense, NotUsed> addExpense();

    ServiceCall<String, NotUsed, PSequence<String>> getMonthExpenses();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("friendservice").with(
                restCall(Method.GET, "/api/meal/expense/:year/:month", getMonthExpenses()),
                restCall(Method.POST, "/api/meal/expense/:timestamp", addExpense())
        ).withAutoAcl(true);
        // @formatter:on
    }


}