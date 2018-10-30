package client; 
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import carrentalagency.CarRentalAgencyServer;
import namingservice.INamingService;
import namingservice.NamingServer;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import session.IReservationSession;
import session.ManagerSession;
import session.ReservationSession;

public class Client extends AbstractTestAgency<ReservationSession, ManagerSession> {
	private INamingService namingservice;
	
	/********
	 * MAIN *
	 ********/
	
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);

		Client client = new Client("trips");
		
		NamingServer.main(args);
		CarRentalAgencyServer.main(args);
		INamingService namingservice1 = (INamingService) registry.lookup("namingserver");
		client.namingservice = namingservice1;
		
	
		// An example reservation scenario on car rental company 'Hertz' would be...
		
		client.run();
	}
	
	/***************
	 * CONSTRUCTOR *
	 ***************/
	
	public Client(String scriptFile) {
		super(scriptFile);
	}
	


	@Override
	protected ReservationSession getNewReservationSession(String name) throws Exception {
		ReservationSession resSes = new ReservationSession(this.namingservice, "reservation_"+name);
		return resSes;
	}


	@Override
	protected ManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
		ManagerSession manSes = new ManagerSession(this.namingservice,"reservation_"+name);
		return manSes;
	}
	
	@Override
	protected void checkForAvailableCarTypes(ReservationSession session, Date start, Date end) throws Exception {
		List<CarType> cars = session.getAvailableCarTypes(start, end);
		for (CarType s : cars) {
		    System.out.println(s);
		}
		
	}

	@Override
	protected void addQuoteToSession(ReservationSession session, String name, Date start, Date end, String carType,
			String region) throws Exception {
		ReservationConstraints constraint = new ReservationConstraints(start, end, carType, region);
		session.createQuote(constraint, name);
		
	}

	@Override
	protected List<Reservation> confirmQuotes(ReservationSession session, String name) throws Exception {
		List<Reservation> res = session.confirmQuotes(session.getCurrentQuotes());
		return res;
	}

	@Override
	protected int getNumberOfReservationsBy(ManagerSession ms, String clientName) throws Exception {
		return ms.getNumberReservationsBy(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSession ms, String carRentalName, String carType)
			throws Exception {
		return ms.numberOfReservationsByCarType(carType, carRentalName);
	}
}