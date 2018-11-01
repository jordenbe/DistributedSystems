package rental;


import client.IReservationSession;
import rental.session.IReservationSessionRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RentalAgency implements RemoteRentalAgency {
    List<IReservationSessionRemote> reservationSessions = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(null);
        try {
            RemoteRentalAgency remoteRentalAgency = (RemoteRentalAgency) UnicastRemoteObject.exportObject(new RentalAgency(), 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RentalAgency", remoteRentalAgency);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public IReservationSessionRemote getReservationSession(String name) throws RemoteException {
        IReservationSessionRemote iReservationSessionRemote = null;
        if (reservationSessions != null) {
            for (IReservationSessionRemote s : reservationSessions) {
                if (s.getName().equals(name)) {
                    iReservationSessionRemote = s;
                }
            }
        }


        if (iReservationSessionRemote == null) {
            iReservationSessionRemote = new IReservationSession(name);
            reservationSessions.add( iReservationSessionRemote);
        }

        return iReservationSessionRemote;
    }


}
