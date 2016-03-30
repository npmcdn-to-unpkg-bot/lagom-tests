/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import raylan.meal.expense.api.MealExpenseService;

public class MealExpenseModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(MealExpenseService.class, MealExpenseServiceImpl.class));
  }
}
