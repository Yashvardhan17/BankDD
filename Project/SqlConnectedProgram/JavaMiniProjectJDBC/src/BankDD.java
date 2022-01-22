import java.io.IOException;		// all imported packages used.
import java.security.SecureRandom;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import java.sql.*;
interface basics					//interface deceleration		
{
	void SetName(String name);
	void SetAccID(String AccID);	
}
class Sender implements basics		//class implementing interface. Also has its own characters
{
	private String Sname;			//class fields/Members
	private String SAccID;
	private String AuthPass;
	private double SAmt;
	public void SetName(String a)	//Mutator and Accessor Functions for all files
	{
		this.Sname=a;
	}
	public void SetAccID(String Id)
	{
		this.SAccID= Id;
	}
	public void SendAmount(double a)
	{
		this.SAmt=a;
	}
	public String getSname()
	{
		return Sname;
	}
	public String getSID()
	{
		return SAccID;
	}
	public  double getSAmount()
	{
		return SAmt;
	}
	boolean  SetSendStatus()
	{
		return true;
	}
	public void setAuthPass(String s)
	{
		this.AuthPass=s;
	}
	public String getAuthPass( )
	{
		return AuthPass;
	}
}
class Receiver implements basics			//class Receiver implements interface . Also has its own implementations
{
	
	private String Rname;					//class fields
	private String RAccID;
	private double Rexpamt;
	public void SetName(String a)			//Mutator and Accessor Functions
	{
		this.Rname=a;
	}
	public void SetAccID(String Id)
	{
		this.RAccID= Id;
	}
	public String getSname()
	{
		return Rname;
	}
	public String getSID()
	{
		return RAccID;
	} 
	public void setRExpectedamt(double a)
	{
		this.Rexpamt=a;
	}
	public double getRExpectedamt()
	{
		return this.Rexpamt;
	}
		
}
// Driver Class or main Class
public class BankDD {
	
	public static void main(String[] args) throws IOException			//main function , Note Throws IOExceptions
	{
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(
			"jdbc:mysql://localhost:3306/BankData","root","javaprogramJDBC");
			//here BankData is database name, root is username and password
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from BankDetails");
			while(rs.next())
			System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			
		Sender send = new Sender();										//object creation of both classes (Sender and Receiver)
		Receiver receive = new Receiver();
		int confirm = 0;
		int k,c=3;													//local variables
		String passSender = null;
		Instruction();
		JOptionPane.showMessageDialog(null, "-----------------------  **  WELCOME TO CHECK-IN AND CHECK-OUT BANK DEMAND DRAFT(DD) SYSTEM  **  -----------------------------");
		//code for receiving Senders Information
		do {														//do-while loop
		JOptionPane.showMessageDialog(null, "***Please Provide The Sender(s) Information As Requested***");
		String s=JOptionPane.showInputDialog("*Enter Sender(s) Name :");
		System.out.println(s);
		send.SetName(s);											//Function call with object
		String s1=JOptionPane.showInputDialog("*Enter Sender(s) Account-ID :");
		send.SetAccID(s1);											//Function call with object
		try{														//Invalid Input Exception Handling using Try/Catch Block
			String s2=JOptionPane.showInputDialog("*Enter The Amount($) To Be Sent :");
			double a= Double.parseDouble(s2);
			send.SendAmount(a);	
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "*Exception Occured\n*You Can Only Enter Decimal-point-Numbers as Amount($)*\n*INVALID INPUT*\n*Sorry Try Again* ");
			System.exit(1);
		}
			//Function call with object
		k=JOptionPane.showConfirmDialog(null, "Confirm Sender(s) Details:\n -Sender(s) Name : "+send.getSname()+"\n *Sender's Account-ID : "+send.getSID()+"\n *Sending Aomunt($) : "+send.getSAmount());
		if((k==0))											//if-else ladder
			confirm=0;
		else if(k==1)
			confirm=1;
		else if(k==2)
		{
			JOptionPane.showMessageDialog(null, "-----------   Canceling Transaction------------\n   Exiting!.......");
			System.exit(1);
		}
		}while(confirm!=0);									//terminate condition
		//Displaying PassWord And Status
		if(send.SetSendStatus())
			JOptionPane.showMessageDialog(null, "Sender(s) Account Status -- "+send.SetSendStatus()+"\n *Account Activated*\n *Information Successfully SAVED* \n **Genrating Unique Authentication PassWord(5-digit-Alphanumeric)**");
			passSender=generateRandomPassword();			//Authentication Password Creation at Senders End
			System.out.println(passSender);
			send.setAuthPass(passSender);
			JOptionPane.showMessageDialog(null, " Genrated-PassWord : "+send.getAuthPass()+"\n**Share This PassWord With The RECEIVER(ONLY)** ");
		//Similar to Sender(s) Loop code
		//Receiving Receivers Information from Below Code
		do 
		{
			JOptionPane.showMessageDialog(null, "*(Please Provide The RECEIVER(s) Information As Requested)*");
			String r=JOptionPane.showInputDialog("Enter Receiver(s) Name :");
			receive.SetName(r);
			String r1=JOptionPane.showInputDialog("Enter RECEIVER(s) Account-ID :");
			receive.SetAccID(r1);
			try {
			String r2=JOptionPane.showInputDialog("Enter The Amount($) To Be RECEIVED(Expected) :");
			double b= Double.parseDouble(r2);
			receive.setRExpectedamt(b);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "*You Can Only Enter Decimal-point-Numbers as Amount($)\n*INVALID INPUT*\nSorry Try Again ");
				System.exit(1);
			}
			k=JOptionPane.showConfirmDialog(null, "Confirm Receiver(s) Details: \n *Receiver(s) Name : "+receive.getSname()+"\n Receiver(s) Account-ID : "+receive.getSID()+"\n Expected Aomunt : "+receive.getRExpectedamt());
			if((k==0))
				confirm=0;
			else if(k==1)
				confirm=1;
			else if(k==2)
			{
				JOptionPane.showMessageDialog(null, "-----------  Canceling Transaction------------  \n Exiting!.......");
				System.exit(1);
			}
		}while(confirm!=0);					//Termination Condition
		
			// Code For Indentity Confirmation Of Receiver
			JOptionPane.showMessageDialog(null, "*****IDENTITY CONFIRMATION*****\n........  !PROCEDURE NEXT!  ........");
			String checksname=JOptionPane.showInputDialog("Please Confirm The SENDER(s) Name(FOR CONFIRMATION) : ");
			RecAccStatus(checksname,send.getSname());
			String pass=JOptionPane.showInputDialog("Please Provide The Authentication-Password (As Given By Sender) To Complete Transaction:");
			//code to check PassWord Provided at Senders End Which should be Told To the Receiver by Sender
			if(pass.equals(send.getAuthPass()))
				JOptionPane.showMessageDialog(null, "**Transaction Completed**\n Succesfuly Received The Amount($)");
			else {
				//providing 3 tries to enter password
			while(!pass.equals(send.getAuthPass()))
				{
		
				JOptionPane.showMessageDialog(null, "!! *Authentication PassWord Does Not Match* !!\n *Please Try Again");
				pass=JOptionPane.showInputDialog("TRIES REMAINING("+(c-1)+")\n Enter PassWord Again:");
				c--;
				if(c==(0+1))
				{
				JOptionPane.showMessageDialog(null, "PASSWORD AUTHENTICATION -- FAILED!! \n ....CANCELLING AND EXITING....");
				System.exit(1);
				}
				if(pass.equals(send.getAuthPass()))
				JOptionPane.showMessageDialog(null, "!!HOORAY!! \n --PASSWORD MATHCHED-- \n ^^^^Transaction Completed^^^^\n ****Succesfuly Received The Amount($)****");
				}
			}
			
			double a=send.getSAmount(),b=receive.getRExpectedamt();
			double need=recamnt(a,b);		//Function Call to calculate Remaining Amount to Be Received based on Expectation
			//TRANSACTION SUMMARY
			JOptionPane.showMessageDialog(null, "------------------------------------------------------\nSENDERS DETAILS:-\n------------------------------------------------------\n *NAME:"+send.getSname()+"\n *ACCOUNT-ID:"+send.getSID()+"\n *ACCOUNT-STATUS :ACTIVE\n *TRANSACTION-STATUS: COMPLETED\n *SENT-AMOUNT($):"+send.getSAmount()+"\n------------------------------------------------------\n (VEREFIED) RECEVIERS DETAILS:\n------------------------------------------------------\n *NAME:"+receive.getSname()+"\n *ACCOUNT-ID : "+receive.getSID()+"\n *AMOUNT(EXPECTED) : "+receive.getRExpectedamt()+"\n *AMOUNT(RECEIVED) : "+send.getSAmount()+"\n *Amount(REMAINING/LEFT-TO-RECEIVE) : "+need+"\n *TRANSACTION STATUS : SUCCUSESFUL(RECEIVED)"+"\n------------------------------------------------------\n           (TRANSACTION)DATE AND TIME: "+DatenTime()+"\n------------------------------------------------------");
			String p=send.getSname();
			String q=send.getSID();
			String r=receive.getSname();
			String s=receive.getSID();
			double t=send.getSAmount();
			//String Query = "Insert into BankDetails"+"values ("+p+","+q+","+r+","+s+","+t+")";
			stmt.executeUpdate("INSERT INTO BankDetails(Sender_Name,Sender_Account_ID,Reciever_Name,Reciever_Account_ID,Transaction_Amount) VALUE ('"+p+"','"+q+"','"+r+"',"+s+",'"+t+"')");   //Here All the Information is Stored in Database
			//stmt.executeUpdate(Query);
			
			con.close();
		}catch(Exception e)
	{ 
			System.out.println(e);
		}
		}
	public static String generateRandomPassword()
    {
        // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
 
        // each iteration of loop choose a character randomly from the given ASCII range
        // and append it to StringBuilder instance
 
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
 
        return sb.toString();
    }
	public static void RecAccStatus(String checkname,String a)		//Function Definition to check the Senders Name at Receivers end
	{
		if(checkname.equalsIgnoreCase(a))
		JOptionPane.showMessageDialog(null, "Transaction(SEND) Request Pending \n Received From "+a+"\n PRESS OK!!");
		else {
		JOptionPane.showMessageDialog(null, "No Transcation Request Pending From :"+checkname+"!!\n Account Holder INACTIVE!! ....\n Exiting The Transaction......");
		System.exit(1);
		}
	}
	public static double recamnt(double a,double b)				//Function Definition to get the Difference in Expected and Received Amount
	{
		double c = 0;
		if(a>b)
		c=0;
		else if(b>a)
		c= b-a; 
		return c;
		
	}
	public static void Instruction()							//Function to Display Some Rules
	{
		JOptionPane.showMessageDialog(null, "*****  ! Please Read The Rules CareFully Before Using The Software !  *****\n *Yes - Continues The Procedure\n *No- Retry\n *Cancel - Terminates The Transaction Process.\n *Please Enter Amount in Number(Decimals Allowed)\n *Take Note of Senders Name And Authentication-PassWord  ");
	}
	public static String DatenTime()							//Function to Return Date And Time 	
	{
		DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
    	String dateString2 = dateFormat2.format(new Date(0)).toString();
    	return dateString2;
	}
}


