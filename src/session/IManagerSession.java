package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;

public interface IManagerSession extends Remote {

	public void addCarRentalCompany(String name, CarRentalCompany crc) throws RemoteException;
	public void UnRegisterCarRentalCompany(String name) throws RemoteException;
	public Integer numberOfReservationsByCarType(String cartype, String crc) throws RemoteException;
	public Set<String> getBestCustomer() throws RemoteException;
	public CarType getMostPopularCarType(Integer year, ICarRentalCompany crc) throws RemoteException;
	
}
