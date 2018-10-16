/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.Collection;
import javax.ejb.Remote;
import rental.*;


@Remote
public interface ManagerSessionRemote {
    Collection<CarType> getCarTypes(CarRentalCompany carRentalCompany);
    void createQuote(ReservationConstraints constraints, String guest);
    int getNumberOfReservation(CarRentalCompany carRentalCompany, String carType);
    Object getBestCustomer();
    
}
