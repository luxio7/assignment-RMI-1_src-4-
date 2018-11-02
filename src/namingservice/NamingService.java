package namingservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rental.CarRentalCompany;
import rental.ICarRentalCompany;

public class NamingService implements INamingService{
	
	static Map<String, ICarRentalCompany> registeredCRC = new HashMap<String, ICarRentalCompany>();
	
	public synchronized void register(String name, ICarRentalCompany crc) throws RemoteException{
		registeredCRC.put(name, crc);
	}
	
	public synchronized void unregister(String name){
		registeredCRC.remove(name);
	}
	public Map<String, ICarRentalCompany> getRentals(){
		return registeredCRC;
	}
	public ICarRentalCompany getRental(String CrcName){
		return registeredCRC.get(CrcName);
	}
	
	public List<ICarRentalCompany> getAllCompanies(){
		List<ICarRentalCompany> list = new ArrayList();
		for(ICarRentalCompany crc : registeredCRC.values()){
			list.add(crc);
		}
		return list;
		
	}

}
