package carrentalagency;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import namingservice.INamingService;
import session.IManagerSession;
import session.IReservationSession;
import session.ManagerSession;
import session.ReservationSession;

public class CarRentalAgency implements ICarRentalAgency{

	private INamingService namingService;

	public CarRentalAgency(INamingService namingService) {
		this.namingService = namingService;
	}
	private Map<String, ReservationSession> activeReservationSessions = new HashMap<String, ReservationSession>();
	private Map<String, ManagerSession> activeManagerSessions = new HashMap<String, ManagerSession>();
	
	public void endReservationSession(String sessionId) 
			throws RemoteException {
		synchronized (this.activeReservationSessions) {
			this.activeReservationSessions.remove(sessionId);
		}
	}
	
	public void endManagerSession(String sessionId) 
			throws RemoteException {
		synchronized (this.activeManagerSessions) {
			this.activeManagerSessions.remove(sessionId);
		}
	}
	
	public IReservationSession getReservationSession(String sessionId, String clientName)
			throws RemoteException, IllegalArgumentException {
		
		if (sessionId == null) {
			throw new IllegalArgumentException();
		}
		IReservationSession session = this.activeReservationSessions.get(sessionId);
		
		if (session != null) {
			return session;
		} else {
			ReservationSession newSession = new ReservationSession(this.namingService, sessionId, clientName);
			this.activeReservationSessions.put(sessionId, newSession);
			return (IReservationSession) UnicastRemoteObject.exportObject(newSession, 0);
		}
	}
	
	public IManagerSession getManagerSession(String sessionId)
			throws RemoteException, IllegalArgumentException {
		
		if (sessionId == null) {
			throw new IllegalArgumentException();
		}
		
		IManagerSession session = this.activeManagerSessions.get(sessionId);
		
		if (session != null) {
			return session;
		} else {
			ManagerSession newSession = new ManagerSession(this.namingService, sessionId);
			this.activeManagerSessions.put(sessionId, newSession);
			return (IManagerSession) UnicastRemoteObject.exportObject(newSession, 0);
		}
	}
	
}
