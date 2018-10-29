package namingservice;

import java.rmi.Remote;
import java.util.Map;

import rental.CarRentalCompany;

public interface INamingService extends Remote{
	
	public static Map<String, CarRentalCompany> getRentals();
	public static CarRentalCompany getRental(String CrcName);
}
