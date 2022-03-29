package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;

import java.io.Serializable;
import java.util.HashMap;

public class Error implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public static class Err implements JsonBase, Serializable {
        private static final long serialVersionUID = 1L;

        public static class Extra extends HashMap<String, Object> implements Serializable {
            private static final long serialVersionUID = 1L;
        }

        ;

        public String code;
        public String message;
        public Extra extra;

        public Err() {
        }

    }

    public Error.Err error;
    public String requestId;

    public Error() {
    }

    @Override
    public String toString() {
        String extras = " ";
        if(error.extra != null) {
            try {
                extras += Transmogrifier.mapObject(error.extra).toString();
            } catch(JsonException e) {}
        }
        return "code " + error.code + " (" + error.message + ")" + extras;
    }
}

