package session;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.Car;
import rental.CarType;
import rental.Reservation;

@Remote
public interface ManagerSessionRemote {
    
    public Set<CarType> getCarTypes(String company);
    
    public Set<Integer> getCarIds(String company,String type);
    
    public int getNumberOfReservations(String company, String type, int carId);
    
    public int getNumberOfReservations(String company, String type);
    
    void addCar(int uid, CarType type);
    
    void addCar(Car car);
    
    void addCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed);
    
    void addCarType(CarType cartype);
    
    void addCarRentalCompany(String name, List<String> regions, List<Car> cars);
    
    /*************/
    /***LOOKUPS***/
    /*************/
    
    Set<String> getBestClients();
   
      
}