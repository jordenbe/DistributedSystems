package server;


import rental.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IRemoteCarRentalCompany extends Remote {
    String getName() throws RemoteException;
    Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
    Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException,RemoteException;
    Reservation confirmQuote(Quote quote) throws ReservationException,RemoteException;
    List<Reservation> getReservationsByRenter(String clientName) throws RemoteException;
    int getNumberOfReservationsForCarType(String carType) throws RemoteException;
    String getId() throws RemoteException;
    void cancelReservation(Reservation reservation) throws RemoteException;
    Collection<CarType> getAllCarTypes() throws RemoteException;
    List<Car> getAllCars() throws RemoteException;
    int getNumberOfReservationsForCarTypeInYear(String type, int year) throws RemoteException;
}
