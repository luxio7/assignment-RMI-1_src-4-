package namingservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NamingServer {
	public static void main(String[] args) throws RemoteException{
		System.setSecurityManager(null);
		INamingService ns = new NamingService();
		INamingService stub = (INamingService) UnicastRemoteObject.exportObject((Remote) ns,0);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("namingserver", stub);
	}

}
