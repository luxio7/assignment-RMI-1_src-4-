package session;

import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
	

    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(namingService.getRentals().keySet());
    }
	
    private List<Quote> quotes = new ArrayList();
    
    public void createQuote(ReservationConstraints constraint, String client) throws ReservationException{
        boolean go = true;
        for (String s: getAllRentalCompanies()){
              try{
                  CarRentalCompany crc = namingService.getRental(s);
                  Quote quote = crc.createQuote(constraint, client);
                  quotes.add(quote);
                  go = false;
              }
              catch (ReservationException e){
                  
              }
        
        }
        if (go){
            throw new ReservationException("error");
        }
      }
    
    public List<Quote> getCurrentQuotes(){
        return quotes;
    }
    
    
    public List<Reservation> confirmQuotes(List<Quote> quotes) throws ReservationException{
        List<Reservation> res = new ArrayList<Reservation>();
        for (Quote q: quotes){
            try{
                CarRentalCompany crc = namingService.getRental(q.getRentalCompany());
                Reservation reservation = crc.confirmQuote(q);
                res.add(reservation);
            }
            catch(ReservationException e){
                for(Reservation r : res ){
                    CarRentalCompany crc = namingService.getRental(r.getRentalCompany());
                    crc.cancelReservation(r);
                }
                
                throw new ReservationException("All reservations cancelled");
            }
        }
        return res;
    }
    
    
    public List<CarType> getAvailableCarTypes(Date start, Date end){
        List<CarType> cartype = new ArrayList();      
        for(String crc : getAllRentalCompanies()){
            CarRentalCompany crc1 = namingService.getRental(crc);
            for(CarType ct : crc1.getAvailableCarTypes(start, end)){
                cartype.add(ct);
            }
        }
        return cartype;
    }
    
    public CarType getCheapestCarType(Date start, Date end, String region){
    	
        List<CarType> availableCartypes = new ArrayList();
        List<CarRentalCompany> goodCarRentalCompany = new ArrayList();
        CarType cheapestCarType = null;
        
        //de juiste rentalcompanie krijgen
        for(String crc : getAllRentalCompanies()){
            CarRentalCompany crc1 = namingService.getRental(crc);
            for (String regionToCheck : crc1.getRegions()){
            	if (regionToCheck == region) {
            		goodCarRentalCompany.add(crc1);
            	}
            }
        }
        
        //de juiste cartypes krijgen van de car rental companies en de goedkoopste opslaan
        double minimumPrice = Double.POSITIVE_INFINITY;
        
        for(CarRentalCompany crc : goodCarRentalCompany){
        	
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
