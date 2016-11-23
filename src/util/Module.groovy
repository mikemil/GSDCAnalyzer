package util

/**
 * Created by mike on 11/5/16.
 */
class Module {

    String name
    ModuleStatus status
    ModuleType type
    WebService webSvc
    WebServiceClient webClient

    String toString() {
        'Module: '+name+' type: '+ type + ' status: '+ status
    }
}
