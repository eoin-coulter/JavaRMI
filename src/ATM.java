import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATM {
    public static void main(String args[]) {
// get user’s input, and perform the operations

        try { //bind
            BankInterface bank = (BankInterface) Naming.lookup("//localhost/Bank");
            String action = args[0];


            if (action.equals("login")) {
                try {

                    login(args[1], args[2], bank);
                } catch (InvalidLogin e) {

                }
            }
            if (action.matches("-?(0|[1-9]\\d*)")) {
                try {
                    if (args[1].equals("balance")) {
                        long sessionId = Long.parseLong(args[0]);
                        int accountNumber = Integer.parseInt(args[2]);
                        BigDecimal balance = bank.getBalance(accountNumber, sessionId);
                        System.out.print("Balance for " + accountNumber + ":  €" + balance);
                    }
                } catch (Exception e) {
                }
                if (args[1].equals("deposit")) {
                    try {
                        long sessionId = Long.parseLong(args[0]);
                        int accountNumber = Integer.parseInt(args[2]);
                        BigDecimal deposit = new BigDecimal(args[3]);
                        bank.deposit(accountNumber, deposit, sessionId);
                        System.out.println("Successfully deposited : €" + deposit);
                    } catch (InvalidSession e) {

                    }


                }
                if (args[1].equals("withdraw")) {
                    try {
                        long sessionId = Long.parseLong(args[0]);
                        int accountNumber = Integer.parseInt(args[2]);
                        BigDecimal withdraw = new BigDecimal(args[3]);

                        BigDecimal bal = bank.getBalance(accountNumber, sessionId);
                        bal = bal.subtract(withdraw);
                        if(bal.signum() == -1){
                            throw new IllegalArgumentException("Insufficient Funds");
                        }

                        bank.withdraw(accountNumber, withdraw, sessionId);
                        System.out.println("Successfully Withdrew : €" + withdraw);
                    } catch (InvalidSession e) {
                    }


                }
                if (args[1].equals("statement")) {
                    try {
                        System.out.println("Trying to load statement");
                        long sessionId = Long.parseLong(args[0]);

                        Date from = new SimpleDateFormat("dd/MM/yyyy").parse(args[2]);
                        Date to = new SimpleDateFormat("dd/MM/yyyy").parse(args[3]);

                        boolean dateCheck = isDateValid(from,to);
                        if (!dateCheck){
                            throw new IllegalArgumentException("Invalid Date Entered");
                        }

                        Statement statement = bank.getStatement(from, to, sessionId);
                        System.out.print("Got Statement");
                        printStatement(statement);


                    }catch (IllegalStateException e){
                        System.out.print("No Transactions between given dates");

                    }catch (InvalidSession e) {
                        System.out.print("Failed");
                    }
                }

            }

        } catch (Exception e) {
        }
    }

    public static void printStatement(Statement statement) {
        System.out.print("Statement for " + statement.getAccountName() + ":\n");
        int i = 0;
        for (Transaction transaction : statement.transactions) {
            i++;
            System.out.print("Transaction number " + i + "\n");
            System.out.print("Transaction Date : " + transaction.getTransactionDate() + "\n");
            System.out.print("Transaction Type : " + transaction.getTransactionType() + "\n");
            System.out.print("Transaction Amount : " + transaction.getTransactionAmount());
            System.out.print("----------------------------------------------- \n");


        }
    }

    public static void login(String user, String password, BankInterface bank) throws RemoteException, InvalidLogin, MalformedURLException, NotBoundException {

        long answer = bank.login(user, password);

        System.out.print("Successful login for " + user + " session ID " + answer + "for 5 minutes ");


    }

    public static boolean isDateValid(Date from, Date to) {
        try {
            SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            fullFormat.setLenient(false);

            Date start = fullFormat.parse(String.valueOf(from));
            Date end = fullFormat.parse(String.valueOf(to));

            int yearStart = Integer.parseInt(yearFormat.format(start));
            int yearEnd = Integer.parseInt(yearFormat.format(end));

            if (yearStart >= 1900 && yearEnd >= 1900 && yearStart <= yearEnd)
                    return true;
            else
                return false;
        } catch (ParseException e) {
            return false;
        }
    }


}

