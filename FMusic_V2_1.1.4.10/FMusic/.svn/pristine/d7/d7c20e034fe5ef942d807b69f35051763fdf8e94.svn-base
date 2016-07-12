package vn.com.fptshop.fmusic.download.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by MinhDH on 12/4/15.
 */
public class IOCloseUtils {

    public static final void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (IOCloseUtils.class) {
                closeable.close();
            }
        }
    }
}
