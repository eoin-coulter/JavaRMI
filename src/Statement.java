import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Statement implements StatementInterface, Serializable {
    public int accountNum;
    public Date startDate;
    public Date endDate;
    public String accountName;
    public List<Transaction> transactions;


    public Statement(int accountNum, Date startDate, Date endDate, String accountName, List<Transaction> transactions) {
        this.accountNum = accountNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountName = accountName;
        this.transactions = transactions;
    }

    public int getAccountnum() {
        return accountNum;
    }


    public Date getStartDate() {
        return startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public String getAccountName() {
        return accountName;
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }


}
