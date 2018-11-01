package rental.session;


import rental.Car;
import rental.CarType;
import rental.Reservation;
import server.IRemoteCarRentalCompany;
import server.NamingService;

import java.rmi.RemoteException;
import java.util.*;

public class ManagerSession extends Session implements ManagerSessionRemote {

    public ManagerSession(String id) throws RemoteException {
        setId(id);
    }

    @Override
    public void registerCarRentalCompany(IRemoteCarRentalCompany company) {
        try {
            NamingService.registerCarRentalCompany(company);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unregisterCarRentalCompany(IRemoteCarRentalCompany company) {
        try {
            NamingService.unregisterCarRentalCompany(company);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<IRemoteCarRentalCompany> getRegisteredCarRentalCompanies() {
        return NamingService.getCarRentalCompanies();
    }

    @Override
    public int getNumberOfReservationsByCarType(String carRentalCompany, String carType)
    {
        try {
            IRemoteCarRentalCompany company = NamingService.getCarRentalCompany(carRentalCompany);
            return company.getNumberOfReservationsForCarType(carType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getNumberOfReservationsBy(String client) {
        try{
            int c=0;
            for(IRemoteCarRentalCompany cr : NamingService.getCarRentalCompanies()) {
                c += cr.getReservationsByRenter(client).size();
            }
            return c;
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getBestCustomer() {
        Map<String,Integer> klanten = new TreeMap<>();
        for (IRemoteCarRentalCompany carRentalCompany: NamingService.getCarRentalCompanies()){
            try {
                for(Car car: carRentalCompany.getAllCars()){
                    for(Reservation r : car.getReservations())
                    {
                        Integer cur = klanten.get(r.getCarRenter());
                        klanten.put(r.getCarRenter(),cur==null?1:cur++);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
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
            IRemoteCarRentalCompany company = NamingService.getCarRentalCompany(carRentalCompany);
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

    @Override
    public void setCompanyName(String name) throws RemoteException {
        super.setId(name);
    }
}
