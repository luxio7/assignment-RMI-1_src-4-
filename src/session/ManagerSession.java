package session;


import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import namingservice.INamingService;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Reservation;

public class ManagerSession extends AbstractSession implements IManagerSession{
	
	
	public ManagerSession(INamingService namingservice, String id) {
		super(namingservice, id);
	}
	
	public void addCarRentalCompany(String name, CarRentalCompany crc) throws RemoteException{
		try{
			this.namingService.register(name, crc);
		} catch (RemoteException e){
			throw new RemoteException();
		}
		
	}

	public void UnRegisterCarRentalCompany(String name) throws RemoteException{
		try{
			this.namingService.unregister(name);
		} catch (RemoteException e){
			throw new RemoteException();
		}
	}
	
	public Integer numberOfReservationsByCarType(String cartype, String crc) throws RemoteException{
		ICarRentalCompany crc1 = this.getNamingService().getRental(crc);
		return crc1.getReservationsByType(cartype);
	}
	
    public int getNumberReservationsBy(String clientName) throws RemoteException {
        int counter = 0;
        
        List<ICarRentalCompany> crcs = this.getNamingService().getAllCompanies();
        for (ICarRentalCompany crc : crcs){
        	counter += crc.getReservationsBy(clientName).size();
        }
        
        return counter;
    }
	public Set<String> getBestCustomer() throws RemoteException{
        List<ICarRentalCompany> crc = namingService.getAllCompanies();
        
        
        List<Car> cars = new ArrayList<Car>();
        for (ICarRentalCompany crcLoop : crc){
        	cars.addAll(crcLoop.getCars());
        }
        
        
        
        Map<String,Integer> amountOfReservations = new HashMap<String, Integer>();
        for(Car c : cars){
            for(Reservation r : c.getAllReservations()){
                if(amountOfReservations.containsKey(r.getCarRenter())){
                    Integer res = amountOfReservations.get(r.getCarRenter());
                    amountOfReservations.put(r.getCarRenter(), res +1);
                }
                else{
                	amountOfReservations.put(r.getCarRenter(),1);
                    
                }
            }
        }
        Integer max = 0;
        Set<String> clients = new HashSet<String>();
        for(String client : amountOfReservations.keySet()){
            if(amountOfReservations.get(client) == max){
                clients.add(client);
            } else if(amountOfReservations.get(client) > max) {
            	max = amountOfReservations.get(client);
            	clients = new HashSet<String>();
            	clients.add(client);
            }
        }
        return clients;
    }
	
	
	public CarType getMostPopularCarType(Integer year, ICarRentalCompany crc) throws RemoteException{
		Map<CarType, Integer> reservations = new HashMap<CarType, Integer>();
		List<Reservation> reservationsSpecificCar = new ArrayList<Reservation>();
		List<Reservation> goodReservations = new ArrayList<Reservation>();
		
		for (Car car : crc.getCars()){
			
			reservationsSpecificCar = car.getAllReservations();
			
			for (Reservation carReservation: reservationsSpecificCar){
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(carReservation.getStartDate());
				int reservationYear = calendar.get(Calendar.YEAR);
				
				if (reservationYear == year){
					goodReservations.add(carReservation);
				}
			}
			
			if (reservations.get(car.getType()) == null){
				reservations.put(car.getType(), goodReservations.size());
			} else {
				reservations.put(car.getType(), reservations.get(car.getType())+goodReservations.size());
			}	
		}
		
		Integer maximumReservations = 0;
		CarType mostPopularCarType = null;
		for (CarType key: reservations.keySet()){
			if (reservations.get(key) > maximumReservations){
				maximumReservations = reservations.get(key);
				mostPopularCarType = key;
			}
		}
		
		return mostPopularCarType;
	}

	
	
}
