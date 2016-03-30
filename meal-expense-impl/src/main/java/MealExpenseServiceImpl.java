import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import raylan.meal.expense.api.MealExpense;
import raylan.meal.expense.api.MealExpenseService;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by spetit on 29/03/2016.
 */
public class MealExpenseServiceImpl implements MealExpenseService {


    private final PersistentEntityRegistry persistentEntities;
    private final CassandraSession db;

    @Inject
    public MealExpenseServiceImpl(PersistentEntityRegistry persistentEntities, CassandraReadSide readSide,
                             CassandraSession db) {
        this.persistentEntities = persistentEntities;
        this.db = db;

        persistentEntities.register(MealExpenseEntity.class);
        readSide.register(MealExpenseEventProcessor.class);
    }

    private PersistentEntityRef<MealExpenseCommand> friendEntityRef(Instant date) {
        PersistentEntityRef<MealExpenseCommand> ref = persistentEntities.refFor(MealExpenseEntity.class, date.toString());
        return ref;
    }


    @Override
    public ServiceCall<NotUsed, MealExpense, NotUsed> addExpense() {
        return (id, request) -> {
            return friendEntityRef(request.date).ask(new MealExpenseCommand.CreateMealExpense(request))
                    .thenApply(ack -> NotUsed.getInstance());
        };
    }

    @Override
    public ServiceCall<String, NotUsed, PSequence<String>> getMonthExpenses() {
        return (date, req) -> {
            CompletionStage<PSequence<String>> result = db.selectAll("SELECT * FROM meal_expense WHERE date <= ? AND date >= ?", date, date)
                    .thenApply(rows -> {
                        List<String> amounts = rows.stream().map(row -> row.getString("amount")).collect(Collectors.toList());
                        return TreePVector.from(amounts);
                    });
            return result;
        };
    }

}
