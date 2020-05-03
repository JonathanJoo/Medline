package utils;


import java.util.*;

public class MedlineUtils {
    public static String normalizeMonth (Object mm) {
        Map<String, String> mmMap = new HashMap<String, String>() {
            {
                put("jan", "01");
                put("feb", "02");
                put("mar", "03");
                put("apr", "04");
                put("may", "05");
                put("jun", "06");
                put("jul", "07");
                put("aug", "08");
                put("sep", "09");
                put("oct", "10");
                put("nov", "11");
                put("dev", "12");
            }
        };
        String mmStr=null;
        int mmSetIdx=0;

        if (mm.getClass() == String.class) {
            if (((String)mm).length() >=3){
                mm = ((String) mm).toLowerCase().substring(0, 3);
            }
            if (mmMap.containsKey(mm)) {
                mmStr = mmMap.get(mm);
            } else {
                if (isNumeric((String)mm) ) {
                    int mmInt=Integer.valueOf((String)mm);
                    if (mmInt <1 || mmInt> 12) {
                        mmStr=null;
                    } else {
                        mmStr=String.format("%02d", mmInt);
                    }

                }
            }
        } else if (mm.getClass() == Integer.class) {
            int mmInt=(Integer)mm;
            if (mmInt <1 || mmInt> 12) {
                mmStr=null;
            } else {
                mmStr=String.format("%02d", mmInt);
            }

        }
        return mmStr;
    }

    public static boolean isNumeric(final String str) {
        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;

    }


    public static void main (String[] args) {
        MedlineUtils utils = new MedlineUtils();
        System.out.println(normalizeMonth("febr"));
    }



}
