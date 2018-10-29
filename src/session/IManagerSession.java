package session;

import java.rmi.Remote;

import rental.CarRentalCompany;
import rental.CarType;

public interface IManagerSession extends Remote {

	public void addCarRentalCompany(String name, CarRentalCompany crc);
	public void UnRegisterCarRentalCompany(String name);
	public Integer numberOfReservationsByCarType(String cartype, CarRentalCompany crc);
	public String getBestCustomer(String crc1);
	public CarType getMostPopularCarType(Integer year, CarRentalCompany crc);
	
}
