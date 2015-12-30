/*
//- To start this java program make sure the database tables have been created.
//- SQL commands for creating the tables can be seen below.
q31. CREATE TABLES AND INSERTS USING DERBY IJ

java org.apache.derby.tools.ij
connect 'jdbc:derby:Q31_DB;create=true';

DROP TABLE ASSIGNMENT;
DROP TABLE PROJECT;
DROP TABLE EMPLOYEE;
DROP TABLE JOB;

CREATE TABLE JOB (
  job_code int NOT NULL,
  job_description varchar(40) NOT NULL,
  job_chg_hour int NOT NULL,
  PRIMARY KEY (job_code)
);

CREATE TABLE EMPLOYEE (
  emp_num int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  emp_lname varchar(40) NOT NULL,
  emp_fname varchar(40) NOT NULL,
  emp_initial varchar(2) NOT NULL,
  emp_hiredate date NOT NULL,
  job_code int NOT NULL,
  PRIMARY KEY (emp_num),
  FOREIGN KEY (job_code) REFERENCES JOB(job_code)
);
      
CREATE TABLE PROJECT(
  proj_num int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  proj_name varchar(40) NOT NULL,
  emp_num int UNIQUE NOT NULL,
  PRIMARY KEY (proj_num),
  FOREIGN KEY (emp_num) REFERENCES EMPLOYEE(emp_num)
);

CREATE TABLE ASSIGNMENT (
  assign_num int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  assign_date date NOT NULL,
  proj_num int NOT NULL,
  emp_num int NOT NULL,
  assign_hours int NOT NULL,
  assign_chg_hour int NOT NULL,
  assign_charge int NOT NULL,
  PRIMARY KEY (assign_num),
  FOREIGN KEY (proj_num) REFERENCES PROJECT(proj_num),
  FOREIGN KEY (emp_num) REFERENCES EMPLOYEE(emp_num)
);

INSERT INTO JOB VALUES(4523,'Cleaner',18);
INSERT INTO JOB VALUES(6587,'Programmer',25);

INSERT INTO EMPLOYEE VALUES(DEFAULT,'King','Lizzie','LK','22.05.2015',6587);
INSERT INTO EMPLOYEE VALUES(DEFAULT,'Queen','Bob','BQ','22.05.2015',4523);

INSERT INTO PROJECT VALUES(DEFAULT,'Project RocketShip',2);
INSERT INTO PROJECT VALUES(DEFAULT,'Project Submarine',1);

INSERT INTO ASSIGNMENT VALUES(DEFAULT,'25.05.2015',1,2,10,250,500);
INSERT INTO ASSIGNMENT VALUES(DEFAULT,'23.05.2015',2,1,20,400,100);

SELECT * FROM JOB;
SELECT * FROM EMPLOYEE;
SELECT * FROM PROJECT;
SELECT * FROM ASSIGNMENT;

  //- Make sure derby server is running first
  java -jar /Users/lizardmagic/apache/dberby-10.11.1.1-bin/lib/derbyrun.jar server start

//- TO SHOW THE INSERT WORKED THROUGH IJ, ONCE WE SHUTDOWN THE SERVER
java org.apache.derby.tools.ij
connect 'jdbc:derby:Q31_DB;create=true';
SELECT * FROM EMPLOYEE;
  */
import java.sql.*;
import java.io.*;

public class Employee {
   public static void main(String[] args) {
       System.out.println("Inserting values in derby  database employee table!");
       String driver = "org.apache.derby.jdbc.ClientDriver";
       // the database name
       String dbName="Q31_DB";
       // define the Derby connection URL to use
       String protocol = "jdbc:derby://localhost:1527/";
       String connectionURL = protocol + dbName + ";";
       Connection conn = null;
       try{
           Class.forName(driver);
           conn = DriverManager.getConnection(connectionURL);
           System.out.println("Connected to database " + dbName);

           try {
               conn.setAutoCommit(false);
               Statement st = conn.createStatement();
               String lname = getEmployeeLastName();
               String fname = getEmployeeFirstName();
               String initial = fname.substring(0,1) + lname.substring(0,1);
               String startDate = getEmployeeStartDate(); 
               int jobCode = getJobCode();
               //int val = st.executeUpdate("INSERT INTO JOB VALUES("+1+","+"'newjob'"+","+300+")");
               int val1 = st.executeUpdate("INSERT INTO EMPLOYEE VALUES("+"DEFAULT,'"+lname+"','"+fname+"','"+initial+"','"+startDate+"',"+jobCode+")");
               //- Below is a query that will fail and role back as the job_code does not exist. Uncomment to test
               //int val2 = st.executeUpdate("INSERT INTO EMPLOYEE VALUES("+"DEFAULT,'Fail,"+"'Demonstrate RollBack',"+"'DF',"+"'04.06.2015',"+6666+")");
               //commit this addition
               conn.commit();
               System.out.println("1 row affected");
           } catch (SQLException sqle) {
               String theError = (sqle).getSQLState();
               if (theError.equals("40XL1"))
               { conn.rollback();
                  System.out.println(" *** employee *** NOT *** Inserted !!!");
               } // error with another transac
               else
               throw sqle;
           }
           conn.setAutoCommit(true);
           conn.close();
           System.out.println("Closed connection");
       }
       catch (Exception e){
           e.printStackTrace();
       }
   }

    private static String getEmployeeFirstName() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Employee's first name.");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return ans;
  } 

  private static String getEmployeeLastName() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Employee's Last name.");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return ans;
  } 

  private static String getEmployeeStartDate() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    try { 
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Employee's Start Date.");
        System.out.println("Epexted Date Format: dd.mm.yyyy");
        ans = br.readLine();
        if ( ans.length() == 0 ) 
          System.out.print("Nothing entered: ");
      }
    } catch (java.io.IOException e) {
      System.out.println("Could not read response from stdin"); 
    }
    return ans;
  } 

  private static int getJobCode() {
    BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
    String ans = "";
    int result =-1;
    try {
      while ( ans.length() == 0 ) {
        System.out.println("Please Enter the Employee's JobCode.");
        System.out.println("Enter one of the valid job_code: 6587 or 4523");
        System.out.println("Enter in a differnt number, other then the two listed above to see a rollback occur.");
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
