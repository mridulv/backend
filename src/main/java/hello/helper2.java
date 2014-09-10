package hello;

import java.util.Comparator;
import java.util.Map;

public class helper2 implements Comparator<Long> {

    Map<Long,Long> base;
    public helper2(Map<Long, Long> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(Long a, Long b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else
            return 1;
    }
}