package rental.session;

import rental.CarRentalCompany;
import rental.CarType;

import java.util.ArrayList;
import java.util.Set;

public interface ManagerSessionRemote {

    void registerCarRentalCompany(CarRentalCompany company);

    void unregisterCarRentalCompany(CarRentalCompany company);

    ArrayList<CarRentalCompany> getRegisteredCarRentalCompanies();

    Set<String> getCarInformation(String carRentalCompany);

    int getNumberOfReservationsByCarType(String carRentalCompany, String carType);
    int getNumberOfReservationsBy(String client);

    String getBestCustomer();

    CarType getMostPopularCarType(String carRentalCompany, int year);
}
