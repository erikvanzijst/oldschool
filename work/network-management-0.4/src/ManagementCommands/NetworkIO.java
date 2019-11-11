/*
 * NetworkIO.java
 *
 * Created on 25 November 2003, 13:59
 */

package ManagementCommands;

import jozart.swingutils.*;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import javax.swing.event.*;
import java.lang.reflect.InvocationTargetException;

import logging.OMMLogger;
import com.marketxs.management.*;
/**
 *
 * @author  Laurence Crutcher
 */
public class NetworkIO implements NetworkEventListener {
    protected EventListenerList listenerList = new EventListenerList();    
    
    /** Creates a new instance of NetworkIO */
    public NetworkIO() {
    }
    
    
    public void read(int routerId, ManagementClient client, String key, String attribute) {
        OMMLogger.logger.debug("network read thread starting");
        ReadWorker readWorker = new ReadWorker(routerId, client, key, attribute);
    }
    
    public void write(int routerId, ManagementClient client, String key, String attribute, Object value) {
        OMMLogger.logger.debug("network write thread starting");
        WriteWorker writeWorker = new WriteWorker(routerId, client, key, attribute, value);        
    }        
    
    public void invoke(int routerId, ManagementClient client, String key, String method, Object[] args, String[] signature) {
        OMMLogger.logger.debug("network invoke thread starting");
        InvokeWorker invokeWorker = new InvokeWorker(routerId, client, key, method, args, signature);
    }
  
    class ReadWorker extends SwingWorker {
        ManagementClient client;
        String key;
        String attribute;
        int routerId;
        SwingWorker worker;
        Throwable exception;        
        
        ReadWorker(final int r, final ManagementClient c, final String k, final String a) {
            client = c;
            key = k;
            attribute = a;
            routerId = r;
            worker = this;

            /* Start worker */
            worker.start();            
        }
        
        protected Object construct() throws java.lang.Exception {
            System.out.println("key:" + key);
            System.out.println("attribute:" + attribute);
                Object obj = client.readAttribute(key, attribute);
                OMMLogger.logger.debug("readAttribute returned:" + obj);
                return obj;            
        }

        public void finished() {
                /* Set the worker to null and stop, but only if we are the active worker. */
                if (worker == this) {
                    worker = null;
                }
                try {
                    Object returnObject = get();
                    networkEvent(returnObject);
                }
                catch (InvocationTargetException ex) {
                    /* Handle exceptions thrown by the factory method. */
                    exception = ex.getTargetException();
                    cleanUp(routerId, exception);
                }
                catch (InterruptedException ex) {
                    // unreachable - result is ready
                    Thread.currentThread().interrupt();
                }
        }

    }
        
    
    
    class WriteWorker extends SwingWorker {
        ManagementClient client;
        String key;
        String attribute;
        int routerId;
        Object value;
        SwingWorker worker;
        Throwable exception;        
        
        WriteWorker(final int r, final ManagementClient c, final String k, final String a, final Object o) {
            client = c;
            key = k;
            attribute = a;
            value = o;
            routerId = r;
            worker = this;

            /* Start worker */
            worker.start();            
        }
        
        protected Object construct() throws java.lang.Exception {
                client.writeAttribute(key, attribute, value);
                OMMLogger.logger.debug("writeAttribute returned");
                return null;            
        }

        public void finished() {
                /* Set the worker to null and stop, but only if we are the active worker. */
                if (worker == this) {
                    worker = null;
                }
                try {
                    Object returnObject = get();
                }
                catch (InvocationTargetException ex) {
                    /* Handle exceptions thrown by the factory method. */
                    exception = ex.getTargetException();
                    cleanUp(routerId, exception);
                }
                catch (InterruptedException ex) {
                    // unreachable - result is ready
                    Thread.currentThread().interrupt();
                }
        }

    }
    
    class InvokeWorker extends SwingWorker {
        ManagementClient client;
        String key;
        String method;
        int routerId;
        Object[] args;
        String[] signature;
        SwingWorker worker;
        Throwable exception;        
        
        InvokeWorker(int r, ManagementClient c, String k, String m, Object[] a, String[] s) {
            client = c;
            key = k;
            method = m;
            args = a;
            routerId = r;
            signature = s;
            worker = this;

            /* Start worker */
            worker.start();            
        }
        
        protected Object construct() throws java.lang.Exception {
            Object object=client.invoke(key, method, args, signature);
            OMMLogger.logger.debug("invoke returned");
            return object;           
        }

        public void finished() {
                /* Set the worker to null and stop, but only if we are the active worker. */
                if (worker == this) {
                    worker = null;
                }
                try {
                    Object returnObject = get();
                    networkEvent(returnObject);
                }
                catch (InvocationTargetException ex) {
                    /* Handle exceptions thrown by the factory method. */
                    exception = ex.getTargetException();
                    cleanUp(routerId, exception);
                }
                catch (InterruptedException ex) {
                    // unreachable - result is ready
                    Thread.currentThread().interrupt();
                }
        }

    }    

    private void cleanUp(int routerId, Throwable t) {
        String errorMessage;
        if (t instanceof TimeoutException) {
            errorMessage = "timeout";
        }
        else if (t instanceof InterruptedException) {
            errorMessage = "interrupted";
        }
        else {
            errorMessage = t.getMessage();
        } 
        networkEvent(new NetworkException(t, routerId));        
    }    
    
    public void addNetworkEventListener(NetworkEventListener nel) {
        listenerList.add(NetworkEventListener.class, nel);
    }
    
    public void networkEvent(Object o) {
        if (o==null)
            OMMLogger.logger.debug("network event: null");         
        else {
            OMMLogger.logger.debug("network event: " + o.toString());        
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying those that are interested in this event
            for (int i = listeners.length-2; i>=0; i-=2) {
                if (listeners[i]==NetworkEventListener.class) {
                    ((NetworkEventListener)listeners[i+1]).networkEvent(o);
                }	       
            }     
        }
    }
    
}
