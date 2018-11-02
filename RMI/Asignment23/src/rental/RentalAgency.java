package rental;


import rental.session.SessionControlRemote;
import rental.session.SessionControl;
import server.IRemoteCarRentalCompany;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class RentalAgency {
	
	public static void main(String[] args) throws ReservationException,
			NumberFormatException, IOException {
		CrcData hertz  = loadData("hertz.csv");
		CrcData dockx = loadData("dockx.csv");



		System.setSecurityManager(null);
		try {
			RemoteCarRentalCompany hertzRemote = new RemoteCarRentalCompany(hertz.name, hertz.regions, hertz.cars);
			IRemoteCarRentalCompany hertzStub = (IRemoteCarRentalCompany) UnicastRemoteObject.exportObject(hertzRemote, 0);

			RemoteCarRentalCompany dockxRemote = new RemoteCarRentalCompany(dockx.name, dockx.regions, dockx.cars);
			IRemoteCarRentalCompany dockxStub = (IRemoteCarRentalCompany) UnicastRemoteObject.exportObject(dockxRemote, 0);

			SessionControl sessionManager = new SessionControl();
			SessionControlRemote remoteSessionManager = (SessionControlRemote) UnicastRemoteObject.exportObject(sessionManager, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Hertz", hertzStub);
			registry.bind("Dockx",dockxStub);
			registry.bind("SessionControl", remoteSessionManager);
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();

		}
	}

	public static CrcData loadData(String datafile)
			throws ReservationException, NumberFormatException, IOException {

		CrcData out = new CrcData();
		int nextuid = 0;

		// open file
		BufferedReader in = new BufferedReader(new FileReader(datafile));
		StringTokenizer csvReader;


		
		try {
			// while next line exists
			while (in.ready()) {
				String line = in.readLine();
				
				if (line.startsWith("#")) {
					// comment -> skip					
				} else if (line.startsWith("-")) {
					csvReader = new StringTokenizer(line.substring(1), ",");
					out.name = csvReader.nextToken();
					out.regions = Arrays.asList(csvReader.nextToken().split(":"));
				} else {
					// tokenize on ,
					csvReader = new StringTokenizer(line, ",");
					// create new car type from first 5 fields
					CarType type = new CarType(csvReader.nextToken(),
							Integer.parseInt(csvReader.nextToken()),
							Float.parseFloat(csvReader.nextToken()),
							Double.parseDouble(csvReader.nextToken()),
							Boolean.parseBoolean(csvReader.nextToken()));
					System.out.println(type);
					// create N new cars with given type, where N is the 5th field
					for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
						out.cars.add(new Car(nextuid++, type));
					}
				}
			}
		} finally {
			in.close();
		}

		return out;
	}
	
	static class CrcData {
		public List<Car> cars = new LinkedList<Car>();
		public String name;
		public List<String> regions =  new LinkedList<String>();
	}

}
