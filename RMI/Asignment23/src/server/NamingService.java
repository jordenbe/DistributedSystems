package server;



import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class NamingService {
    private static ArrayList<IRemoteCarRentalCompany> carRentalCompanies = new ArrayList<>();

    public static ArrayList<IRemoteCarRentalCompany> getCarRentalCompanies() {
        return new ArrayList<>(carRentalCompanies);
    }

    public static void registerCarRentalCompany(IRemoteCarRentalCompany company) throws RemoteException {
        if(company != null)
        {
            try {
                Registry registry = LocateRegistry.getRegistry(null);
                IRemoteCarRentalCompany carRentalCompany = (IRemoteCarRentalCompany) registry.lookup(company.getName());
                carRentalCompanies.add(carRentalCompany);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void unregisterCarRentalCompany(IRemoteCarRentalCompany company) throws RemoteException {
        if(company != null)
        {
            carRentalCompanies.remove(company);
        }
    }

    public static IRemoteCarRentalCompany getCarRentalCompany(String company) throws RemoteException {
        IRemoteCarRentalCompany foundCompany = null;
        for (IRemoteCarRentalCompany carRentalCompany : carRentalCompanies) {
            if (carRentalCompany.getName().equals(company)) {
                foundCompany = carRentalCompany;
            }
        }
        if (foundCompany == null) {
            throw new RemoteException("Company not found");
        }
        return  foundCompany;
    }


}
