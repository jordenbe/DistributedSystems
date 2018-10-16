package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.Quote;
import rental.ReservationConstraints;
import rental.ReservationException;

@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();
    void createQuote(ReservationConstraints constraints, String guest);
    List<Quote> getCurrentQuotes();
    void confirmQuotes() throws ReservationException;
    
}
