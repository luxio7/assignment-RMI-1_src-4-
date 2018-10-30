package carrentalagency;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import namingservice.INamingService;

public class CarRentalAgencyServer {
	
	/**
	 * connects to the namingservice so it knows about all the car rental companies
	 * after this it creates a stub for a carrentalagency that manages all the sessions (managersessions + reservationsessions)
	 */
	public static void main(String[] args) throws RemoteException, NotBoundException{
		System.setSecurityManager(null);
		
		Registry registry = LocateRegistry.getRegistry();
		INamingService namingService = (INamingService) registry.lookup("namingserver");
		ICarRentalAgency crc = new CarRentalAgency(namingService);
		
        ICarRentalAgency stub = (ICarRentalAgency) UnicastRemoteObject.exportObject(crc, 0);
        registry.rebind("carrentalagency", stub);
	}
	
	
	
	
}
