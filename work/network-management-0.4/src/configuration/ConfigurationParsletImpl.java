
// This is auto-generated. We don't use it.

package configuration;

import org.xml.sax.*;

public class ConfigurationParsletImpl implements ConfigurationParslet {
    
 

    public boolean addressConvertor(java.lang.String data) throws SAXException {
        return true;
    }    
    
    public java.lang.String addressElementConvertor(java.lang.String data) throws SAXException {
        try {
            return data.trim();
        } catch (IllegalArgumentException ex) {
            throw new SAXException("address_Element(" + data.trim() + ")", ex);
        }        
    }
    
    public boolean administrationConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public boolean connectionConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public boolean destinationConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public int distanceConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("distance(" + data.trim() + ")", ex);
        }        
    }
    
    public java.lang.String hostConvertor(java.lang.String data) throws SAXException {
       try {
            return data.trim();
       } catch (IllegalArgumentException ex) {
        throw new SAXException("host(" + data.trim() + ")", ex);
       }        
    }
    
    public java.lang.String httpPasswordConvertor(java.lang.String data) throws SAXException {
       try {
            return data.trim();
       } catch (IllegalArgumentException ex) {
        throw new SAXException("httpPassword(" + data.trim() + ")", ex);
       }        
    }
    
    public int httpPortConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("httpPort(" + data.trim() + ")", ex);
        }        
    }
    
    public java.lang.String httpUserConvertor(java.lang.String data) throws SAXException {
       try {
            return data.trim();
       } catch (IllegalArgumentException ex) {
            throw new SAXException("httpUser(" + data.trim() + ")", ex);
       }        
    }
    
    public boolean interfaceConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public int listenerPortConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("listenerPort(" + data.trim() + ")", ex);
        }          
    }
    
    public java.lang.String managementAgentConvertor(java.lang.String data) throws SAXException {
        try {
            return data.trim();
        } catch (IllegalArgumentException ex) {
            throw new SAXException("managementAgent(" + data.trim() + ")", ex);
        }          
    }
    
    public java.lang.String managementDomainConvertor(java.lang.String data) throws SAXException {
        try {
            return data.trim();
        } catch (IllegalArgumentException ex) {
            throw new SAXException("managementDomain(" + data.trim() + ")", ex);
        }         
    }
    
    public boolean managementKeyConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public int managementPortConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("managementPort(" + data.trim() + ")", ex);
        }        
    }
    
    public int metricConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("metric(" + data.trim() + ")", ex);
        }         
    }
    
    public boolean networkConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public boolean routerConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
    public int routerIdConvertor(java.lang.String data) throws SAXException {
        try {
            return Integer.parseInt(data.trim());
        } catch (IllegalArgumentException ex) {
            throw new SAXException("routerId(" + data.trim() + ")", ex);
        }         
    }
    
    public boolean sourceConvertor(java.lang.String data) throws SAXException {
        return true;
    }
    
}

