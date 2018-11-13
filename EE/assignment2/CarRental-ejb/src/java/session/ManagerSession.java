package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
    public int getNumberOfReservations(String company, String type, int id) {
                return em.createNamedQuery("SELECT count(R) FROM Reservation R WHERE R.rentalCompany = :company AND R.carId = :id", Number.class)
                .setParameter("company", company)
                .setParameter("id", id)
                .getSingleResult()
                .intValue();

    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        return em.createNamedQuery("SELECT count(R) FROM Reservation R WHERE R.rentalCompany = :company AND R.carType = :type", Number.class)
                    .setParameter("company", company)
                    .setParameter("type", type)
                    .getSingleResult()
                    .intValue();
    }

    @Override
    public void addCar(int uid, CarType type) 
    {
        em.persist(new Car(uid, type));
    }
    
    @Override
    public void addCar(Car car)
    {
        if(car != null)
            em.persist(car);
        else throw new IllegalArgumentException();
    }

    @Override
    public CarType addCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed) {
        return addCarType(new CarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed));
    }
    
    @Override
    public CarType addCarType(CarType cartype) {
        if(cartype != null){
            em.persist(cartype);
            return cartype;
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
        List<Object[]> bestClients = em.createNamedQuery(query).getResultList();
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
    

    
    
    

}