package util

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created by mike on 11/5/16.
 */
@EqualsAndHashCode
@ToString(includeNames=true)
class Module {

    String name
    ModuleStatus status
    ModuleType type
    WebService webSvc
    WebServiceClient webClient

    //String toString() {
    //    'Module: '+name+' type: '+ type + ' status: '+ status
    //}
}
