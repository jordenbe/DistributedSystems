package rental.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionControlRemote extends Remote {

    IReservationSessionRemote createNewReservationSession(String name) throws RemoteException;
    IManagerSessionRemote createNewManagerSession(String name) throws RemoteException;
}
