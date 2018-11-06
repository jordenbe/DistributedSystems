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
        try {
             return new HashSet<CarType>(em.createNamedQuery("getCarTypes").setParameter("company", company).getResultList());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Set<Integer> getCarIds(String company, String type) {
        Set<Integer> out = new HashSet<Integer>();
        try {
            for(Car c: RentalStore.getRental(company).getCars(type)){
                out.add(c.getId());
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ManagerSession.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return out;
    }

    @Override
    public int getNumberOfReservations(String company, String type, int id) {
        return em.createNamedQuery("getNumberOfReservationsByCarId", Number.class)
                    .setParameter("company", company)
                    .setParameter("id", id)
                    .getSingleResult()
                    .intValue();

    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        return em.createNamedQuery("getNumberOfReservationsByCarId", Number.class)
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
    public void addCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed) {
        em.persist(new CarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed));
    }
    
    @Override
    public void addCarType(CarType cartype) {
        if(cartype != null){
            em.persist(cartype);
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
        List<Object[]> bestClients = em.createNamedQuery("ManagerSession.getBestClients").getResultList();
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