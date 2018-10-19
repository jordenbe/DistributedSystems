
package session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ejb.Stateless;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;


@Stateless
public class ManagerSession implements ManagerSessionRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    public Collection<CarType> getCarTypes(String carRentalCompany){
        
        return RentalStore.getRental(carRentalCompany).getCarTypes();
    }
    
 
    
    public String getBestCustomer(){
        Set<String> companies = RentalStore.getRentals().keySet();
        Map<String,Integer> klanten = new TreeMap<String,Integer>();
        for (String c:companies){
            CarRentalCompany carRentalCompany = RentalStore.getRental(c);
            for(Car car: carRentalCompany.getCars()){
                for(Reservation r : car.getAllReservations())
                {
                    Integer cur = klanten.get(r.getCarRenter());
                    klanten.put(r.getCarRenter(),cur==null?1:cur++);
                }
            }
        }
        Map.Entry<String,Integer> max = null;
        for(Map.Entry<String,Integer> e : klanten.entrySet())
            if(max == null || e.getValue() > max.getValue())
                max = e;
        return max.getKey();
    }

    @Override
    public int getNumberOfReservationsBy(String client) {
        int c = 0;
        for(CarRentalCompany cr : getCompanies())
        {
            c += cr.getNumberOfReservationsBy(client);
        }
        return c;
    }
   


    
    private List<CarRentalCompany> getCompanies()
    {
        Set<String> companies = RentalStore.getRentals().keySet();
        List<CarRentalCompany> crcs = new ArrayList<CarRentalCompany>();
        for(String str : companies)
            crcs.add(RentalStore.getRental(str));
        return crcs;
    }

    @Override
    public int getNumberOfReservation(String carRentalCompany, String carType) {
        return RentalStore.getRentals().get(carRentalCompany).getNumberOfReservationsForCarType(carType);
    }

   
}
