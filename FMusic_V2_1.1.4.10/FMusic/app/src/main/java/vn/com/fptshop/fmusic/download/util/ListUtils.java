package vn.com.fptshop.fmusic.download.util;

import java.util.List;

/**
 * Created by MinhDH on 12/4/15.
 */
public class ListUtils {

    public static final boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }
}