package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.*;
import rental.session.ManagerSessionRemote;
import rental.session.IReservationSessionRemote;
import server.IRemoteCarRentalCompany;

public class Client extends AbstractTestManagement<IReservationSessionRemote,ManagerSessionRemote> {
	private IRemoteCarRentalCompany stub;

    /********
	 * MAIN *
	 ********/


	public static void main(String[] args) throws Exception {
		
		String carRentalCompanyName = "Hertz";
		
		// An example reservation scenario on car rental company 'Hertz' would be...
		Client client = new Client("simpleTrips", carRentalCompanyName);
		client.run();

	}

	
	/***************
	 * CONSTRUCTOR *
	 ***************/
	
	public Client(String scriptFile, String carRentalCompanyName) {
		super(scriptFile);

		try {
			Registry registry = LocateRegistry.getRegistry(null);
			 stub = (IRemoteCarRentalCompany) registry.lookup(carRentalCompanyName);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}


	@Override
	protected Set<String> getBestClients(ManagerSessionRemote ms) throws Exception {
	 	return ms.getBestCustomer();
	}

	@Override
	protected String getCheapestCarType(IReservationSessionRemote reservationSessionRemote, Date start, Date end, String region) throws Exception {
	return	reservationSessionRemote.getCheapestCarType(start, end);
	}

	@Override
	protected CarType getMostPopularCarTypeIn(ManagerSessionRemote ms, String carRentalCompanyName, int year) throws Exception {
	return 	ms.getMostPopularCarType(carRentalCompanyName, year);
	}

	@Override
	protected IReservationSessionRemote getNewReservationSession(String name) throws Exception {
		return null;
	}

	@Override
	protected ManagerSessionRemote getNewManagerSession(String name, String carRentalName) throws Exception {
		return null;
	}

	@Override
	protected void checkForAvailableCarTypes(IReservationSessionRemote reservationSessionRemote, Date start, Date end) throws Exception {
		reservationSessionRemote.getAvailableCarTypes(start, end);
	}

	@Override
	protected void addQuoteToSession(IReservationSessionRemote reservationSessionRemote, String name, Date start, Date end, String carType, String region) throws Exception {

	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSessionRemote reservationSessionRemote, String name) throws Exception {
	return reservationSessionRemote.confirmQuotes(name);
	}

	@Override
	protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
		return ms.getNumberOfReservationsBy(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
		return ms.getNumberOfReservationsByCarType(carRentalName,carType);
	}
}