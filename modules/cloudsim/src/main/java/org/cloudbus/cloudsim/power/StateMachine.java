package org.cloudbus.cloudsim.power;

import java.util.List;

/**
 * Created by ponaszki on 2018-04-30.
 */
public interface StateMachine {
    int getTransitionTime(HostState destState);

}
