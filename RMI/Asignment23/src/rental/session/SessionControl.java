package rental.session;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SessionControl implements SessionControlRemote {
    List<IReservationSessionRemote> reservationSessions = new ArrayList<>();
    List<IManagerSessionRemote> managerSessions = new ArrayList<>();

    public SessionControl() {
    }



    public IReservationSessionRemote createNewReservationSession(String name) throws RemoteException {
        ReservationSessionRemote reservationSession = new ReservationSessionRemote(name);
        reservationSessions.add(reservationSession);
        return reservationSession;
    }

    public IManagerSessionRemote createNewManagerSession(String name) throws RemoteException {
        IManagerSessionRemote managerSession = new RemoteManagerSession(name);
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
            iReservationSessionRemote = new ReservationSessionRemote(name);
            reservationSessions.add( iReservationSessionRemote);
        }

        return iReservationSessionRemote;
    }*/



}
