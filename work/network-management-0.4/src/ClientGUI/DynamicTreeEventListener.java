/*
 * DynamicTreeEventListener.java
 *
 * Created on 24 November 2003, 19:45
 */

package ClientGUI;

import java.util.*;

/**
 *
 * @author  Laurence Crutcher
 */
public interface DynamicTreeEventListener extends EventListener {
         
    public void addDynamicTreeEventListener(DynamicTreeEventListener dtel);
    public void dynamicTreeEvent();

}
