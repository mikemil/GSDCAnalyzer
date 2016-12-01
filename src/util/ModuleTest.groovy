package util


/**
 * Created by mike on 11/30/16.
 */
class ModuleTest {


    static void main(String... args) {

        Module m1 = new Module(name:'eft_comm', status:ModuleStatus.DISCOVERED, type:ModuleType.WORKMANAGER)
        println m1

        Module m2 = new Module(name:'eft_comm1', status:ModuleStatus.DISCOVERED, type:ModuleType.WORKMANAGER)
        println m2

        println 'm1 == m2 : '+m1.equals(m2)

        def set = new HashSet<Module>()
        set.add(m1)
        set.add(m2)
        println set
        println set.size()

        // idea is maybe when running against real source code - I might need to add Modules to a set - so
        // we don't create duplicate Modules

    }
}
