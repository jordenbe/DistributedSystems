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
    
    public int getNumberOfReservations(String company, int carId);
    
    public int getNumberOfReservations(String company, String type);
    
    public int getNumberOfReservations(String clietnName);
    
    void addCar(Car car);
    
    void addCar(String company, long id);
    
    long createCar(String carTypeId);
    
    String addCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed);
    
    String addCarType(CarType cartype);
    
    void addCarType(String company, String id);
    
    void addCarRentalCompany(String name, List<String> regions, List<Car> cars);
    
    void addCarRentalCompany(String name);
    
    CarType getMostPopularCarType(String company, int year);
    
   
    
    void addRegions(String company, List<String> regions);
    
    /*************/
    /***LOOKUPS***/
    /*************/
    
    Set<String> getBestClients();
   
      
}