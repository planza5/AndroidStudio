package com.plm.twp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NumbersConverter {
    private static HashMap<String, String> ops = new HashMap();



    private static HashMap<String, String> numtokens = new HashMap();

    static {
        ops.put("por","x");
        ops.put("entre","/");
        ops.put("mas","+");
        ops.put("menos","-");

        numtokens.put("uno", "1");
        numtokens.put("dos", "2");
        numtokens.put("dós", "2");
        numtokens.put("tres", "3");
        numtokens.put("trés", "3");
        numtokens.put("cuatro", "4");
        numtokens.put("cinco", "5");
        numtokens.put("seis", "6");
        numtokens.put("seís", "6");
        numtokens.put("siete", "7");
        numtokens.put("ocho", "8");
        numtokens.put("nueve", "9");
        numtokens.put("nove", "9");
        numtokens.put("diez", "10");
        numtokens.put("once", "11");
        numtokens.put("doce", "12");
        numtokens.put("trece", "13");
        numtokens.put("catorce", "14");
        numtokens.put("quince", "15");
        numtokens.put("dieciséis", "16");
        numtokens.put("dieciseis", "16");
        numtokens.put("diecisiete", "17");
        numtokens.put("dieciocho", "18");
        numtokens.put("diecinueve", "19");
        numtokens.put("veinte", "20");
        numtokens.put("veinti", "20");
        numtokens.put("venti", "20");
        numtokens.put("treinta", "30");
        numtokens.put("cuarenta", "40");
        numtokens.put("cincuenta", "50");
        numtokens.put("sesenta", "60");
        numtokens.put("setenta", "70");
        numtokens.put("ochenta", "80");
        numtokens.put("noventa", "90");
        numtokens.put("cien", "100");
        numtokens.put("cientos", "100");
        numtokens.put("cientas", "100");
        numtokens.put("ciento", "100");
        numtokens.put("quinientos", "500");
        numtokens.put("setecientos", "700");
        numtokens.put("mil", "1000");
        numtokens.put("millon", "1000000");
        numtokens.put("millón", "1000000");
        numtokens.put("millones", "1000000");
        numtokens.put("coma", ",");
        numtokens.put("con", ",");


        numtokens.put("por","x");
        numtokens.put("entre","/");
        numtokens.put("mas","+");
        numtokens.put("menos","-");
    }


    public static int tokensToNumber(List<String> tokens) {
        int total = 0;
        int currentSum = 0;

        for (String token : tokens) {
            int value = Integer.valueOf(numtokens.get(token));

            if (value >= 100) {
                total += currentSum * value;
                currentSum = 0;
            } else {
                currentSum += value;
            }
        }

        total += currentSum;
        return total;
    }


    public static List<String> tokenize(String s) {
        s = s.replaceAll(" y ","" );
        s = s.replaceAll(" ","" );
        s = s.replaceAll("000","mil" );
        s = s.replaceAll("multiplicado por","multiplicadopor" );
        s = s.replaceAll("dividido por","divididopor" );
        s = s.replaceAll("dividido entre","divididoentre" );
        s = s.replaceAll("más","mas" );
        //s = s.replaceAll(".","" );



        int n = s.length();
        List<String>[] dp = new ArrayList[n + 1];
        dp[0] = new ArrayList<>();

        for (int i = 1; i <= n; ++i) {
            for (int j = i - 1; j >= 0; --j) {
                String token = s.substring(j, i);
                if (numtokens.containsKey(token) && dp[j] != null) {
                    if (dp[i] == null || dp[i].size() > dp[j].size() + 1) {
                        dp[i] = new ArrayList<>(dp[j]);
                        dp[i].add(token);
                    }
                }
            }
        }

        return dp[n];
    }

    /*
    public static String getValues(List<String> tokenList) {
        List<Integer> values=new ArrayList<>();


        for(String one:tokenList){
            String value=numtokens.get(one);

            if(value!=null){
                values.add(value);
            }else{
                values.add(Integer.parseInt(value));
            }
        }

        return values;
    }
*/


    public static String getMathExpresion(List<String> tokensList) {
        ArrayList partNumbers = new ArrayList();
        String result = "";

        for (String token : tokensList) {
            if(!numtokens.containsKey(token)) {
                partNumbers.add(token);
            } else {
                result+=tokensToNumber(partNumbers)+token;
                partNumbers.clear();
            }
        }

        if(!partNumbers.isEmpty()){
            result+=tokensToNumber(partNumbers);
        }

        return result;
    }
}
