package util
/**
 * Created by mike on 11/5/16.
 */
class WarAnalyzer extends JarAnalyzer implements Analyzer {

    List<Module> analyze(String projectPath, String name) {
        // first find all modules analyzed in Jars analyzer
        def mods = super.analyze(projectPath, name)

        // now look for the WAR specific files
        mods.addAll( analyzeWAR(projectPath) )

        return mods
    }

    private List<Module> analyzeWAR(String projectPath) {
          List<Module> mods = analyzeWebXml(projectPath)
          mods.addAll( analyzeWebServicesXml(projectPath) )
          mods.addAll( analyzeWebServicesClientXml(projectPath) )

          return mods
    }

    private List<Module> analyzeWebXml(projectPath) {
        List<Module> mods = new FileFinder().findModules(projectPath, /web\.xml/, webXmlAnalysis)
        return  mods
    }
    private List<Module> analyzeWebServicesXml(projectPath) {
        List<Module> mods = new FileFinder().findModules(projectPath, /webservices\.xml/, webServicesXmlAnalysis)
        return  mods
    }
    private List<Module> analyzeWebServicesClientXml(projectPath) {
        List<Module> mods = new FileFinder().findModules(projectPath, /webservicesclient\.xml/, webServicesClientXmlAnalysis)

        // TODO - check once I can run against real source code!!!
        //these may only be in the COMPONENT project
        //mods.addAll ( new FileFinder().findModules(projectPath, /WebServiceClient\.\.*xml/, serviceLocatorClientXmlAnalysis) )

        return  mods
    }

    // Closure for processing web.xml file
    def  webXmlAnalysis = { root ->
        println '*** inside closure to process web.xml ***'
        def mods = []
        def svc = root.'resource-ref'
        svc.each {
            def name = it.'res-ref-name'.text()
            def type = it.'res-type'.text()
            if ( name.startsWith(AnalyzerConstants.J2C_CONNECTION) ) {
                mods << new Module(name: name, type: ModuleType.J2C, status: ModuleStatus.DISCOVERED)
            } else if ( type.endsWith(AnalyzerConstants.WORKMANAGER) ) {
                mods << new Module(name: name, type: ModuleType.WORKMANAGER, status: ModuleStatus.DISCOVERED)
            }
        }

        return mods
    }

    // Closure for processing webservices.xml file
    def webServicesXmlAnalysis =  { root ->
        println '*** inside closure to process webservices.xml ***'
        def mods = []
        def svc = root.'webservice-description'
        svc.each {
            // TODO - need to figure how to determine RPC vs. JAX-WS and LTPA vs. BasicAuth or something else
            WebServiceType wsType = (svc.'jaxrpc-mapping-file') ? WebServiceType.RPC : WebServiceType.UNKNOWN
            def ws = new WebService(name: it.'webservice-description-name' , wsType: wsType, securityImpl: SecurityImpl.LTPA)
            mods << new Module(name:it.'webservice-description-name', type: ModuleType.WEBSERVICE, webSvc: ws, status: ModuleStatus.DISCOVERED)
        }

        return mods
    }

    // Closure for processing webservicesclient.xml file
    def webServicesClientXmlAnalysis = { root ->
        println '*** inside closure to process webservicesclient.xml ***'
        def mods = []
        def svc = root.'client-config'
        svc.each {
            def secImpl = (it.'security-type' == AnalyzerConstants.LTPA) ? SecurityImpl.LTPA : SecurityImpl.BasicAuth
            def wsClient = new WebServiceClient(name: it.'client-name' , wsType: WebServiceType.UNKNOWN, securityImpl: secImpl)
            mods << new Module(name:it.'client-name', type: ModuleType.WEBSERVICECLIENT, webClient: wsClient, status: ModuleStatus.DISCOVERED)
        }

        return mods
    }

    // Closure for processing Service Locator files WebServiceClient.*.xml
//    def serviceLocatorClientXmlAnalysis = { root ->
//        println '*** inside closure to process service locator files ***'
//        def mods = []
//        def svc = root.'?????'
//        svc.each {
//            // TODO - need to figure how to determine RPC vs. JAX-WS and LTPA vs. BasicAuth or something else
//            //def wsClient = new WebServiceClient(name: it.'service-ref-name' , wsType: WebServiceType.RPC, securityImpl: SecurityImpl.LTPA)
//            //println it
//            //mods << new Module(name:it.'service-ref-name', type: ModuleType.WEBSERVICECLIENT, webClient: wsClient, status: ModuleStatus.DISCOVERED)
//        }
//
//        return mods
//    }

}


