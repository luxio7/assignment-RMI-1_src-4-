package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import namingservice.INamingService;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class ReservationSession implements IReservationSession{

    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(INamingService.getRentals().keySet());
    }
	
    private List<Quote> quotes = new ArrayList();
    
    public void createQuote(ReservationConstraints constraint, String client) throws ReservationException{
        boolean go = true;
        for (String s: getAllRentalCompanies()){
              try{
                  CarRentalCompany crc = INamingService.getRental(s);
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
                CarRentalCompany crc = INamingService.getRental(q.getRentalCompany());
                Reservation reservation = crc.confirmQuote(q);
                res.add(reservation);
            }
            catch(ReservationException e){
                for(Reservation r : res ){
                    CarRentalCompany crc = INamingService.getRental(r.getRentalCompany());
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
            CarRentalCompany crc1 = INamingService.getRental(crc);
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
            CarRentalCompany crc1 = INamingService.getRental(crc);
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
