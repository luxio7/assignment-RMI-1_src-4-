package client; 
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import carrentalagency.CarRentalAgencyServer;
import namingservice.INamingService;
import namingservice.NamingServer;
import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import session.IReservationSession;
import session.ManagerSession;
import session.ReservationSession;

public class Client extends AbstractTestManagement<ReservationSession, ManagerSession> {
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
		ICarRentalCompany crc1 = (ICarRentalCompany) registry.lookup("Hertz");
		ICarRentalCompany crc2 = (ICarRentalCompany) registry.lookup("Dockx");
		namingservice1.register("Hertz", crc1);
		namingservice1.register("Dockx", crc2);
		
		
	
		// An example reservation scenario on car rental company 'Hertz' would be...
		
		client.run();
	}
	
	/***************
	 * CONSTRUCTOR *
	 ***************/
	
	public Client(String scriptFile) {
		super(scriptFile);
	}
	


	protected ReservationSession getNewReservationSession(String name) throws Exception {
		ReservationSession resSes = new ReservationSession(this.namingservice, "reservation_"+name);
		return resSes;
	}


	protected ManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {		
		ManagerSession manSes = new ManagerSession(this.namingservice,"reservation_"+name);
		return manSes;
	}
	
	protected void checkForAvailableCarTypes(ReservationSession session, Date start, Date end) throws Exception {
		List<CarType> cars = session.getAvailableCarTypes(start, end);
		for (CarType s : cars) {
		    System.out.println(s);
		}
		
	}
	
	protected void addQuoteToSession(ReservationSession session, String name, Date start, Date end, String carType,
			String region) throws Exception {
		ReservationConstraints constraint = new ReservationConstraints(start, end, carType, region);
		session.createQuote(constraint, name);
		
	}


	protected List<Reservation> confirmQuotes(ReservationSession session, String name) throws Exception {
		List<Reservation> res = session.confirmQuotes(session.getCurrentQuotes());
		return res;
	}

	protected int getNumberOfReservationsBy(ManagerSession ms, String clientName) throws Exception {
		return ms.getNumberReservationsBy(clientName);
	}


	protected int getNumberOfReservationsForCarType(ManagerSession ms, String carRentalName, String carType)
			throws Exception {
		System.out.println("nu zoeken naar via client" + carType);
		return ms.numberOfReservationsByCarType(carType, carRentalName);
	}


	@Override
	protected Set<String> getBestClients(ManagerSession ms) throws Exception {
		return ms.getBestCustomer();
	}

	@Override
	protected String getCheapestCarType(ReservationSession session, Date start, Date end, String region)
			throws Exception {
		return session.getCheapestCarType(start, end, region).getName();
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSession ms, String carRentalCompanyName, int year)
			throws Exception {
		
		return ms.getMostPopularCarType(year, this.namingservice.getRental(carRentalCompanyName));
	}
}