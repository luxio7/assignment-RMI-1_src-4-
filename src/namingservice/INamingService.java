package namingservice;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rental.CarRentalCompany;
import rental.ICarRentalCompany;

public interface INamingService extends Remote{
	
	static Map<String, ICarRentalCompany> registeredCRC = new HashMap();
	public void register(String name, CarRentalCompany crc);
	public void unregister(String name);
	public Map<String, CarRentalCompany> getRentals();
	public CarRentalCompany getRental(String CrcName);
	public List<CarRentalCompany> getAllCompanies();
}
