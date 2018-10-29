package carrentalagency;

import namingservice.INamingService;

public class CarRentalAgency implements ICarRentalAgency{

	private INamingService namingService;

	public CarRentalAgency(INamingService namingService) {
		this.namingService = namingService;
	}

}
