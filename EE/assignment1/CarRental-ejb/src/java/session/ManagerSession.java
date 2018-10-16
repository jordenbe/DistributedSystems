
package session;

import java.util.Collection;
import java.util.List;
import java.util.Set;
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
    
    public int getNumberOfReservation(String carRentalCompany, String carType){
      List<Car> cars = RentalStore.getRental(carRentalCompany).getCars();
      int resAmount=0;
       for (Car car:cars){
           if(car.getType().getName().equals(carType)){
             List<Reservation> reservations =  car.getAllReservations();
            resAmount += reservations.size();       
           }
       }
       return resAmount;
    }
    
    public Object getBestCustomer(){
       Set<String> companies = RentalStore.getRentals().keySet();
       for (String c:companies){
           CarRentalCompany carRentalCompany = RentalStore.getRental(c);
           carRentalCompany.
       }
       
    }

   
}
