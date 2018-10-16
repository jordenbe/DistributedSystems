/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;

/**
 *
 * @author jorden
 */
@Stateless
public class ManagerSession implements ManagerSessionRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    public Collection<CarType> getCarTypes(CarRentalCompany carRentalCompany){
        return carRentalCompany.getCarTypes();
    }
    
    public int getNumberOfReservation(CarRentalCompany carRentalCompany,String carType){
       
      List<Car> cars =  carRentalCompany.getCars();
      int resAmount=0;
       for (Car car:cars){
           if(car.getType().equals(carType)){
             List<Reservation> reservations =  car.getAllReservations();
            resAmount += reservations.size();       
           }
       }
       return resAmount;
    }
    
    public Object getBestCustomer(){
        
    }
}
