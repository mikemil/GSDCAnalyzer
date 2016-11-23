package util

/**
 * Created by mike on 11/5/16.
 */
class ComponentAnalyzer  implements Analyzer {

    List<Module> analyze(String projectPath, String name) {
        /.*Service\.*\.java/
        List<Module> mods = new FileFinder().findModules(projectPath, /WSLConfig.*\.xml/, serviceLocatorClientXmlAnalysis)
        return  mods
    }

    // Closure for processing Service Locator files WSLConfig.*.xml
    def serviceLocatorClientXmlAnalysis = { root ->
        println '*** inside closure to process service locator files ***'
        // TODO - for these we will need to 1) rename to ServiceLocator.*.xml and 2) move them into INF_SHARED_CONFIG RTC Component
        def mods = []
        def svc = root
        svc.each {
            //may need to use findAll to handle file that has multiples
            //def impl = findSecurityImplementation(it.'handler-info'.param)
            def impl = it.'handler-info'.param.find { it.'param-name' == 'securityType'}
            println impl
            //def wsClient = new WebServiceClient(name: it.'service-info'.'service-endpoint' , wsType: WebServiceType.RPC, securityImpl: SecurityImpl.LTPA)
            //println it
            //mods << new Module(name:it.'service-ref-name', type: ModuleType.WEBSERVICECLIENT, webClient: wsClient, status: ModuleStatus.DISCOVERED)
        }

        return mods
    }

    def findSecurityImplementation = {
        it.find { it.'param-name' == 'securityType' }
    }
}
