package atr.util;

import com.sun.scenario.effect.Merge;
import lombok.Data;

import java.util.stream.Stream;

/**
 * Created by atrposki on 27-May-17.
 */
public class StringConcat {

    public static String reduce(String delimiter, Stream<String> stream){
        StringBuilder sb = new StringBuilder();
        stream.map(StringWrapper::new)
        .reduce(null,(l,r)->{
            if(l!=null){
                sb.append(delimiter);
            }
            sb.append(r);
            return r;
        });
        return sb.toString();

    }

    @Data
    public static class StringWrapper{
        private String str;
        public StringWrapper(String str){
            this.str=str;
        }

        @Override
        public  String toString(){
            return this.str;
        }
    }
}

