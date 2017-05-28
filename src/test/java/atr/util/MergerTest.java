package atr.util;

import atr.util.Merger;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by atrposki on 27-May-17.
 */
public class MergerTest {


    @Test
    public void givenCriteriaFalse_mergeDoesntMergeAny() {
        List<Object> anyList = Arrays.asList(1, 2, 3, 4);
        Merger<Object> merger = new Merger<>(alwaysFalse(), throwIfCalled());

        List<Object> result = merger.merge(anyList.stream());

        assertEquals(anyList.size(), result.size());
        assertArrayEquals(anyList.toArray(), result.toArray());
    }

    @Test
    public void givenCriteriaTrue_mergeMergesAllElements() {
        List<Integer> anyNonEmptyList = Arrays.asList(1, 2, 3, 4, 5);
        Merger<Integer> merger = new Merger<>(alwaysTrue(), sum());

        List<Integer> result = merger.merge(anyNonEmptyList.stream());

        assertEquals(1, result.size());

    }

    @Test
    public void givenEmptyStream_mergeReturnsEmptList() {

        Merger<Integer> merger = new Merger<>((l, r) -> true, (l, r) -> {
            throw new RuntimeException();
        });

        List<Integer> result = merger.merge(Stream.empty());

        assertTrue(result.isEmpty());

    }

    @Test
    public void givenSingleElementStream_mergeReturnsUnalteredList() {
        int anySingleElement = 3;
        List<Integer> singleElemList = Arrays.asList(anySingleElement);
        Merger<Integer> merger = new Merger<>((l, r) -> true, (l, r) -> {
            throw new RuntimeException();
        });

        List<Integer> result = merger.merge(singleElemList.stream());

        assertEquals(1, result.size());
        assertTrue(anySingleElement == result.get(0));

    }

    @Test
    public void givenCriteriaEqualsAndStrategyReturnLeft_mergeRemovesConsecutiveDuplicates() {
        List<Integer> array = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 5, 4, 4);
        Merger<Integer> merger = new Merger<>(simpleEquals(), returnLeft());

        List<Integer> result = merger.merge(array.stream());

        assertEquals(6, result.size());
        assertArrayEquals(new Object[]{1, 2, 3, 4, 5, 4}, result.toArray());

    }

    @Test
    public void givenCriteriaEqualsAndStrategyReturnLeft_mergeRemovesDuplicatesFromOrderedList() {
        List<Integer> anySortedList = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 5, 4, 4);
        anySortedList.sort(Integer::compare);
        Merger<Integer> merger = new Merger<>(simpleEquals(), returnLeft());

        List<Integer> result = merger.merge(anySortedList.stream());

        Set<Integer> expectedSet = anySortedList.stream().collect(Collectors.toSet());
        Set<Integer> resultSet = result.stream().collect(Collectors.toSet());

        assertEquals(expectedSet.size(), result.size());
        assertEquals(expectedSet, resultSet);
    }

    private <T> BiPredicate<T, T> simpleEquals() {
        return (l, r) -> l == r;
    }

    private <T> BinaryOperator<T> returnLeft() {
        return (l, r) -> l;
    }

    private <T> BiPredicate<T, T> alwaysFalse() {
        return (l, r) -> false;
    }

    private <T> BiPredicate<T, T> alwaysTrue() {
        return (l, r) -> true;
    }

    private <T> BinaryOperator<T> throwIfCalled() {
        return (l, r) ->{ throw new RuntimeException();};
    }

    private  BinaryOperator<Integer> sum() {
        return (l, r) ->l+r;
    }

}
