package client;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.print.PaperSource;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import rental.CarType;
import rental.Reservation;
import rental.ReservationConstraints;
import session.CarRentalSessionRemote;
import session.ManagerSessionRemote;

public class Main extends AbstractTestAgency<Object, Object>{
    
    @EJB
    static CarRentalSessionRemote session;
    
    @EJB
    static ManagerSessionRemote mSession;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("found rental companies: "+session.getAllRentalCompanies());
            Main m = new Main("simpleTrips");
            m.run();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    public Main(String scriptFile) {
        super(scriptFile);
       
       
    }

    @Override
    protected Object getNewReservationSession(String name) throws Exception {
        
       //return session;
        InitialContext context = new InitialContext();
        return  (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
      
       
    }

    @Override
    protected Object getNewManagerSession(String name, String carRentalName) throws Exception {
       // return mSession;
         InitialContext context = new InitialContext();
        return (ManagerSessionRemote) context.lookup(ManagerSessionRemote.class.getName()); 
    }

    @Override
    protected void checkForAvailableCarTypes(Object session, Date start, Date end) throws Exception {
        Set<String> cts = ((CarRentalSessionRemote)session).checkAvailableCarTypes(start, end);
        for(String ct : cts )
        {
            System.out.println(ct);
        }
    }

    @Override
    protected void addQuoteToSession(Object session, String name, Date start, Date end, String carType, String region) throws Exception {
        ReservationConstraints rc = new ReservationConstraints(start, end, carType, region);
       ((CarRentalSessionRemote)session).createQuote(rc, name);
     
     
     
     
    }

    @Override
    protected List<Reservation> confirmQuotes(Object session, String name) throws Exception {
        ((CarRentalSessionRemote)session).confirmQuotes();
        return ((CarRentalSessionRemote)session).getCurrentReservations();
    }

    @Override
    protected int getNumberOfReservationsBy(Object ms, String clientName) throws Exception {
        return ((ManagerSessionRemote)session).getNumberOfReservationsBy(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(Object ms, String carRentalName, String carType) throws Exception {
       return  ((ManagerSessionRemote)ms).getNumberOfReservation(carRentalName, carType);
      
    }
}
