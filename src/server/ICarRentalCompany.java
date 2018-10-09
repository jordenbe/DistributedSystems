package server;


import rental.CarType;
import rental.Quote;
import rental.ReservationConstraints;
import rental.ReservationException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Set;

public interface ICarRentalCompany extends Remote {

    public Set<CarType> getAvailableCarTypes(Date start, Date end);
    public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException;
}
