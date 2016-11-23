package util

/**
 * Created by mike on 11/10/16.
 */
class EJBAnalyzer implements Analyzer {

    List<Module> analyze(String projectPath, String name) {
        println 'analyze the EJB - not allowed to have remote EJBs!'
        List<Module> mods = []

        //todo fix later - just testing right now
        //mods << new Module(name:name, type: ModuleType.EJB, status: ModuleStatus.DISCOVERED)
        return mods
    }
}
