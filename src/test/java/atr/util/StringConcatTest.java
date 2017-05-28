package atr.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by atrposki on 27-May-17.
 */
public class StringConcatTest {
    @Test
    public void givenEmptyStrings_joinReturnsOnlyDelimiters(){
        String anyDelimiter=",";
        Stream<String> streamOfEmptyStrings = Stream.of("","","","");

        String actuall = StringConcat.reduce(anyDelimiter,streamOfEmptyStrings);

        assertEquals(",,,",actuall);
    }

    @Test
    public void givenEmptyStream_joinReturnsEmptyString(){
        String anyDelimiter="dsa";
        Stream<String> emptpyStream = Stream.empty();

        String actuall = StringConcat.reduce(anyDelimiter,emptpyStream);

        assertEquals("",actuall);
    }

    @Test
    public void whenStreamHasEmptyStrings_joinReturnsEmptyDelimiters(){
        String anyDelimiter=",";
        Stream<String> streamOfEmptyStrings = Stream.of("","1","2","","","3","","4");

        String actuall = StringConcat.reduce(anyDelimiter,streamOfEmptyStrings);

        assertEquals(",1,2,,,3,,4",actuall);
    }

    @Test
    public void givenOneElement_joinOmmitsDelimiter(){
        String anyDelimiter=",";
        String singleElement = "1";
        Stream<String> singleElementStream = Stream.of(singleElement);

        String actuall = StringConcat.reduce(anyDelimiter,singleElementStream);

        assertEquals(singleElement,actuall);
    }

    @Test
    public void givenMultipleElements_joinAddsDelimiterBetween(){
        String anyDelimiter=",";
        List<String> elements = Arrays.asList("1","2","3","4");
        String actuall = StringConcat.reduce(anyDelimiter,elements.stream());

        assertEquals(String.join(anyDelimiter,elements),actuall);
    }

    @Test
    public void givenNullElements_joinBehavesLikeStringJoin(){
        String anyDelimiter=",";
        List<String> elements = Arrays.asList("1",null,null,"4");
        String actuall = StringConcat.reduce(anyDelimiter,elements.stream());

        assertEquals(String.join(anyDelimiter,elements),actuall);
    }


}
