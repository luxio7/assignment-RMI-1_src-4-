package carrentalagency;

import java.rmi.Remote;
import java.rmi.RemoteException;

import namingservice.INamingService;
import session.IManagerSession;
import session.IReservationSession;

public interface ICarRentalAgency extends Remote {
	
	public void endReservationSession(String sessionId) throws RemoteException;
	public void endManagerSession(String sessionId) throws RemoteException;
	public IReservationSession getReservationSession(String sessionId) throws RemoteException, IllegalArgumentException;
	public IManagerSession getManagerSession(String sessionId) throws RemoteException, IllegalArgumentException;
}
