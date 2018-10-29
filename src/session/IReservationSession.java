package session;

import java.rmi.Remote;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public interface IReservationSession extends Remote {
	
	public Set<String> getAllRentalCompanies();
	public void createQuote(ReservationConstraints constraint, String client) throws ReservationException;
	public List<Quote> getCurrentQuotes();
	public List<Reservation> confirmQuotes(List<Quote> quotes) throws ReservationException;
	public List<CarType> getAvailableCarTypes(Date start, Date end);
	public CarType getCheapestCarType(Date start, Date end, String region);
	
}
