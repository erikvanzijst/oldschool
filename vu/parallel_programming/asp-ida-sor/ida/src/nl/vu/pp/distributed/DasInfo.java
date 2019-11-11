package nl.vu.pp.distributed;

import java.util.*;
import java.net.*;
import java.rmi.*;


public class DasInfo {

    private int total_hosts;
    private int host_number;
    private String host_name;
    private String[] host_names;


    public DasInfo() throws RuntimeException {
	String temp;
	Properties p = System.getProperties();
    	total_hosts = getIntProperty(p, "prun_total_hosts");
	host_number = getIntProperty(p, "prun_host_number");
	temp = p.getProperty("prun_host_names");
	if (host_number >= total_hosts || host_number < 0 || total_hosts < 1)
	    throw new RuntimeException("Sanity check on host numbers failed!");

	host_names = new String[total_hosts];
	StringTokenizer tok = new StringTokenizer(temp, " ", false);
	for (int i = 0; i < total_hosts; i++) {	    
	    String t = tok.nextToken();	    
	    try {
		/*
		  This looks weird, but is required to get the entire hostname
		  ie. 'java.sun.com' instead of just 'java'.
		*/
		InetAddress adres = InetAddress.getByName(t);
		adres = InetAddress.getByName(adres.getHostAddress());
		host_names[i] = adres.getHostName();
	    } catch (Exception e) {
		throw new RuntimeException("Could not find host name " +
					   t + " " + e);
	    }
	}
	host_name = host_names[host_number];
    }


    private int getIntProperty(Properties p, String name) throws RuntimeException {
	String temp = p.getProperty(name);
	if (temp == null)
	    throw new RuntimeException("Property " + name + " not found !");
	return Integer.parseInt(temp);
    }


    public String hostName() {
	return host_name;
    }


    public int hostNumber() {
	return host_number;
    }


    public int totalHosts() {
	return total_hosts;
    }

 
    public String getHost(int num) {
	if (num < 0 || num >= total_hosts)
	    return null;
	return host_names[num];
    }


    public String[] getAllHosts() {
	String [] temp = new String[total_hosts];
	for (int i = 0; i < total_hosts; i++)
	    temp[i] = host_names[i];
	return temp;
    }


    public String toString() {
    	String temp = "\n------ DasInfo ------\n";
	temp += "Total hosts   : " + total_hosts + "\n";
	temp += "My hostnumber : " + host_number + "\n";
	temp += "My hostname   : " + host_name + "\n";
	temp += "All Hosts     : ";
	for (int i = 0; i < total_hosts; i++) {
	    temp += host_names[i] + "\n";
	    if (i != (total_hosts - 1))
		temp += "              : ";
	}
	temp += "---------------------\n";
	return temp;
    }
}
