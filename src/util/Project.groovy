package util

import groovy.transform.ToString

/**
 * Base class for each source code project
 * and it should have a list of 'modules'
 * each representing some type of work to be
 * done as part of the migration.
 */
@ToString
class Project {

    String projectName
    String path
    ProjectType projectType

    List<Module> modules

    Project(File dir) {
        projectName = dir.getName()
        path = dir.getPath()
    }

    Analyzer getAnalyzer(String projectPath) {
        Analyzer analyzer = null

        if (projectIsEAR(path)) {
            analyzer = new EarAnalyzer()
            projectType = ProjectType.EAR
        } else if (projectIsWAR(path)) {
            analyzer = new WarAnalyzer()
            projectType = ProjectType.WAR
        } else if (projectIsComponent(path)) {
            analyzer = new ComponentAnalyzer()
            projectType = ProjectType.COMPONENT
        } else if (projectIsEJB(path)) {
            analyzer = new EJBAnalyzer()
            projectType = ProjectType.EJB
        } else {
            analyzer = new JarAnalyzer()
            projectType = ProjectType.JAR
        }
        return analyzer
    }

    boolean projectIsEAR(projectPath) {
        // check for application.xml
        return new File(projectPath+System.getProperty('file.separator')
                +AnalyzerConstants.META_INF_FOLDER+System.getProperty('file.separator')
                +AnalyzerConstants.APPLICATION_XML)
                .exists()
    }

    boolean projectIsEJB(projectPath) {
        // quick&dirty - check if name ends EJB or maybe contains ejb string
        // more detail would be to see if we find either the @EJB annotations or the ejb-jar.xml
        // deployment descriptor
        //
        // not sure I care about the EJB projects...
        //
        return projectPath.contains("ejb") || projectPath.endsWith("EJB")
    }

    boolean projectIsWAR(projectPath) {
        return new File(projectPath+System.getProperty('file.separator')
                +AnalyzerConstants.WEB_INF_FOLDER+System.getProperty('file.separator')+
                AnalyzerConstants.WEB_XML)
                .exists()
    }

    boolean projectIsComponent(projectPath) {
        // look for two directories - build and www to find a COMPONENT projectName
        def dirs = []
        new File(projectPath).eachDir() { dir ->
            dirs << dir.getName()
        }
        println 'directories: '+dirs
        def componentDirs = ['build', 'www']
        if (dirs.size() == 2 && dirs.containsAll(componentDirs)) {
            return true
        }
        return false
    }

//    String toString() {
//        '\nProject: '+projectName+'\n path: '+path+'\n projectType: '+projectType+'\n modules: '+modules
//    }

}
