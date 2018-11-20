package ds.gae.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ds.gae.CarRentalModel;
import ds.gae.EMF;
import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;

public class CarRentalServletContextListener implements ServletContextListener {
	
	private EntityManagerFactory emf = EMF.get();

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// This will be invoked as part of a warming request, 
		// or the first user request if no warming request was invoked.
						
		// check if dummy data is available, and add if necessary
		if(!isDummyDataAvailable()) {
			addDummyData();
		}
	}
	
	private boolean isDummyDataAvailable() {
		// If the Hertz car rental company is in the datastore, we assume the dummy data is available
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery("SELECT c.name FROM CarRentalCompany c WHERE c.name = :name").setParameter("name", "Hertz")
					.getSingleResult() != null;	
		}
		catch(NoResultException nre)
		{
			return false;
		}
		finally {em.close();}

	}
	
	private void addDummyData() {
		loadRental("Hertz","hertz.csv");
        loadRental("Dockx","dockx.csv");
	}
	
	private void loadRental(String name, String datafile) {
		Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.INFO, "loading {0} from file {1}", new Object[]{name, datafile});
		EntityManager em = emf.createEntityManager();
        try {
        	
            List<Car> cars = loadData(name, datafile);
            CarRentalCompany company = new CarRentalCompany(name, cars);
            em.persist(company);
            //CarRentalModel.get().CRCS.put(name, company);

        } catch (NumberFormatException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
        	em.close();
        }
	}
	
	public static List<Car> loadData(String name, String datafile) throws NumberFormatException, IOException {
		// FIXME: adapt the implementation of this method to your entity structure
		
		List<Car> cars = new ArrayList<Car>();
		int carId = 1;

		//open file from jar
		
		BufferedReader in = new BufferedReader(new InputStreamReader(CarRentalServletContextListener.class.getClassLoader().getResourceAsStream(datafile)));
		//while next line exists
		//EntityManager em = EMF.get().createEntityManager();
		//try
		//{
			while (in.ready()) {
				//read line
				String line = in.readLine();
				//if comment: skip
				if (line.startsWith("#")) {
					continue;
				}
				//tokenize on ,
				StringTokenizer csvReader = new StringTokenizer(line, ",");
				//create new car type from first 5 fields
				CarType type = new CarType(csvReader.nextToken(),
						Integer.parseInt(csvReader.nextToken()),
						Float.parseFloat(csvReader.nextToken()),
						Double.parseDouble(csvReader.nextToken()),
						Boolean.parseBoolean(csvReader.nextToken()));
				//create N new cars with given type, where N is the 5th field
				//em.persist(type);
				for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
					cars.add(new Car(carId++, type));
				}
			}
			return cars;
		/*
		}
		finally
		{
			em.close();
		}*/
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Please leave this method empty.
	}
}