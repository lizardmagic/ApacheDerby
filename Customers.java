//- To start this java program make sure the database tables have been created.
//- SQL commands for creating the tables can be seen below.
/*
  CREATE TABLE Customers (CustomerId int, CustomerName varchar(30), CustomerAddress varchar(30), PRIMARY KEY (CustomerId));
  CREATE TABLE BankAccount (AccountNumber int, Balance int, PRIMARY KEY (AccountNumber));
  CREATE TABLE AccountHolding (AccountNumber int, CustomerId int, FOREIGN KEY (AccountNumber) References BankAccount (AccountNumber), FOREIGN KEY (CustomerId) References Customers (CustomerId));

  SELECT * FROM AccountHolding;
  SELECT * FROM Customers;
  SELECT * FROM BankAccount;

  DROP TABLE AccountHolding;
  DROP TABLE Customers;
  DROP TABLE BankAccount;
*/
//- To run this program, change directory to your derby's lib, move this java file to this location.
//cd /Users/lizardmagic/apache/db-derby-10.11.1.1-bin/lib
//- Compile the java file
//javac Customers.java
//- Open new bash terminal and start the derby server
//- Return to previous terminal and start the java program.
// java Customers
//- NOTE:
  //- Command to start derby server
  // java -jar /Users/lizardmagic/apache/db-derby-10.11.1.1-bin/lib/derbyrun.jar server start
  //- Command to set the Classpath
  //export CLASSPATH .:/Users/lizardmagic/apache/db-derby-10.11.1.1-bin/lib/derby.jar:/Users/lizardmagic/apache/db-derby-10.11.1.1-bin/lib/derbytools.jar:/Users/lizardmagic/apache/db-derby-10.11.1.1-bin/lib/derbyclient.jar

import java.sql.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customers {
  private static String dbName="BankDB;";
  private static String autoCreate = "create=true;";
      private static String driver = "org.apache.derby.jdbc.ClientDriver";
  private static String connectionURL = "jdbc:derby://localhost:1527/" + dbName;
  //- jdbc connection
  private static Connection conn = null;
  private static PreparedStatement insertCust = null;
  private static PreparedStatement insertBankAccount = null;
  private static PreparedStatement insertAccountHolding = null;

  private static PreparedStatement checkCustId = null;

  private static PreparedStatement getMaxCustId = null;
  private static PreparedStatement getMaxBnkAccNo = null;
  
  private static PreparedStatement displayAllCustomers = null;
  private static PreparedStatement displayAccountHoldings = null;
  private static PreparedStatement displayAllBankAccounts = null;

  private static ResultSet resultCheckCustId = null;
  private static ResultSet resultMaxCustId = null;
  private static ResultSet resultMaxBnkAccNo = null;
  private static ResultSet resultAllCustomers = null;
  private static ResultSet resultAllBankAccounts = null;
  private static ResultSet resultAllAccountHoldings = null;

  public static void main(String[] args) {
    try {
      createConnection();
      createPreparedStatements();
      clientMenu();
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static void createConnection() {
    try {
      Class.forName(driver);
      System.out.println("Driver has loaded successfully.");
      try {
        conn = DriverManager.getConnection(connectionURL);

        System.out.println("Connected to database " + dbName);
        conn.setAutoCommit(false);
        System.out.println("Set AutoCommit for Connection to False");
      } catch (Throwable e)  {   
        /*       Catch all exceptions and pass them to 
         **       the exception reporting method             */
        System.out.println(" . . . exception thrown:");
        errorPrint(e);
      }
    } catch(java.lang.ClassNotFoundException e)     {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
      System.out.println("\n *** Check your CLASSPATH variable is correct. ***\n");
    }
  }

  private static void createPreparedStatements()
  {
    //- Creating our prepared statements
     try {
      insertCust = conn.prepareStatement("INSERT INTO Customers (CUSTOMERID,CUSTOMERNAME,CUSTOMERADDRESS) VALUES (?,?,?)");
      insertBankAccount = conn.prepareStatement("INSERT INTO BankAccount (ACCOUNTNUMBER,BALANCE) VALUES(?,?)");
      insertAccountHolding = conn.prepareStatement("INSERT INTO AccountHolding (ACCOUNTNUMBER,CUSTOMERID) VALUES(?,?)");
      getMaxCustId = conn.prepareStatement("SELECT MAX(CUSTOMERID) FROM Customers");
      getMaxBnkAccNo = conn.prepareStatement("SELECT MAX(ACCOUNTNUMBER) FROM BankAccount");
      displayAllCustomers = conn.prepareStatement("SELECT * FROM Customers");
      displayAccountHoldings = conn.prepareStatement("SELECT * FROM AccountHolding");
      displayAllBankAccounts = conn.prepareStatement("SELECT * FROM BankAccount");
      checkCustId = conn.prepareStatement("SELECT CUSTOMERID FROM Customers WHERE CUSTOMERID=?");
    } catch (Throwable e)  {   
        /*       Catch all exceptions and pass them to 
         **       the exception reporting method             */
        System.out.println(" . . . exception thrown:");
        errorPrint(e);
      } 
  }

  private static void clientMenu() {
    while (true) {
      int option = clientStartMenu();
      if (option == 1) break;
      if (option == 2) addNewCustomer();
      if (option == 3) addNewBankAccountToExistingCustomer();
      if (option == 4) displayAllCustomer();
      if (option == 5) displayBankAccounts();
      if (option == 6) displayAccountHoldings();
    }
    shutdown();
  }

  private static void shutdown()
  {
    try
    {
      conn.setAutoCommit(true);
      if (insertCust != null)
      {
        insertCust.close();
      }
      if (insertBankAccount != null)
      {
        insertBankAccount.close();
      }
      if (insertAccountHolding != null)
      {
        insertAccountHolding.close();
      }
      if (getMaxCustId != null)
      {
        getMaxCustId.close();
      }
      if (getMaxBnkAccNo != null)
      {
        getMaxBnkAccNo.close();
      }
      if (displayAllCustomers != null)
      {
        displayAllCustomers.close();
      }
      if (displayAccountHoldings != null)
      {
        displayAccountHoldings.close();
      }
      if (displayAllBankAccounts != null)
      {
        displayAllBankAccounts.close();
      }
      if (checkCustId != null)
      {
        checkCustId.close();
      }
      if (resultCheckCustId != null)
      {
        resultCheckCustId.close();
      }
      if (resultMaxCustId != null)
      {
        resultMaxCustId.close();
      }
      if (resultMaxBnkAccNo != null)
      {
        resultMaxBnkAccNo.close();
      }
      if (resultAllCustomers != null)
      {
        resultAllCustomers.close();
      }
      if (resultAllBankAccounts != null)
      {
        resultAllBankAccounts.close();
      }
      if (resultAllAccountHoldings != null)
      {
        resultAllAccountHoldings.close();
      }
      if (conn != null)
      {
        //DriverManager.getConnection(connectionURL + ";shutdown=true");
        conn.close();
        System.out.println("Closed connection");
      }           
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    }
  }
 
  private static void insertNewCustomer(int customerNo, String custName, String custAddress)
  {
    try {
      insertCust.setInt(1,customerNo);
      insertCust.setString(2,custName);
      insertCust.setString(3,custAddress);
      //- Executing are inserts, will not be committed as autocomit is false
      insertCust.executeUpdate();
      //-  Now manually calling commit()    
      conn.commit();
      System.out.println("Inserted new Customer, customerId = " + customerNo + " custName = " + custName + "custAddress =  "+ custAddress);
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static void insertNewBankAccount(int accountNo, int balance)
  {
    try {
      insertBankAccount.setInt(1,accountNo);
      insertBankAccount.setInt(2,balance);
      //- Executing are inserts, will not be committed as autocomit is false
      insertBankAccount.executeUpdate();
      //- Now manually calling commit()   
      conn.commit();
      System.out.println("Inserted new BankAccount, accountNo = " + accountNo + " balance = " + balance);
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static void insertNewAccountHolding(int accountNo, int customerNo)
  {
    try {
      insertAccountHolding.setInt(1,accountNo);
      insertAccountHolding.setInt(2,customerNo);
      //- Executing are inserts, will not be committed as autocomit is false
      insertAccountHolding.executeUpdate();
      //- Now manually calling commit()  
      conn.commit();
      System.out.println("Inserted new AccountHolding, accountNo = " + accountNo + " customerNo = " + customerNo);
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }
  
  //  Method to Handle the SQL Exceptions
  static void errorPrint(Throwable e)
  {
    if (e instanceof SQLException) 
      SQLExceptionPrint((SQLException)e);
    else {
      System.out.println("A non SQL error occured.");
      e.printStackTrace();
    }   
  }  // END errorPrint 

  //  Iterates through a stack of SQLExceptions 
  static void SQLExceptionPrint(SQLException ex)
  {
    Logger lgr = Logger.getLogger(Customers.class.getName());

    if (((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState())))) {

      lgr.log(Level.INFO, "Derby shut down normally", ex);

    } else {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex1) {
          lgr.log(Level.WARNING, ex1.getMessage(), ex1);
        }
      }
      lgr.log(Level.SEVERE, ex.getMessage(), ex);
    }
  }  //  END SQLExceptionPrint 
  
private static void addNewCustomer() {
    int custId = getCustomerId();
    String custName = getCustomerName();
    String custAdd = getCustomerAddress();
    insertNewCustomer(custId,custName,custAdd);
  }
  
  private static int getNextBankAccountNo() {
    int nextBnkNo = -1;
    try {
      resultMaxBnkAccNo = getMaxBnkAccNo.executeQuery();
      if (resultMaxBnkAccNo.next()){
        nextBnkNo = resultMaxBnkAccNo.getInt(1);
      } 
      //System.out.println("nextBnkNo = " + nextBnkNo);
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
    if (nextBnkNo != -1) nextBnkNo++;
    return nextBnkNo;
  }
   
  private static int checkCustomerExists(int customerNo) {
    int validCustId = -1;
    try {
      checkCustId.setInt(1,customerNo);
      resultCheckCustId = checkCustId.executeQuery();
      if (resultCheckCustId.next()){
        validCustId = resultCheckCustId.getInt(1);
      } 
      //System.out.println("validCustId = " + validCustId);
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
    return validCustId;
  }

  private static void addNewBankAccountToExistingCustomer() {
    int custId = getCustomerId();
    int startBal = getStartingBalance();
    int newBankAccountNo = getNextBankAccountNo();
    if (checkCustomerExists(custId) != -1) {
      if (newBankAccountNo != -1 ) {
        insertNewBankAccount(newBankAccountNo,startBal);
        insertNewAccountHolding(newBankAccountNo,custId);
      } else {
        System.out.println("\nWARINING: Unable to create a new bank account Number. Please try again.");
      }
    } else {
        System.out.println("\nWARINING: The CustomerId does not Exist in our system.");
        System.out.println("Please try again with a valid CustomerId.");
      }
  }
  
  private static void displayAllCustomer() {
    try {
      resultAllCustomers = displayAllCustomers.executeQuery();
      System.out.println("**** Customer Table ****");
      System.out.println("CustomerId | CustomerName | CustomerAddress");
      while (resultAllCustomers.next()) {
        System.out.print(resultAllCustomers.getInt(1));
        System.out.print(" | ");
        System.out.print(resultAllCustomers.getString(2));
        System.out.print(" | ");
        System.out.println(resultAllCustomers.getString(3));
      }
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static void displayAccountHoldings() {
    try {
      resultAllAccountHoldings = displayAccountHoldings.executeQuery();
      System.out.println("**** AccountHoldings Table ****");
      System.out.println("AccountNumber | CustomerId ");
      while (resultAllAccountHoldings.next()) {
        System.out.print(resultAllAccountHoldings.getInt(1));
        System.out.print(" | ");
        System.out.println(resultAllAccountHoldings.getInt(2));
      }
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static void displayBankAccounts() {
    try {
      resultAllBankAccounts = displayAllBankAccounts.executeQuery();
      System.out.println("**** BankAccount Table ****");
      System.out.println("AccountNumber | Balance");
      while (resultAllBankAccounts.next()) {
        System.out.print(resultAllBankAccounts.getInt(1));
        System.out.print(" | ");
        System.out.println(resultAllBankAccounts.getInt(2));
      }
    } catch (Throwable e)  {   
      /*       Catch all exceptions and pass them to 
       **       the exception reporting method             */
      System.out.println(" . . . exception thrown:");
      errorPrint(e);
    } 
  }

  private static int clientStartMenu()
  {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    int result = -1;
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("\nPlease Select your option");
        System.out.println("1) Press 1 to Exit.");
        System.out.println("2) Press 2 to create a new Customer");
        System.out.println("3) Press 3 to add new Bank Account to Existing Customer");
        System.out.println("4) Press 4 to display all Customers");
        System.out.println("5) Press 5 to display all BankAccounts");
        System.out.println("6) Press 6 to display all AccountHoldings");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
      result = Integer.parseInt(ans);
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return result;
  } 

  private static int getCustomerId() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    int result = -1;
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Customers id.");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
      result =Integer.parseInt(ans);
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return result;
  } 

  private static String getCustomerName() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Customers name.");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return ans;
  } 

  private static String getCustomerAddress()  {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Customers Address.");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return ans;
  } 

  private static int getStartingBalance() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    int result =-1;
    try {
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Accounts Starting Balance. (Must be a Integer)");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
      result =Integer.parseInt(ans);
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return result;
  }  
}

