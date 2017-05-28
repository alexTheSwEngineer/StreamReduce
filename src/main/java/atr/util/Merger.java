package atr.util;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * Created by atrposki on 25-May-17.
 */
public class Merger <T>{

    private BiPredicate<T,T> mergeCriteria;
    private BinaryOperator<T> mergeStrategy;

    public Merger(BiPredicate<T,T> mergeCriteria, BinaryOperator<T> mergeStrategy){
        this.mergeCriteria=mergeCriteria;
        this.mergeStrategy=mergeStrategy;
    }

    public Merger(Compare<T> comparator, BinaryOperator<T> mergeStrategy){
       this(comparator.toEquals(),mergeStrategy);
    }

    public List<T> merge(Stream<T> stream){
        MergeWrapper<T> result= stream.map(this::map)
              .reduce(null,this::reduce);
        return result!=null?result.toList():new ArrayList<T>();
    }

    private MergeWrapper<T> map(T obj){
        return Merger.staticMap(obj);
    }

    private MergeWrapper<T> reduce(MergeWrapper<T> left,MergeWrapper<T> right){
        if(right.getPrevious()!=null){
            throw new IllegalArgumentException("right hand argument must not have predecessor");
        }

        if(left == null){
            return right;
        }

        if(this.mergeCriteria.test(left.get(),right.get())){
            T merged = this.mergeStrategy.apply(left.get(),right.get());
            return new MergeWrapper<T>(left.getPrevious(),merged);
        }

        return new MergeWrapper<T>(left,right.get());
    }

    public static <T> MergeWrapper<T> staticMap(T object) {
        return new MergeWrapper<T>(null,object);
    }

    public Stream<T> mergeToStream(Stream<T> inputStream) {
        return merge(inputStream).stream();
    }

    public static class MergeWrapper<T> {
        private MergeWrapper<T> previous;
        private T merged;

        private MergeWrapper(MergeWrapper<T> previous, T merged) {
            this.merged = merged;
            this.previous = previous;
        }

        public T get() {
            return merged;
        }

        public MergeWrapper<T> getPrevious() {
            return previous;
        }

        public List<T> toList(){
            Deque<T> result = new LinkedList<T>();
            MergeWrapper<T> current = this;
            while (current!=null){
              result.addFirst(current.get());
              current = current.getPrevious();
            }
            return (LinkedList<T>)result;
        }

    }
}
