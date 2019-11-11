/*
 * ManagementInterface.java
 *
 * Created on 4 december 2003, 11:55
 */

package configuration;

import com.marketxs.management.*;

/**
 *
 * @author  laurence.crutcher
 */
public class ManagementInterface {
    int managementPort;
    int httpPort;
    int listenerPort;        
    String httpUser;
    String httpPassword;    
    State managementChannelState;
    ManagementClient managementClient;
    String managementDomain;
    String managementAgent;
    String messagingDomain;
    String messagingAgent;  
        
    /** Creates a new instance of ManagementInterface */
    public ManagementInterface() {
    }
    
        public ManagementClient getManagementClient() {
            return managementClient;
        }
        
        public void setManagementClient(ManagementClient client) {
            managementClient = client;
        }    
   
        public String getManagementDomain() {
            return managementDomain;
        }
        
        public String getManagementAgent() {
            return managementAgent;
        }        
        
        public String getMessagingDomain() {
            return messagingDomain;
        }
        
        public void setMessagingDomain(String md) {
            messagingDomain = md;
        }
        
        public String getMessagingAgent() {
            return messagingAgent;
        }
        
        public void setMessagingAgent(String ma) {
            messagingAgent = ma;
        }
        
        public String getMessagingKey() {
            return (messagingDomain + ":" + messagingAgent);
        }
            
        
        public int getListenerPort() {
            return listenerPort;
        }
        
        void setListenerPort (int port) {
            listenerPort = port;
        }
        
        void setHTTPPassword (String s) {
            httpPassword = s;
        }
                
        void setHTTPPort (int port) {
            httpPort = port;
        }
                        
        void setHTTPUser (String s) {
            httpUser = s;
        }
        
        void setManagementPort (int port) {
            managementPort = port;
        }
        
        void setManagementDomain (String s) {
            managementDomain = s;
        }
        
        void setManagementAgent (String s) {
            managementAgent = s;
        }
        
        public String getManagementKey() {
            return managementDomain + ":" + managementAgent;
        }
        
        public int getManagementPort() {
            return managementPort;
        }
        
        public String printManagementChannelState() {
            return managementChannelState.toString();
        }
        
        public boolean isManagementChannelConnected() {
            return (managementChannelState.isConnected());
        }
        
        public void managementChannelIsConnected(boolean b) {
            if (b)
                managementChannelState.setConnected();
            else
                managementChannelState.setDisconnected();
        }        
}
