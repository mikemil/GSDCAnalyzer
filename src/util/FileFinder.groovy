package util

/**
 * Created by mike on 11/5/16.
 */
class FileFinder {

    List<String> findFiles(String baseDir, String includePattern)  {
        if (!baseDir || !includePattern) {
            return []
        }

        return  new FileNameByRegexFinder().getFileNames(baseDir, includePattern)
    }

    List<String> findFiles(String baseDir, String includePattern, String textToFind)  {
        List<String> filesFound = findFiles(baseDir, includePattern)

        if (!filesFound  || !textToFind) {
            return filesFound
        }

        // now we look for the text inside each of the files returned
        List<String> retPaths = []
        filesFound.each { path ->
            String fileText = new File(path).text
            if (fileText.contains(textToFind)) {
                retPaths << path
            }
        }
        println 'found ' + retPaths.size() + ' files in '+baseDir +
                ' with pattern: ' + includePattern + ' ' +
                ' containing string:' + textToFind + '\n' +retPaths

        return retPaths
    }

    List<Module> findModules(String projectPath, String includePattern, Closure fileSpecificClosure) {
        List<String> filePaths = findFiles(projectPath, includePattern)
        if (!filePaths) return []

        List<Module> mods = []
        filePaths.each { filePath ->
            def slurper = new XmlSlurper()
            slurper.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
            slurper.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            def root = slurper.parse( new File(filePath) )
            mods.addAll ( fileSpecificClosure(root) )
        }

        return  mods
    }


    static void main(String... args) {
       //new FileFinder().findFiles('/users/mike/development/codebase/ent_ftswebsvcs', /.*Service\.*\.java/)

       // new FileFinder().findFiles('/users/mike/development/codebase/ent_ftswebsvcs', /.*Service\.*\.java/, "@WebService")

    }

}
