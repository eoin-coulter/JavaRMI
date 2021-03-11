import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface BankInterface extends Remote {

    // The login method returns a token that is valid for some time period that must be passed to the other methods as a session identifier
    public long login(String username, String password) throws RemoteException, InvalidLogin, InvalidLogin;

    public void deposit(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession, InvalidSession;

    public void withdraw(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession;

    public BigDecimal getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession;

    public Statement getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSession;
}
