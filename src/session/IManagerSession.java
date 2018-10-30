package session;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rental.CarRentalCompany;
import rental.CarType;

public interface IManagerSession extends Remote {

	public void addCarRentalCompany(String name, CarRentalCompany crc) throws RemoteException;
	public void UnRegisterCarRentalCompany(String name) throws RemoteException;
	public Integer numberOfReservationsByCarType(String cartype, String crc) throws RemoteException;
	public String getBestCustomer(String crc1) throws RemoteException;
	public CarType getMostPopularCarType(Integer year, CarRentalCompany crc);
	
}
