package client;

import rental.*;
import rental.session.ReservationSessionRemote;
import server.NamingService;

import java.rmi.RemoteException;
import java.util.*;

public class ReservationSession implements ReservationSessionRemote {

    List<Quote> quotesList = new ArrayList();
    List<Reservation> reservationList = new ArrayList();

    @Override
    public  void createQuote(ReservationConstraints constraints, String guest) {
        boolean found = false;
        for(CarRentalCompany company:NamingService.getCarRentalCompanies()){
            if(!found)
            {
                try{
                //    CarRentalCompany company =  NamingService.getCarRentalCompany(name);
                    Quote q = company.createQuote(constraints, guest);
                    found = true;
                    quotesList.add(q);
                }
                catch(Exception ex){
                }
            }
        }
    }

    @Override
    public List<Quote> getCurrentQuotes() {
        return quotesList;
    }

    @Override
    public synchronized void confirmQuotes(String name) throws ReservationException, RemoteException {
        for(Quote quote :getCurrentQuotes()){
            CarRentalCompany company = NamingService.getCarRentalCompany(quote.getRentalCompany());
            try{
                if(quote.getCarRenter().equals(name))
                    reservationList.add(company.confirmQuote(quote));
            }
            catch(Exception ex){
                for (Reservation res:reservationList){
                    NamingService.getCarRentalCompany(res.getRentalCompany()).cancelReservation(res);
                }
                throw ex;
            }

        }

    }

    @Override
    public Set<String> getAvailableCarTypes(Date start, Date end) throws RemoteException {
        Set<String> carTypes = new HashSet<>();
        for(CarRentalCompany com : NamingService.getCarRentalCompanies())
        {
            for (CarType ct:com.getAvailableCarTypes(start, end)){
                carTypes.add(ct.toString());
            }
        }
        return carTypes;
    }

    @Override
    public String getCheapestCarType(Date start, Date end) {
        return null;
    }



    @Override
    public List<Reservation> getCurrentReservations() {
        return null;
    }
}
