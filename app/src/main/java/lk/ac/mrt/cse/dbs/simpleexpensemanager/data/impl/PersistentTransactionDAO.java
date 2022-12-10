package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentTransactionDAO implements TransactionDAO {
    private DbHandler db;

    public PersistentTransactionDAO(DbHandler db){
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        if(expenseType == ExpenseType.EXPENSE){
            PersistentAccountDAO pa = new PersistentAccountDAO(this.db);
            try {
                Account user = pa.getAccount(accountNo);
                if(user.getBalance() < amount){
                    return;
                }
            }catch (Exception e){
                System.out.println("Invalid Account");
            }
        }
        String sDate = date.toString();
        this.db.addNewTransaction(sDate,accountNo, amount, getStringExpense(expenseType));
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return this.db.readTransactions(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return this.db.readTransactions(limit);
    }



    public String getStringExpense(ExpenseType expense){
        if(expense == ExpenseType.EXPENSE){
            return "Expense";
        }else{
            return "Income";
        }
    }



    public void removeTransaction(String transactionNo) throws InvalidAccountException {
        int result = this.db.deleteData("transaction","transaction_no",transactionNo);
        if(result == 0){
            throw new InvalidAccountException("Transaction  is invalid");
        }
    }

}

