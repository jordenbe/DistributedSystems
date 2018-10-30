package rental.session;

import rental.CarRentalCompany;

import java.util.Date;
import java.util.Set;

public interface ManagerSessionRemote {

    void registerCarRentalCompany(CarRentalCompany company);

    void unregisterCarRentalCompany(CarRentalCompany company);

    Set<String> getRegisteredCarRentalCompanies();

    Set<String> getCarInformation(String carRentalCompany);

    int getNumberOfReservationsByCarType(String carRentalCompany, String carType);

    String getBestCustomer();

    String getMostPopularCarType(String carRentalCompany, int year);
}
