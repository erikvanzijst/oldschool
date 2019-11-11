/*
 * NetworkEventListener.java
 *
 * Created on 25 November 2003, 16:45
 */

package ManagementCommands;

import java.util.*;

/**
 *
 * @author  Laurence Crutcher
 */

public interface NetworkEventListener extends EventListener {
    public void addNetworkEventListener(NetworkEventListener nel);
    public void networkEvent(Object o);
    
}
