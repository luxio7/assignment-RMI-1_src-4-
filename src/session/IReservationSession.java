package session;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public interface IReservationSession extends Remote {
	
	public Set<String> getAllRentalCompanies() throws RemoteException;
	public void createQuote(ReservationConstraints constraint, String client) throws Exception;
	public List<Quote> getCurrentQuotes();
	public List<Reservation> confirmQuotes(List<Quote> quotes) throws ReservationException, RemoteException;
	public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	public CarType getCheapestCarType(Date start, Date end, String region) throws Exception;
	
}
