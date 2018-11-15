package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;

@Stateless
public class ManagerSession implements ManagerSessionRemote {
    
    @PersistenceContext
    EntityManager em; 
    
    @Override
    public Set<CarType> getCarTypes(String company) {
        return new HashSet<CarType>(em.createQuery(
                "SELECT ct from CarRentalCompany crc JOIN crc.carTypes ct WHERE crc.name = :company"
        ).setParameter("company", company)
         .getResultList());
    }

    @Override
    public Set<Integer> getCarIds(String company, String type) {
        List<Integer> ids = em.createQuery("SELECT C.id FROM CarRentalCompany as CRC JOIN CRC.cars C JOIN CRC.carTypes CT"
                + " WHERE CRC.name = :company AND CT.name = :type")
                   .setParameter("company", company)
                    .setParameter("type", type)
                    .getResultList();
        return new HashSet<>(ids);
    }

    @Override
    public int getNumberOfReservations(String company, int id) {
                return em.createQuery("SELECT count(R) FROM Reservation R WHERE R.rentalCompany = :company AND R.carId = :id", Number.class)
                .setParameter("company", company)
                .setParameter("id", id)
                .getSingleResult()
                .intValue();

    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        return em.createQuery("SELECT count(R) FROM Reservation R WHERE R.rentalCompany = :company AND R.carType = :type", Number.class)
                    .setParameter("company", company)
                    .setParameter("type", type)
                    .getSingleResult()
                    .intValue();
    }
    
    @Override
    public void addCar(Car car)
    {
        if(car != null)
            em.persist(car);
        else throw new IllegalArgumentException();
    }

    @Override
    public String addCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed) {
        return addCarType(new CarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed));
    }
    
    @Override
    public String addCarType(CarType carType) {
        if(carType != null){
            if(em.find(CarType.class, carType.getName()) == null)
            {
                 em.persist(carType);
            }
            return carType.getName();
        }
        else throw new IllegalArgumentException();
    }

    @Override
    public void addCarRentalCompany(String name, List<String> regions, List<Car> cars) {
        em.persist(new CarRentalCompany(name, regions, cars));
    }
    
    @Override
    public Set<String> getBestClients()
    {
        String query = "SELECT R.carRenter, COUNT(R) as amount FROM Reservation R GROUP BY R.carRenter ORDER BY amount DESC";
        List<Object[]> bestClients = em.createQuery(query).getResultList();
        Set<String> results = new HashSet<String>();
        Long max = (Long) bestClients.get(0)[1];

        for (Object[] array : bestClients) {
            Long count = (Long) array[1];
            if (count.equals(max)) {
                results.add((String) array[0]);
            }
        }

        return results;
    }

    @Override
    public int getNumberOfReservations(String clietnName) {
        String query = "SELECT COUNT(*) FROM Reservation R WHERE r.carRenter = :name GROUP BY r.carRenter";
        return em.createQuery(query).setParameter("name", clietnName).getFirstResult();
    }

    @Override
    public CarType getMostPopularCarType(String company, int year) {
        String type = (String)em.createQuery("SELECT R.carType FROM Reservation R WHERE R.rentalCompany = :company AND R.startDate > :curYear AND R.startDate < :nextYear"
                + "GROUP BY R.carType ORDER BY COUNT(R.id) DESC")
                .setParameter("company", company)
                .setParameter("curYear", new GregorianCalendar(year,0,0).getTime())
                .setParameter("nextYear", new GregorianCalendar(year+1,0,0).getTime()).getResultList().get(0);
        return em.find(CarType.class, type);
        
    }

    @Override
    public void addCar(String company, long carId) {
        CarRentalCompany crc = em.find(CarRentalCompany.class, company);
        Car car = em.createQuery("SELECT C FROM Car C WHERE C.id = :id",Car.class).setParameter("id",carId).getSingleResult();
        crc.addCar(car);
        em.persist(crc);
    }

    @Override
    public long createCar(String carTypeId) {
        Car car = new Car(em.find(CarType.class, carTypeId));
        em.persist(car);
        return (long)car.getId();
    }

    @Override
    public void addCarType(String company, String id) {
        if(em.find(CarType.class, id) == null)
        {
            CarRentalCompany crc = em.find(CarRentalCompany.class, company);
            crc.addCarType(em.find(CarType.class,id));
            em.persist(crc);
        }
      
    }

    @Override
    public void addCarRentalCompany(String name) {
        em.persist(new CarRentalCompany(name));
    }

    @Override
    public void addRegions(String company, List<String> regions) {
        CarRentalCompany crc = em.find(CarRentalCompany.class, company);
        crc.addRegions(regions);
        em.persist(crc);
    }

    

    
    
    

}