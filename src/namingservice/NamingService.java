package namingservice;

import java.util.HashMap;
import java.util.Map;

import rental.CarRentalCompany;
import rental.ICarRentalCompany;

public class NamingService implements INamingService{
	
	Map<String, ICarRentalCompany> registeredCRC = new HashMap();
	
	public void register(String name, ICarRentalCompany crc){
		registeredCRC.put(name, crc);
	}
	
	public void unregister(String name){
		registeredCRC.remove(name);
	}
}
