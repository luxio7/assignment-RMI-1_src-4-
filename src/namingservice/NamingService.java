package namingservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public List<CarRentalCompany> getAllCompanies(){
		List<CarRentalCompany> list = new ArrayList();
		for(CarRentalCompany crc : registeredCRC.values()){
			list.add(crc);
		}
		return list;
		
	}

}
