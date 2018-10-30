package rental.session;


import rental.CarRentalCompany;
import rental.session.ManagerSessionRemote;

import java.util.Set;

public class ManagerSession implements ManagerSessionRemote {
    @Override
    public void registerCarRentalCompany(CarRentalCompany company) {

    }

    @Override
    public void unregisterCarRentalCompany(CarRentalCompany company) {

    }

    @Override
    public Set<String> getRegisteredCarRentalCompanies() {
        return null;
    }

    @Override
    public Set<String> getCarInformation(String carRentalCompany) {
        return null;
    }

    @Override
    public int getNumberOfReservationsByCarType(String carRentalCompany, String carType) {
        return 0;
    }

    @Override
    public String getBestCustomer() {
        return null;
    }

    @Override
    public String getMostPopularCarType(String carRentalCompany, int year) {
        return null;
    }
}
