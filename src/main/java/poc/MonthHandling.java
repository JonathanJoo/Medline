package poc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthHandling {
    private static final String numberOnly = "^\\d+$";
    static Pattern numberPattern = Pattern.compile(numberOnly);
    private static String[] values = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private static String[] keys = new String[]{"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
    private static Map<String, String> myMap;


    public static void main(String[] args) {
        System.out.println(normalizeMonth("janoaru"));
    }


    // "01" --> "01"
    //"jan" --> "01"
    // "janoaru" --> "01"
    // 1 --> "01"
    //https://stackoverflow.com/questions/473282/how-can-i-pad-an-integer-with-zeros-on-the-left







    public static String normalizeMonth(int input) {
        if (input > 0 && input < 12) {
            if (input >= 1 && input < 10) {
                return String.format("%02d", input);
            }
            return Integer.toString(input);
        }
        return "Not a valid month";
    }

    public static String normalizeMonth(String input) {

        Matcher matcherNumberOnly = numberPattern.matcher(input);
        if (matcherNumberOnly.find()) {
            int monthValue = Integer.valueOf(input);
            if (monthValue >= 1 && monthValue < 10) {
                return String.format("%02d", monthValue);
            }
            return Integer.toString(monthValue);
        }

        input.toLowerCase();
        if (input.length() > 3) {
            input = input.substring(0, 3);
        }

        Map<String, String> myMap = mapFromArrays(keys, values);
        String month = matchMonthGetValue(myMap, input);

        if (month.isEmpty()) {
            return "not a valid month";
        } else {
            return month;
        }
    }

    private static String matchMonthGetValue(Map<String, String> myMap, String input) {
        String month="";
        Iterator it = myMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (input.equals(pair.getKey())) {
                month = (String) pair.getValue();
            }
        }
        return month;
    }

    public static <K, V> Map<K, V> mapFromArrays(K[] keys, V[] values) {
        HashMap<K, V> result = new HashMap<K, V>();
        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], values[i]);
        }
        return result;

    }
}


