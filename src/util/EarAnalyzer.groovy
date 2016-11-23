package util
/**
 * Created by mike on 11/5/16.
 */

class EarAnalyzer implements Analyzer {

    List<Module> analyze(String projectPath, String name) {
        println 'analyze the EAR - maybe some other deployment descriptors and see what modules are in the EAR'
        List<Module> mods = []

        //todo fix later - just testing right now
        mods << new Module(name:name, type: ModuleType.EJB, status: ModuleStatus.DISCOVERED)
        return mods
    }

}
