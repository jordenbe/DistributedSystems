package rental.session;

import rental.*;
import rental.session.IReservationSessionRemote;
import server.IRemoteCarRentalCompany;
import server.NamingService;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class ReservationSessionRemote extends Session implements IReservationSessionRemote {
    String name;
    List<Quote> quotesList = new ArrayList();
    List<Reservation> reservationList = new ArrayList();


    public ReservationSessionRemote(String name) {
        this.name = name;
    }

    @Override
    public synchronized void createQuote(ReservationConstraints constraints, String guest) {
        boolean found = false;
        List<IRemoteCarRentalCompany> companies = NamingService.getCarRentalCompanies();
        Collections.shuffle(companies);
        for(IRemoteCarRentalCompany company : companies) {
            if(!found)
            {
                try{
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
    public synchronized List<Reservation> confirmQuotes(String name) throws ReservationException, RemoteException {
        for(Quote quote :getCurrentQuotes()){
            IRemoteCarRentalCompany company = NamingService.getCarRentalCompany(quote.getRentalCompany());
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
        return  reservationList;

    }

    @Override
    public Set<String> getAvailableCarTypes(Date start, Date end) throws RemoteException {
        Set<String> carTypes = new HashSet<>();
        for(IRemoteCarRentalCompany company : NamingService.getCarRentalCompanies())
        {
            for (CarType type : company.getAvailableCarTypes(start, end)){
                carTypes.add(type.getName());
            }
        }
        return carTypes;
    }

    @Override
    public String getCheapestCarType(Date start, Date end) throws RemoteException {
       String cartype = "";
       double cheapestPrice = Double.MAX_VALUE;

        for(IRemoteCarRentalCompany company : NamingService.getCarRentalCompanies())
        {
            for (CarType type : company.getAvailableCarTypes(start, end)){
                if (type.getRentalPricePerDay() < cheapestPrice) {
                    cartype = type.getName();
                    cheapestPrice =  type.getRentalPricePerDay();
                }
            }
        }
        return  cartype;
    }

    @Override
    public List<Reservation> getCurrentReservations() {
        return reservationList;
    }

    @Override
    public String getName() {
        return name;
    }
}
