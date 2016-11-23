package util

import groovy.json.JsonOutput

/**
 * Created by mike on 11/3/16.
 */
class GSDCAnalyzer {


    static void main(String... args) {
        String defaultCodePath = "/Users/mike/development/codebase"
       analyzeProjects(defaultCodePath)
    }

    static void analyzeProjects(String codePath) {
        def projects = []
        new File(codePath).eachDir() { dir ->
            projects << analyzeProject(dir)
        }

        def json = JsonOutput.toJson(projects)
        println JsonOutput.prettyPrint(json)
    }

    private static Project analyzeProject(File dir) {
        def project = new Project(dir)
        def analyzer = project.getAnalyzer(dir.getPath())

        project.setModules(analyzer.analyze(dir.getPath(), dir.getName()))

        return project
    }

}
