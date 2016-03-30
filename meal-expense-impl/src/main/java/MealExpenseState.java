/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import raylan.meal.expense.api.MealExpense;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class MealExpenseState implements Jsonable {

  public final Optional<MealExpense> mealExpense;

  @JsonCreator
  public MealExpenseState(Optional<MealExpense> mealExpense) {
    this.mealExpense = Preconditions.checkNotNull(mealExpense, "mealExpense");
  }

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof MealExpenseState && equalTo((MealExpenseState) another);
  }

  private boolean equalTo(MealExpenseState another) {
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
    return MoreObjects.toStringHelper("MealExpenseState").add("mealExpense", mealExpense).toString();
  }
}
