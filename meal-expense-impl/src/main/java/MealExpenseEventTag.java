/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class MealExpenseEventTag {

  public static final AggregateEventTag<MealExpenseEvent> INSTANCE =
    AggregateEventTag.of(MealExpenseEvent.class);

}
