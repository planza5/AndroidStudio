package com.plm.twp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testConvert(){
        String num="treinta y tres";
        //String num="cuatrocientosveintitresmilseiscientosdoce";
        //String num="milnovecientosventicuatro";
        List<String> list = NumbersConverter.tokenize(num);
        System.out.println();
    }
}