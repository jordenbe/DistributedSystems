package server;


import rental.CarRentalCompany;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class NamingService {

   private static ArrayList<CarRentalCompany> carRentalCompanies = new ArrayList<>();

    public static ArrayList<CarRentalCompany> getCarRentalCompanies() {
        return carRentalCompanies;
    }

    public static void registerCarRentalCompany(CarRentalCompany company) throws RemoteException {
        carRentalCompanies.add(company);
    }

    public static void unregisterCarRentalCompany(CarRentalCompany company) throws RemoteException {
        carRentalCompanies.remove(company);
    }

    public static CarRentalCompany getCarRentalCompany(String company) throws RemoteException {
        CarRentalCompany foundCompany = null;
        for (CarRentalCompany carRentalCompany : carRentalCompanies) {
            if (carRentalCompany.getName().equals(company)) {
                foundCompany = carRentalCompany;
            }
        }
        if (foundCompany == null) {
            throw new RemoteException("Company not found");
        }
        return  foundCompany;
    }

}
