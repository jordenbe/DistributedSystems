package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.*;
import rental.session.IManagerSessionRemote;
import rental.session.IReservationSessionRemote;
import rental.session.SessionControlRemote;

public class Client extends AbstractTestManagement<IReservationSessionRemote,IManagerSessionRemote> {
	//private IRemoteCarRentalCompany stub;
	private SessionControlRemote sessionControl;

    /********
	 * MAIN *
	 ********/


	public static void main(String[] args) throws Exception {
		Client client = new Client("trips");
		client.run();
	}

	
	/***************
	 * CONSTRUCTOR *
	 ***************/
	
	public Client(String scriptFile) {
		super(scriptFile);

		try {
			Registry registry = LocateRegistry.getRegistry(null);
			 sessionControl = (SessionControlRemote) registry.lookup("SessionControl");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}


	@Override
	protected Set<String> getBestClients(IManagerSessionRemote ms) throws Exception {

	 	return ms.getBestCustomer();
	}

	@Override
	protected String getCheapestCarType(IReservationSessionRemote reservationSessionRemote, Date start, Date end, String region) throws Exception {
	return	reservationSessionRemote.getCheapestCarType(start, end);
	}

	@Override
	protected CarType getMostPopularCarTypeIn(IManagerSessionRemote ms, String carRentalCompanyName, int year) throws Exception {
	return 	ms.getMostPopularCarType(carRentalCompanyName, year);
	}

	@Override
	protected IReservationSessionRemote getNewReservationSession(String name) throws Exception {
		return sessionControl.createNewReservationSession(name);
	}

	@Override
	protected IManagerSessionRemote getNewManagerSession(String name, String carRentalName) throws Exception {
		return sessionControl.createNewManagerSession(carRentalName);
	}

	@Override
	protected void checkForAvailableCarTypes(IReservationSessionRemote reservationSessionRemote, Date start, Date end) throws Exception {
		reservationSessionRemote.getAvailableCarTypes(start, end);
	}

	@Override
	protected void addQuoteToSession(IReservationSessionRemote reservationSessionRemote, String name, Date start, Date end, String carType, String region) throws Exception {
		reservationSessionRemote.createQuote(new ReservationConstraints(start, end, carType, region), "client");
	}

	@Override
	protected List<Reservation> confirmQuotes(IReservationSessionRemote reservationSessionRemote, String name) throws Exception {
	return reservationSessionRemote.confirmQuotes(name);
	}

	@Override
	protected int getNumberOfReservationsBy(IManagerSessionRemote ms, String clientName) throws Exception {
		return ms.getNumberOfReservationsBy(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(IManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
		return ms.getNumberOfReservationsByCarType(carRentalName,carType);
	}
}