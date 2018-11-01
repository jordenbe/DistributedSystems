package rental.session;

import rental.CarType;
import server.IRemoteCarRentalCompany;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public interface ManagerSessionRemote {
    void registerCarRentalCompany(IRemoteCarRentalCompany company);
    void unregisterCarRentalCompany(IRemoteCarRentalCompany company);
    ArrayList<IRemoteCarRentalCompany> getRegisteredCarRentalCompanies();
    int getNumberOfReservationsByCarType(String carRentalCompany, String carType);
    int getNumberOfReservationsBy(String client);
    String getBestCustomer();
    CarType getMostPopularCarType(String carRentalCompany, int year);
    void setCompanyName(String name) throws RemoteException;
}
