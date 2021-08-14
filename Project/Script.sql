Create Database BankData;
Use BankData;
Select * from all_tables;
Show Tables;
Create table BankDetails(Sender_Name varchar(256), Sender_Account_ID varchar(256),Reciever_Name varchar(256), Reciever_Account_ID varchar(256), 
Transaction_Amount double(10,2));
Select *from BankDetails where Sender_Name= 'Yash'And Transaction_Amount=2000;
Insert into BankDetails values('Yash', '1234', 'Yashu' , '4321' , '2000');
Delete from BankDetails where Sender_Name = 'a';
 