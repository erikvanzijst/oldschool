/*
 * AddressEventInterface.java
 *
 * Created on 17 November 2003, 17:00
 */

package ClientGUI;

import java.util.*;

/**
 *
 * @author  Laurence Crutcher
 */
public interface AddressEventListener extends EventListener {
    
    public void addAddressEventListener(AddressEventListener ael);
    public void addressEvent();
}
