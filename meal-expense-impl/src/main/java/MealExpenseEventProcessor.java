/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class MealExpenseEventProcessor extends CassandraReadSideProcessor<MealExpenseEvent> {

  private PreparedStatement writeFollowers = null; // initialized in prepare
  private PreparedStatement writeOffset = null; // initialized in prepare

  private void setMealExpense(PreparedStatement writeFollowers) {
    this.writeFollowers = writeFollowers;
  }

  private void setWriteOffset(PreparedStatement writeOffset) {
    this.writeOffset = writeOffset;
  }

  @Override
  public AggregateEventTag<MealExpenseEvent> aggregateTag() {
    return MealExpenseEventTag.INSTANCE;
  }

  @Override
  public CompletionStage<Optional<UUID>> prepare(CassandraSession session) {
    // @formatter:off
    return
      prepareCreateTables(session).thenCompose(a ->
      prepareWriteMealExpense(session).thenCompose(b ->
      prepareWriteOffset(session).thenCompose(c ->
      selectOffset(session))));
    // @formatter:on
  }

  private CompletionStage<Done> prepareCreateTables(CassandraSession session) {
    // @formatter:off
    return session.executeCreateTable(
        "CREATE TABLE IF NOT EXISTS meal_expense2 ("
          + "date text, amount decimal, "
          + "PRIMARY KEY (date))")
            .thenCompose(a -> session.executeCreateTable(
                    "CREATE TABLE IF NOT EXISTS meal_expense_offset ("
                            + "partition int, offset timeuuid, "
                            + "PRIMARY KEY (partition))"));
    /*

     */
    // @formatter:on
  }

  private CompletionStage<Done> prepareWriteMealExpense(CassandraSession session) {
    return session.prepare("INSERT INTO meal_expense2 (date, amount) VALUES (?, ?)").thenApply(ps -> {
      setMealExpense(ps);
      return Done.getInstance();
    });
  }

  private CompletionStage<Done> prepareWriteOffset(CassandraSession session) {
    return session.prepare("INSERT INTO meal_expense_offset (partition, offset) VALUES (1, ?)").thenApply(ps -> {
      setWriteOffset(ps);
      return Done.getInstance();
    });
  }

  private CompletionStage<Optional<UUID>> selectOffset(CassandraSession session) {
    return session.selectOne("SELECT offset FROM meal_expense_offset")
        .thenApply(
        optionalRow -> optionalRow.map(r -> r.getUUID("offset")));
  }

  @Override
  public EventHandlers defineEventHandlers(EventHandlersBuilder builder) {
    builder.setEventHandler(MealExpenseEvent.MealExpenseCreated.class, this::processMealEntityChanged);
    return builder.build();
  }

  private CompletionStage<List<BoundStatement>> processMealEntityChanged(MealExpenseEvent.MealExpenseCreated event, UUID offset) {
    BoundStatement bindWriteFollowers = writeFollowers.bind();

    bindWriteFollowers.setString("date", event.date);
    bindWriteFollowers.setDecimal("amount", event.amount);
    BoundStatement bindWriteOffset = writeOffset.bind(offset);
    return completedStatements(Arrays.asList(bindWriteFollowers, bindWriteOffset));
  }



}
