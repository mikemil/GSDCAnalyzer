package util

/**
 * Created by mike on 11/9/16.
 */
class AnalyzerUtil {

    static String getNameFromPath(String path)  {
        String name = ''
        if (path) {
            def idx = path.lastIndexOf(System.getProperty('file.separator'))
            if (idx >0) {
                name = path.substring(idx+1)
            }
        }
        return name
    }

}