package rental.session;

import java.rmi.RemoteException;

public abstract class Session
{
    private String id;

    public Session()
    {

    }

    public void setId(String id) throws RemoteException
    {
        if(id == null || id.isEmpty())
            throw new RemoteException("Your compnay name cannot be null nor empty");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
