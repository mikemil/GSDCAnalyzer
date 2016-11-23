package util

/**
 * Created by mike on 11/5/16.
 */
class JarAnalyzer implements Analyzer {

    List<Module> analyze(String projectPath, String name) {
        println 'analyze the Jar - mostly looking for service locator code, digging thru *.java source code'
        List<Module> mods = []

        // check for the assorted things we will need to migrate
        mods.addAll( analyzeForWebServices(projectPath) )
        mods.addAll( analyzeForWebServiceClients(projectPath) )
        mods.addAll( analyzeForHibernate(projectPath) )
        mods.addAll( analyzeForOpenJPA(projectPath) )
        mods.addAll( analyzeForOpenJPA(projectPath) )
        mods.addAll( analyzeForAtomicWorkTasks(projectPath) )

        return mods
    }

    protected List<Module> analyzeForWebServices(String projectPath) {
        List<Module> mods = []
        List<String> filePaths = new FileFinder().findFiles(projectPath, /.*Service\.*\.java/, "@WebService")
        if (filePaths) {
            filePaths.each { path ->
                String fileText = new File(path).getText('UTF-8')
                if (fileText.contains("@WebService")) {
                    String svcName = AnalyzerUtil.getNameFromPath(path)
                    // TODO - need to determine RPC vs. JAX-WS
                    // TODO - need to determine LTPA vs. BasicAuth or something else
                    WebService ws = new WebService(name: svcName, wsType: WebServiceType.RPC, securityImpl: SecurityImpl.LTPA )
                    mods <<  new Module(name:svcName, type: ModuleType.WEBSERVICE, webSvc:ws)
                }
            }
        }
        return mods
    }

    protected List<Module> analyzeForWebServiceClients(String projectPath) {
        // think I have already implemented in the WAR analyzer!!!
        // just need to make sure that file won't appear anyplace other than a WAR...
        return []
    }

    protected List<Module> analyzeForHibernate(String projectPath) {
        //check for file hibernate.cfg.xml - which is hibernate's configuration file
        List<Module> mods = []
        List<String> filePaths = new FileFinder().findFiles(projectPath, /hibernate.cfg.xml/)
        if (filePaths) {
            mods <<  new Module(name:AnalyzerUtil.getNameFromPath(projectPath), type: ModuleType.HIBERNATE, status: ModuleStatus.DISCOVERED)
        }
        return mods
    }


    protected List<Module> analyzeForOpenJPA(String projectPath) {
        // check for file 'persistence.xml'  which is used by JPA
        //  TODO - also check java files for string openjpa - but it may be in comments!
        List<Module> mods = []
        List<String> filePaths = new FileFinder().findFiles(projectPath, /persistence\.xml/)
        if (filePaths) {
            mods <<  new Module(name:AnalyzerUtil.getNameFromPath(projectPath), type: ModuleType.OPENJPA, status: ModuleStatus.DISCOVERED)
        }
        return mods
    }

    protected List<Module> analyzeForAtomicWorkTasks(String projectPath) {
        // check for java source files containing "extends AtomicWorkTask"
        List<Module> mods = []
        List<String> filePaths = new FileFinder().findFiles(projectPath, /.*\.java/, "extends AtomicWorkTask")
        if (filePaths) {
            filePaths.each { path ->
                mods <<  new Module(name:AnalyzerUtil.getNameFromPath(path), type: ModuleType.WORKMANAGER, status: ModuleStatus.DISCOVERED)
            }
        }
        return mods
    }


}
