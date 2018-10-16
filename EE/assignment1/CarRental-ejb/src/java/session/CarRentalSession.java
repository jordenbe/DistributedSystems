package session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
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
      for(String name:getAllRentalCompanies()){
          try{
            CarRentalCompany company =  RentalStore.getRental(name);
           quotesList.add( company.createQuote(constraints, guest));
             }
          catch(ReservationException ex){
           
          }
            
      }              
         
    }
    
    public List<Quote> getCurrentQuotes(){
        return quotesList;
    }
        
    public void confirmQuotes() throws ReservationException
    {
        for(Quote quote :getCurrentQuotes()){
         CarRentalCompany company =   RentalStore.getRental(quote.getRentalCompany());
            try{
                reservationList.add(company.confirmQuote(quote));
            }
            catch(ReservationException ex){
                for (Reservation res:reservationList){
                     RentalStore.getRental(res.getRentalCompany()).cancelReservation(res);
                throw ex;
            }
        } 
    }        
          
               
        }

    
}
