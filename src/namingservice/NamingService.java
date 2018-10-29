package namingservice;

import java.util.HashMap;
import java.util.Map;

import rental.CarRentalCompany;
import rental.ICarRentalCompany;

public class NamingService implements INamingService{
	
	static Map<String, CarRentalCompany> registeredCRC = new HashMap();
	
	public void register(String name, CarRentalCompany crc){
		registeredCRC.put(name, crc);
	}
	
	public void unregister(String name){
		registeredCRC.remove(name);
	}
	public Map<String, CarRentalCompany> getRentals(){
		return registeredCRC;
	}
	public CarRentalCompany getRental(String CrcName){
		return registeredCRC.get(CrcName);
	}


}
