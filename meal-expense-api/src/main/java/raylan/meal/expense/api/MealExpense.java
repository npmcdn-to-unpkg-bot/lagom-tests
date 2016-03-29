package raylan.meal.expense.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import play.api.i18n.I18nSupport;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by spetit on 29/03/2016.
 */
@Immutable
@JsonDeserialize
public class MealExpense {
    private Instant date;
    private BigDecimal amount;

//    public MealExpense(Instant date, BigDecimal amount) {
//        this.date = date;
//        this.amount = amount;
//    }

    @JsonCreator
    public MealExpense(Instant date, BigDecimal amount) {
        this.date = Preconditions.checkNotNull(date, "date");
        this.amount = Preconditions.checkNotNull(amount, "amount");
    }

    @Override
    public boolean equals(@Nullable Object another) {
        if (this == another)
            return true;
        return another instanceof MealExpense && equalTo((MealExpense) another);
    }

    private boolean equalTo(MealExpense another) {
        return date.equals(another.date) && amount.equals(another.amount);
    }

    @Override
    public int hashCode() {
        int h = 31;
        h = h * 17 + date.hashCode();
        h = h * 17 + amount.hashCode();
        return h;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MealExpense").add("date", date).add("name", amount)
                .toString();
    }
}



