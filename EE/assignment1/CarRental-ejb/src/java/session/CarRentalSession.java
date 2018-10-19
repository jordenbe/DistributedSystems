package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;


@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    List<Quote> quotesList = new ArrayList();
    List<Reservation> reservationList = new ArrayList();
    
    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }
    
    public void createQuote(ReservationConstraints constraints, String guest){
       
        boolean found = false;
        for(String name:getAllRentalCompanies()){
            if(!found)
            {
                try{
                    CarRentalCompany company =  RentalStore.getRental(name);
                    Quote q = company.createQuote(constraints, guest);
                    found = true;
                    quotesList.add(q);
                }
                catch(ReservationException ex){}
            }
        }           
    }
  
    
    @Override
    public Set<String> checkAvailableCarTypes(Date start, Date end)
    {
        Set<String> carTypes = new HashSet<String>();
        for(String str : getAllRentalCompanies())
        {
            CarRentalCompany com = RentalStore.getRental(str);
           
            for (CarType ct:com.getAvailableCarTypes(start, end)){
                carTypes.add(ct.toString());
            }
        }
        return carTypes;
    }
    
    public List<Quote> getCurrentQuotes(){
        return quotesList;
    }
        
    public void confirmQuotes(String name) throws ReservationException
    {
        for(Quote quote :getCurrentQuotes()){
            CarRentalCompany company = RentalStore.getRental(quote.getRentalCompany());
            try{
                 if(quote.getCarRenter().equals(name))
                    reservationList.add(RentalStore.getRentals().get(quote.getRentalCompany()).confirmQuote(quote));
            }
            catch(ReservationException ex){
                for (Reservation res:reservationList){
                     RentalStore.getRental(res.getRentalCompany()).cancelReservation(res);            
                }
                throw ex;
            }
              
        } 
    }        
          
               
        

    @Override
    public List<Reservation> getCurrentReservations() {
        return reservationList;
    }

    
}
