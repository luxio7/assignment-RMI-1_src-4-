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
	public void register(String name, ICarRentalCompany crc2) throws RemoteException;
	public void unregister(String name) throws RemoteException;
	public Map<String, ICarRentalCompany> getRentals() throws RemoteException;
	public ICarRentalCompany getRental(String CrcName) throws RemoteException;
	public List<ICarRentalCompany> getAllCompanies() throws RemoteException;
}
