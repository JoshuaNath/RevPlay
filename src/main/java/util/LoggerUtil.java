package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
    public static Logger getLogger(Class<?> cls) {
        return LogManager.getLogger(cls);
    }
}
