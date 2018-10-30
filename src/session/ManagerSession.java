package session;


import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			System.out.println("hij gaat een crc proberen toe te voegen in managersession -> add car rental company");
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
        Map<String, ICarRentalCompany> rentals = this.getNamingService().registeredCRC;
        for (ICarRentalCompany crc: rentals.values()) {
            counter += crc.getReservationsBy(clientName).size();
        }
        return counter;
    }
	public String getBestCustomer(String crc1) throws RemoteException{
        ICarRentalCompany crc = namingService.getRental(crc1);
        Collection<Car> cars = crc.getCars();
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(Car c : cars){
            for(Reservation r : c.getAllReservations()){
                if(map.containsKey(r.getCarRenter())){
                    Integer res = map.get(r.getCarRenter());
                    map.put(r.getCarRenter(), res +1);
                }
                else{
                    map.put(r.getCarRenter(),1);
                    
                }
            }
        }
        Integer max = 0;
        String clien="";
        for(String client : map.keySet()){
            if(map.get(client)>max){
                max = map.get(client);
                clien = client;
            }
        }
        return clien;
    }
	
	
	public CarType getMostPopularCarType(Integer year, CarRentalCompany crc){
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
