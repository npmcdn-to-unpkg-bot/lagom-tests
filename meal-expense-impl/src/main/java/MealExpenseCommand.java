/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import raylan.meal.expense.api.MealExpense;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

public interface MealExpenseCommand extends Jsonable {

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class CreateMealExpense implements MealExpenseCommand, PersistentEntity.ReplyType<Done> {
    public final MealExpense mealExpense;

    @JsonCreator
    public CreateMealExpense(MealExpense mealExpense) {
      this.mealExpense = Preconditions.checkNotNull(mealExpense, "mealExpense");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof CreateMealExpense && equalTo((CreateMealExpense) another);
    }

    private boolean equalTo(CreateMealExpense another) {
      return mealExpense.equals(another.mealExpense);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + mealExpense.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("CreateMealExpense").add("mealExpense", mealExpense).toString();
    }
  }
}
