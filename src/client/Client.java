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
		
		String carRentalCompanyName = "Hertz";
		
		Client client = new Client("simpleTrips", carRentalCompanyName);
		
		client.crc = (ICarRentalCompany) registry.lookup("rentalserver");
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
	
	public Client(String scriptFile, String carRentalCompanyName) {
		super(scriptFile);
	}
	
	/**
	 * Retrieve a quote for a given car type (tentative reservation).
	 * 
	 * @param	clientName 
	 * 			name of the client 
	 * @param 	start 
	 * 			start time for the quote
	 * @param 	end 
	 * 			end time for the quote
	 * @param 	carType 
	 * 			type of car to be reserved
	 * @param 	region
	 * 			region in which car must be available
	 * @return	the newly created quote
	 *  
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */

	protected Quote createQuote(String clientName, Date start, Date end,
			String carType, String region) throws Exception {
		ReservationConstraints constraints = new ReservationConstraints(start,end,carType,region);
		Quote quote = crc.createQuote(constraints, clientName);
		return quote;
	}

	/**
	 * Confirm the given quote to receive a final reservation of a car.
	 * 
	 * @param 	quote 
	 * 			the quote to be confirmed
	 * @return	the final reservation of a car
	 * 
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */

	protected Reservation confirmQuote(Quote quote) throws Exception {
		Reservation res = crc.confirmQuote(quote);
		return res;
		
		
	}
	


	/**
	 * Get the number of reservations for a particular car type.
	 * 
	 * @param 	carType 
	 * 			name of the car type
	 * @return 	number of reservations for the given car type
	 * 
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
//	@Override
//	protected int getNumberOfReservationsForCarType(String carType) throws Exception {
//		System.out.println(crc.getReservationsByType(carType));
//		return crc.getReservationsByType(carType);
//	}

	@Override
	protected ReservationSession getNewReservationSession(String name) throws Exception {
		ReservationSession resSes = new ReservationSession(this.namingservice);
		return resSes;
	}


	@Override
	protected ManagerSession getNewManagerSession(String name, String carRentalName) throws Exception {
		ManagerSession manSes = new ManagerSession(this.namingservice);
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