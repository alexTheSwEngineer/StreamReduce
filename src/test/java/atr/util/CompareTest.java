import atr.util.Compare;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Created by atrposki on 27-May-17.
 */
public class CompareTest {

    public static class Properties{
        int property1=1;
        int property2=2;
        int property3=3;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Properties)) return false;

            Properties that = (Properties) o;

            if (property1 != that.property1) return false;
            if (property2 != that.property2) return false;
            return property3 == that.property3;

        }

        @Override
        public int hashCode() {
            int result = property1;
            result = 31 * result + property2;
            result = 31 * result + property3;
            return result;
        }
    }

    @Test  //TODO atr split this in properly worded tests
    public void comparesWithCorrectPriority(){
        Properties left = new Properties();
        Properties right = new Properties();

        Compare<Properties> comparator = new Compare<Properties>().by(x -> x.property1).by(x -> x.property2).by((l, r) -> -1);

        BiPredicate<Properties, Properties> equals = comparator.toEquals();
        Assert.assertFalse(equals.test(left,right));

        Compare<Properties> comparator2 = new Compare<Properties>().by(x -> x.property1).by(x -> x.property2).by(x->x.property3);
        Assert.assertTrue(comparator2.toEquals().test(left,right));

        right.property1=10;
        left.property3=10;
        left.property2=10;
        List<Properties> sorted = Arrays.asList(left, right).stream().sorted(comparator2).collect(Collectors.toList());
        Assert.assertEquals(sorted.get(0),left);
        Assert.assertEquals(sorted.get(1),right);

    }

}
