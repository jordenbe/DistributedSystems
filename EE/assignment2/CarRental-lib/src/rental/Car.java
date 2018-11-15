package rental;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import rental.CarType;
import rental.Reservation;

@Entity
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private CarType type;
    
    @OneToMany(cascade =  CascadeType.PERSIST)
    private Set<Reservation> reservations;

    /***************
     * CONSTRUCTOR *
     ***************/
    
    public Car()
    {
        this.reservations = new HashSet<>();
    }
    
    public Car(CarType type)
    {
        this();
        setType(type);
    }

    /******
     * ID *
     ******/
    
    public int getId() {
    	return id;
    }
    
    /************
     * CAR TYPE *
     ************/
    
    public CarType getType() {
        return type;
    }
	
	public void setType(CarType type) {
		this.type = type;
	}
    /****************
     * RESERVATIONS *
     ****************/

    public boolean isAvailable(Date start, Date end) {
        if(!start.before(end))
            throw new IllegalArgumentException("Illegal given period");

        for(Reservation reservation : reservations) {
            if(reservation.getEndDate().before(start) || reservation.getStartDate().after(end))
                continue;
            return false;
        }
        return true;
    }
    
    public void addReservation(Reservation res) {
        reservations.add(res);
    }
    
    public void removeReservation(Reservation reservation) {
        // equals-method for Reservation is required!
        reservations.remove(reservation);
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }
}