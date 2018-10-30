package session;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;

import client.Client;
import namingservice.INamingService;
import namingservice.NamingServer;
import namingservice.NamingService;
import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class ReservationSession extends AbstractSession implements IReservationSession {
	
	public ReservationSession(INamingService namingservice, String id){
		super(namingservice, id);
	}
	

    public Set<String> getAllRentalCompanies() throws RemoteException{
    	return new HashSet<String>(namingService.getRentals().keySet());
    }
	
    private List<Quote> quotes = new ArrayList();
    
    public void createQuote(ReservationConstraints constraint, String client) throws Exception{
        boolean go = false;
        Exception reservationexception = null;
        
        if (getAllRentalCompanies().size()!=0){
        	go = true;
        }
        
        System.out.println("dit zijn alle gevonden crc's voor: " + client);
        Set<String> t = getAllRentalCompanies();
        System.out.println(t);
        
        //hij vindt geen rental companies bij de eerste voorbeelden
        
        for (String s: getAllRentalCompanies()){
              try{
                  ICarRentalCompany crc = namingService.getRental(s);
                  Quote quote = crc.createQuote(constraint, client);
                  quotes.add(quote);
                  System.out.println("er is een quote geadd voor " + client);
                  go = false;
              }
              catch (Exception ex){
            	  reservationexception = ex;
              }
        
        }
        if (go){
        	System.out.println("reservaties geannuleerd voor " + client);
            throw reservationexception;
        }
      }
    
    public List<Quote> getCurrentQuotes(){
        return quotes;
    }
    
    
    public List<Reservation> confirmQuotes(List<Quote> quotes) throws ReservationException, RemoteException{
        List<Reservation> res = new ArrayList<Reservation>();
        for (Quote q: quotes){
            try{
                ICarRentalCompany crc = namingService.getRental(q.getRentalCompany());
                System.out.println("de crc die misschien ne nullpointer geeft");
                System.out.println(crc);
                System.out.println("confirming quote in reservation session -> confrim quotes..");
                System.out.println(q);
                Reservation reservation = crc.confirmQuote(q);
                System.out.println("hij geraakt na confirmQuote");
                res.add(reservation);
                System.out.println("..quote is geconfirmed");
            }
            catch(ReservationException e){
            	System.out.println("..quote is NIET geconfirmed");
                for(Reservation r : res ){
                    ICarRentalCompany crc = namingService.getRental(r.getRentalCompany());
                    crc.cancelReservation(r);
                }
                
                throw new ReservationException("All reservations cancelled");
            }
        }
        return res;
    }
    
    
    public List<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException{
        List<CarType> cartype = new ArrayList();      
        for(String crc : getAllRentalCompanies()){
            ICarRentalCompany crc1 = namingService.getRental(crc);
            for(CarType ct : crc1.getAvailableCarTypes(start, end)){
                cartype.add(ct);
            }
        }
        return cartype;
    }
    
    public CarType getCheapestCarType(Date start, Date end, String region) throws Exception{
    	
        List<CarType> availableCartypes = new ArrayList();
        List<ICarRentalCompany> goodCarRentalCompany = new ArrayList();
        CarType cheapestCarType = null;
        
        System.out.println("voor een rentalcompanie krijgen");
        System.out.println("check op region: " + region);
        
        //de juiste rentalcompanie krijgen
        for(String crc : getAllRentalCompanies()){
            ICarRentalCompany crc1 = namingService.getRental(crc);
            for (String regionToCheck : crc1.getRegions()){
            	System.out.println(regionToCheck);
            	if (regionToCheck.equals(region)) {
            		System.out.println("toevoegen");
            		goodCarRentalCompany.add(crc1);
            		System.out.println(goodCarRentalCompany);
            	}
            }
        }
        
        System.out.println("voor cartypes krijgen");
        System.out.println(goodCarRentalCompany);
        
        if (goodCarRentalCompany.size() == 0) {
        	throw new Exception();
        }
        
        //de juiste cartypes krijgen van de car rental companies en de goedkoopste opslaan
        double minimumPrice = Double.POSITIVE_INFINITY;
        
        for(ICarRentalCompany crc : goodCarRentalCompany){
        	
            for(CarType ct : crc.getAvailableCarTypes(start, end)){
            	if (ct.getRentalPricePerDay() < minimumPrice){
            		cheapestCarType = ct;
            		minimumPrice = ct.getRentalPricePerDay();
            	}
            }
        }
        return cheapestCarType;
    }
    
}
