package rental.session;


import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IReservationSessionRemote extends Remote {
    void createQuote(ReservationConstraints constraints, String guest) throws RemoteException;
    List<Quote> getCurrentQuotes() throws  RemoteException;
    List<Reservation> confirmQuotes(String name) throws ReservationException, RemoteException;
    Set<String> getAvailableCarTypes(Date start, Date end) throws RemoteException;
    String getCheapestCarType(Date start,Date end) throws RemoteException;
    List<Reservation> getCurrentReservations();
    String getName();
}