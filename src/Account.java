import java.math.BigDecimal;
import java.util.ArrayList;

public class Account {
    private String name;
    private String password;
    private long currentSession;
    private BigDecimal balance;
    private int accountNum;
    private ArrayList<Transaction> transactions;


    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(long currentSession) {
        this.currentSession = currentSession;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Account(String name, String password, long currentSession, BigDecimal balance, int accountNum, ArrayList<Transaction> transactions) {
        this.name = name;
        this.password = password;
        this.currentSession = currentSession;
        this.balance = balance;
        this.accountNum = accountNum;
        this.transactions = transactions;
    }
}
