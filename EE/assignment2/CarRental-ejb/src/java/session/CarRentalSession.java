package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {
    
    @PersistenceContext
    EntityManager em;

    private String renter;
    private List<Quote> quotes = new LinkedList<Quote>();

    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(em.createQuery("SELECT Com.name FROM RentalCompany Com", String.class).getResultList());
    }
    
    @Override
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        String query = "SELECT DISTINCT C.type FROM Car C WHERE C.id NOT IN (SELECT R.carId FROM Reservation R WHERE R.startDate <= :end AND R.endDate >= :start)";
        List<CarType> availableCarTypes = em.createQuery(query, CarType.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        return availableCarTypes;
    }

    @Override
    public Quote createQuote( String client, Date start, Date end, String carType, String region) throws ReservationException {
        List<CarRentalCompany> companies = em.createQuery("SELECT CRC FROM CarRentalCompany CRC JOIN CRC.carTypes CT WHERE CT.name = :carType AND :region MEMBER OF CRC.regions", CarRentalCompany.class)
                .setParameter("region", region)
                .setParameter("carType", carType)
                .getResultList();
        
        Quote q = null;
        ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
        if(companies.isEmpty()) throw new ReservationException("The are no companies in the given region and/or the given car type that operate");
        for(CarRentalCompany crc : companies)
        {
            try{
                q = crc.createQuote(constraints, client);
                quotes.add(q);
                return q;
            }
            catch(Exception e){}
        }
        if(q == null) throw new ReservationException("No cars available with the given constraint");
        return null;
    }

    @Override
    public List<Quote> getCurrentQuotes() {
        return quotes;
    }

    @Override
    public List<Reservation> confirmQuotes() throws ReservationException {
       List<Reservation> reservations = new ArrayList<Reservation>();
       
       try{
           for(Quote q : quotes){
               CarRentalCompany crc = em.find(CarRentalCompany.class, q.getRentalCompany());
               if(crc != null)
               {
                   Reservation r = crc.confirmQuote(q);
                   em.persist(r);
                   reservations.add(r);
               }
           }
       }
       catch(Exception e){
           throw new ReservationException(e);
       }
       quotes.clear();
       return reservations;
    }

    @Override
    public void setRenterName(String name) {
        if (renter != null) {
            throw new IllegalStateException("name already set");
        }
        renter = name;
    }

    @Override
    public String getCheapestCarType(Date start, Date end, String region) {
        List l = em.createQuery("SELECT CT.name FROM CarRentalCompany CRC JOIN CRC.carTypes CT JOIN CRC.cars C"
                + " WHERE :region MEMBER OF CRC.regions AND C.type = CT AND C.id NOT IN ("
                + " SELECT R.carId FROM Reservation R WHERE R.startDate <= :end AND R.endDate >= :start)"
                + " ORDER BY CT.rentalPricePerDay")
                .setParameter("region", region)
                .setParameter("start",start)
                .setParameter("end", end).getResultList();
        if(l.isEmpty()) return null;
        return (String)l.get(0);
    }
}