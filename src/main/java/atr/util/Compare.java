package atr.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Compare<T> implements Comparator<T> {

    private List<Comparator<T>> criterias;

    public Compare() {
        this.criterias = new ArrayList<>();
    }

    private Compare(List<Comparator<T>> criterias) {
        this.criterias = criterias;
    }

    public <TProperty extends Comparable<? super TProperty>> Compare<T> by(Comparator<T> customComparator) {
        return addCriteria(customComparator);
    }

    public <TProperty extends Comparable<? super TProperty>> Compare<T> by(Function<T, TProperty> propertyAccessor) {
        return addCriteria(Comparator.comparing(propertyAccessor));
    }

    private Compare<T> addCriteria(Comparator<T> comparator) {
        List<Comparator<T>> allCriteria = new ArrayList<>();
        allCriteria.addAll(this.criterias);
        allCriteria.add(comparator);
        return new Compare<T>(allCriteria);
    }

    @Override
    public int compare(T left, T right) {
        for (Comparator<T> criteria :
            criterias) {
            int compareResult = criteria.compare(left, right);
            if (compareResult != 0) {
                return compareResult;
            }
        }

        return 0;
    }

    public BiPredicate<T, T> toEquals() {
        return (l, r) -> compare(l, r) == 0;
    }
}
