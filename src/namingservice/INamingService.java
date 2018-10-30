package namingservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rental.CarRentalCompany;
import rental.ICarRentalCompany;

public interface INamingService extends Remote{
	
	static Map<String, ICarRentalCompany> registeredCRC = new HashMap();
	public void register(String name, CarRentalCompany crc) throws RemoteException;
	public void unregister(String name) throws RemoteException;
	public Map<String, CarRentalCompany> getRentals() throws RemoteException;
	public CarRentalCompany getRental(String CrcName) throws RemoteException;
	public List<CarRentalCompany> getAllCompanies() throws RemoteException;
}
