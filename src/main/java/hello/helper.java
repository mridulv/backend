package hello;

import java.util.Comparator;
import java.util.Map;

class helper implements Comparator<String> {

    Map<String, Double> base;
    public helper(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}