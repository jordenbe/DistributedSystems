package rental.session;


import client.IReservationSession;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SessionControl implements SessionControlRemote {
    List<IReservationSessionRemote> reservationSessions = new ArrayList<>();
    List<ManagerSessionRemote> managerSessions = new ArrayList<>();

    public SessionControl() {
    }



    public IReservationSessionRemote createNewReservationSession(String name) throws RemoteException {
        IReservationSession reservationSession = new IReservationSession(name);
        reservationSessions.add(reservationSession);
        return reservationSession;
    }

    public ManagerSessionRemote createNewManagerSession(String name) throws RemoteException {
        ManagerSessionRemote managerSession = new ManagerSession(name);
        managerSessions.add(managerSession);
        return managerSession;
    }



   /* public IReservationSessionRemote getReservationSession(String name) throws RemoteException {
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
    }*/



}
