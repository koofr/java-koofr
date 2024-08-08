package net.koofr.api.util;

public interface U {
    static boolean safeEq(Object o1, Object o2) {
        return (o1 != null && o2 != null && o1.equals(o2)) ||
          (o1 == null && o2 == null);
    }
}
