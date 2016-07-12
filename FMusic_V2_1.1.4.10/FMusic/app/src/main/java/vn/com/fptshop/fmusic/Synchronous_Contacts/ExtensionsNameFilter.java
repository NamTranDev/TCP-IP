package vn.com.fptshop.fmusic.Synchronous_Contacts;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Nam on 1/7/2016.
 */
public class ExtensionsNameFilter implements FilenameFilter {

    public static final String[] IMAGE_FILTER = new String[] {".png", ".jpg", ".bmp"};
    String[] mExtensions;
    public ExtensionsNameFilter(String[] extensions)
    {
        mExtensions = extensions;
    }
    @Override
    public boolean accept(File dir, String filename) {
        String lowercaseName = filename.toLowerCase();
        for(String ext : mExtensions) {
            if (lowercaseName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

}
