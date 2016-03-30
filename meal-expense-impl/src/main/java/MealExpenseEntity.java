/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import raylan.meal.expense.api.MealExpense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MealExpenseEntity extends PersistentEntity<MealExpenseCommand, MealExpenseEvent, MealExpenseState> {

  @Override
  public Behavior initialBehavior(Optional<MealExpenseState> snapshotState) {

    BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(
      new MealExpenseState(Optional.empty())));

    b.setCommandHandler(MealExpenseCommand.CreateMealExpense.class, (cmd, ctx) -> {
      if (state().mealExpense.isPresent()) {
        ctx.invalidCommand("MealExpense " + entityId() + " is already created");
        return ctx.done();
      } else {
        MealExpense mealExpense = cmd.mealExpense;
        List<MealExpenseEvent> events = new ArrayList<>();
        events.add(new MealExpenseEvent.MealExpenseCreated(mealExpense.date, mealExpense.amount));
        return ctx.thenPersistAll(events, () -> ctx.reply(Done.getInstance()));
      }
    });

    b.setEventHandler(MealExpenseEvent.MealExpenseCreated.class,
        evt -> new MealExpenseState(Optional.of(new MealExpense(evt.date, evt.amount))));

    return b.build();
  }

}
