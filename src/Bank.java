import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;


public class Bank implements BankInterface {
    private static List<Account> accounts = new ArrayList<Account>();

    // users accounts


    public Bank() throws RemoteException {
        super();
    }

    

    public long login(String username, String password) throws RemoteException, InvalidLogin {
        long session = 0;

        System.out.print("Server login attempting");
        boolean login = false;
        for (Account account : accounts) {


            if (username.equals(account.getName()) && password.equals(account.getPassword())) {
                login = true;
                long leftLimit = 1L;
                long rightLimit = 1000000000000L;
                session = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
                account.setCurrentSession(session);


            }
        }
        if (login == true) {
            return session;
        } else {
            throw new InvalidLogin("Invalid Login Credentials Please try again");
        }
    }


    public void deposit(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession {
        boolean login = false;
        for (Account account : accounts) {
            if (accountnum == account.getAccountNum() && sessionID == account.getCurrentSession()) {
                login = true;
                System.out.print(account.getBalance());

                Transaction transaction = new Transaction();
                transaction.setTransactionType("Deposit");
                transaction.setTransactionAmount(amount);
                transaction.setTransactionDate(new Date());
                System.out.print(transaction.getTransactionDate().toString());
                ArrayList<Transaction> temp = account.getTransactions();
                temp.add(transaction);
                account.setTransactions(temp);


                account.setBalance(account.getBalance().add(amount));

                System.out.print("new Balance : " + account.getBalance());
            }

        }
        if (login == true) {
            return;
        } else throw new InvalidSession("Invalid Credentials");


    }


    public void withdraw(int accountnum, BigDecimal amount, long sessionID) throws RemoteException, InvalidSession {
        boolean login = false;
        for (Account account : accounts) {
            if (accountnum == account.getAccountNum() && sessionID == account.getCurrentSession()) {
                login = true;
                BigDecimal currentBalance = account.getBalance();
                if (currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) > 0) {
                    throw new InvalidSession("Insufficient funds");

                }

                Transaction transaction = new Transaction();
                transaction.setTransactionType("Withdraw");
                transaction.setTransactionAmount(amount);
                transaction.setTransactionDate(new Date());
                ArrayList<Transaction> temp = account.getTransactions();
                temp.add(transaction);
                account.setTransactions(temp);


                account.setBalance(account.getBalance().subtract(amount));
                System.out.print("new Balance : " + account.getBalance());
            }


        }
        if (login == true) {
            return;
        } else throw new InvalidSession("Details do not match. please try again");
    }


    public BigDecimal getBalance(int accountnum, long sessionID) throws RemoteException, InvalidSession {
        Account temp = null;
        for (Account account : accounts) {
            if (accountnum == account.getAccountNum() && sessionID == account.getCurrentSession()) {
                temp = account;
            }

        }
        if (temp != null) {
            return temp.getBalance();
        } else throw new InvalidSession("Invalid Session please login again");


    }


    public Statement getStatement(Date from, Date to, long sessionID) throws RemoteException, InvalidSession {


        for (Account account : accounts) {
            if (account.getCurrentSession() == sessionID) {

                Statement statement = new Statement(account.getAccountNum(), from, to, account.getName(), new ArrayList<Transaction>());
                ArrayList<Transaction> temp = account.getTransactions();
                for (Transaction transaction : temp) {

                    if (transaction.getTransactionDate().after(from) && transaction.getTransactionDate().before(to)) {
                        System.out.print(transaction.getTransactionType());
                        statement.transactions.add(transaction);
                    }
                }
                return statement;


            }

        }
        throw new InvalidSession("Invalid Session please login again");

    }

    public static void main(String args[]) throws Exception {
        try {
            accounts.add(new Account("eoin", "password", 0, new BigDecimal("100000000.34"), 123456789, new ArrayList<>()));
            accounts.add(new Account("conor", "password", 0, new BigDecimal("267572500"), 987654321, new ArrayList<>()));


            // First reset our Security manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
                System.out.println("Security manager set");
            }

            // Create an instance of the local object
            BankInterface bankInterface = new Bank();
            {

            }


            System.out.println("Instance of Bank Server created");
            BankInterface stub = (BankInterface) UnicastRemoteObject.exportObject(bankInterface, 0);


            // Put the server object into the Registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Bank", stub);
            System.out.println("Name rebind completed");
            System.out.println("Server ready for requests!");
        } catch (Exception exc) {
            System.out.println("Error in main - " + exc.toString());
        }
    }
}

