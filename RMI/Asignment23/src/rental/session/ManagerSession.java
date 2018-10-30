package rental.session;


import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;
import server.NamingService;

import javax.naming.NameNotFoundException;
import java.rmi.RemoteException;
import java.util.*;

public class ManagerSession implements ManagerSessionRemote {
    @Override
    public void registerCarRentalCompany(CarRentalCompany company) {
        try {
            NamingService.registerCarRentalCompany(company);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unregisterCarRentalCompany(CarRentalCompany company) {
        try {
            NamingService.unregisterCarRentalCompany(company);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<CarRentalCompany> getRegisteredCarRentalCompanies() {
        return NamingService.getCarRentalCompanies();
    }

    @Override
    public Set<String> getCarInformation(String carRentalCompany) {
        Set<String> carTypes = new HashSet<>();
        for (CarRentalCompany crc : getRegisteredCarRentalCompanies()) {
            if (crc.getName().equals(carRentalCompany)) {
                for (CarType ct : crc.getAllCarTypes()) {
                    carTypes.add(ct.getName());
                }
            }
        }
        return  carTypes;
    }

    @Override
    public int getNumberOfReservationsByCarType(String carRentalCompany, String carType) {
        int resAmount=0;
        try {
          CarRentalCompany company =  NamingService.getCarRentalCompany(carRentalCompany);
            resAmount = company.getNumberOfReservationsForCarType(carType);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return resAmount;
    }

    @Override
    public String getBestCustomer() {
       // Set<String> companies = NamingService.getCarRentalCompanies();
        Map<String,Integer> klanten = new TreeMap<>();
        for (CarRentalCompany carRentalCompany: NamingService.getCarRentalCompanies()){
            for(Car car: carRentalCompany.getCars()){
                for(Reservation r : car.getReservations())
                {
                    Integer cur = klanten.get(r.getCarRenter());
                    klanten.put(r.getCarRenter(),cur==null?1:cur++);
                }
            }
        }
        Map.Entry<String,Integer> max = null;
        for(Map.Entry<String,Integer> e : klanten.entrySet())
            if(max == null || e.getValue() > max.getValue())
                max = e;
        return max.getKey();
    }

    @Override
    public CarType getMostPopularCarType(String carRentalCompany, int year) {
        int resAmount=0;
        CarType mostPopularCT=null;
        try {
            CarRentalCompany company = NamingService.getCarRentalCompany(carRentalCompany);
            for (CarType ct : company.getAllCarTypes()) {
                if (company.getNumberOfReservationsForCarTypeInYear(ct.getName(), year) > resAmount) {
                    resAmount = company.getNumberOfReservationsForCarTypeInYear(ct.getName(), year);
                    mostPopularCT = ct;
                }

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return mostPopularCT;
    }
}
