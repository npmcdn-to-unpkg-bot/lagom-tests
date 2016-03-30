/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface MealExpenseEvent extends Jsonable, AggregateEvent<MealExpenseEvent> {

  @Override
  default public AggregateEventTag<MealExpenseEvent> aggregateTag() {
    return MealExpenseEventTag.INSTANCE;
  }

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public class MealExpenseCreated implements MealExpenseEvent {
    public final String date;
    public final BigDecimal amount;
    public final Instant timestamp;

    public MealExpenseCreated(String date, BigDecimal amount) {
      this(date, amount, Optional.empty());
    }

    @JsonCreator
    private MealExpenseCreated(String date, BigDecimal amount, Optional<Instant> timestamp) {
      this.date = Preconditions.checkNotNull(date, "date");
      this.amount = Preconditions.checkNotNull(amount, "amount");
      this.timestamp = timestamp.orElseGet(() -> Instant.now());
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof MealExpenseCreated && equalTo((MealExpenseCreated) another);
    }

    private boolean equalTo(MealExpenseCreated another) {
      return date.equals(another.date) && amount.equals(another.amount) && timestamp.equals(another.timestamp);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + date.hashCode();
      h = h * 17 + amount.hashCode();
      h = h * 17 + timestamp.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("MealExpenseCreated").add("date", date).add("amount", amount)
          .add("timestamp", timestamp).toString();
    }
  }

}
