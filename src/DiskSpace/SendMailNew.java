package DiskSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*; 
import javax.mail.internet.*;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SendMailNew
{

	public static void main(String [] args)
	{
		
		java.util.Date date = Calendar.getInstance().getTime(); 
		final Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		date = cal.getTime(); 
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  
		String strDate = dateFormat.format(date);

		String hosts[]={
				"dc04plvwbs370",
				"dc04plvihs308",
				"dc04plvrpd301",
				"dc04plvwbs367",
				"dc04plvwbs369",
				"dc04plvwbs377",
				"va10plvwbs604",
				"va10plvwbs605"

};
	

		String cmd="df";
		List<String> rs = new ArrayList<String> ();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//fnetcengn-p-01.internal.das:1525/fnetcep", "SRCDATABASE", "ECMstores1#");
			Statement stmt = con.createStatement();

			//1st Excel Data
			String query1 ="select display_name,fs_ads_path from EASTCLAIMS.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result1 = stmt.executeQuery(query1);
			while(result1.next()) {
				rs.add(result1.getString(2));			
			}

			String query2 ="select display_name,fs_ads_path from WESTCLAIMS.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result2 = stmt.executeQuery(query2);

			while(result2.next()) {
				rs.add(result2.getString(2));			
			}
			String query3 ="select display_name,fs_ads_path from CENTRALCLAIMS.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result3 = stmt.executeQuery(query3);

			while(result3.next()) {
				rs.add(result3.getString(2));			
			}
			String query4 ="select display_name,fs_ads_path from ENROLLMENT.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result4 = stmt.executeQuery(query4);

			while(result4.next()) {
				rs.add(result4.getString(2));			
			}
			String query5 ="select display_name,fs_ads_path from HR.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result5 = stmt.executeQuery(query5);

			while(result5.next()) {
				rs.add(result5.getString(2));			
			}
			String query6 ="select display_name,fs_ads_path from FEP.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result6 = stmt.executeQuery(query6);

			while(result6.next()) {
				rs.add(result6.getString(2));			
			}
			String query7 ="select display_name,fs_ads_path from CFSHOME.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result7 = stmt.executeQuery(query7);

			while(result7.next()) {
				rs.add(result7.getString(2));			
			}
			String query8 ="select display_name,fs_ads_path from PROVIDER.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result8 = stmt.executeQuery(query8);

			while(result8.next()) {
				rs.add(result8.getString(2));			
			}
			String query9 ="select display_name,fs_ads_path from FINANCE.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result9 = stmt.executeQuery(query9);

			while(result9.next()) {
				rs.add(result9.getString(2));			
			}
			String query10 ="select display_name,fs_ads_path from LEGAL.STORAGECLASS where fs_ads_path like '%/SA%' and (area_status=1 or area_status=3) order by display_name";
			ResultSet result10 = stmt.executeQuery(query10);

			while(result10.next()) {
				rs.add(result10.getString(2));			
			}
			stmt.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}

		String result = null;
		//String status = null;

		List<String> hostlist = new ArrayList<String> ();
		List<String> fslist = new ArrayList<String> ();
		List<String> gbblocks = new ArrayList<String> ();
		List<String> freesp = new ArrayList<String> ();
		List<String> perused = new ArrayList<String> ();
		List<String> used = new ArrayList<String> ();
		//List<String> iusedper = new ArrayList<String> ();
		List<String> mountedon = new ArrayList<String> ();

		List<String> resultUnix = new ArrayList<String>();
		//boolean tag=true;
		
		for(int index=0;index<8;index++)
		{
			try {
				resultUnix = unixSSH(hosts[index],cmd);
			System.out.println(hosts[index]);
			for(int i=0;i<resultUnix.size();i++) {
				System.out.println(resultUnix.get(i));
				String str[]=resultUnix.get(i).split("\\s+");

				for (int j=0;j<str.length;j++) {
					if(i==0)
						break;
					//System.out.println("J : "+j);
					//System.out.println("String Value : "+str[j]);
					String everyData = str[j];
					if(everyData.contains("%")) {
						//System.out.println("everyData : "+everyData);
						String substrr=str[j].substring(0, str[j].length() - 1);
						if((j%4==0) && (j/4==1) && !(rs.contains(str[5])) && Integer.valueOf(substrr)>70)
						{	
							if(str[5].contains("msar") && Integer.valueOf(substrr)>99) {
								break;
							}
							//System.out.println("str[6] = "+str[6]);
							hostlist.add(hosts[index]);
							fslist.add(str[0]);
							gbblocks.add(str[1]);
							freesp.add(str[3]);
							perused.add(str[4]);
							used.add(str[2]);
							//iusedper.add(str[5]);
							mountedon.add(str[5]);
							//tag=false;
						
						}
					}
				}
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end of for
		// email ID of Recipient. 
		//String to = "DL-FileNetLightsOnSupport@anthem.com";  
		String to = "sakya.samanta@elevancehealth.com";
		// email ID of  Sender. 
		String sender = "DL-FileNetLightsOnSupport@anthem.com"; 

		// using host as localhost 
		String host = "smtp.wellpoint.com"; 

		// Getting system properties 
		Properties properties = System.getProperties(); 

		// Setting up mail server 
		properties.setProperty("mail.smtp.host", host); 

		// creating session object to get properties 
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties); 

		//System.out.println("HII5");
		try 
		{ 
			// MimeMessage object. 
			MimeMessage message = new MimeMessage(session); 

			// Set From Field: adding senders email to from field. 
			message.setFrom(new InternetAddress(sender)); 


			// Set To Field: adding recipient's email to from field. 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient1)); 

			// Set Subject: subject of the email 
			message.setSubject("Disk Space check-New Production Servers - "+strDate); 


			StringBuilder builder = new StringBuilder();

			builder.append
			(
					"<html>"+ 
							"<body>"+
							"<table border=1>"+
							"<tr bgcolor='#b2beb5'>"+
							"<th>Server</th>"+
							"<th>Filesystem</th>"+
							"<th>512 blocks</th>"+
							"<th>Free</th>"+
							"<th>%Used</th>"+
							"<th>Used</th>"+
							//"<th>%Iused</th>"+
							"<th>Mounted on</th></tr>"
					);
			builder.append("<tr>");
			for(int ins=0;ins<fslist.size();ins++)
			{
				int percentage=Integer.parseInt(perused.get(ins).substring(0, perused.get(ins).length() - 1));
				builder.append("<td>");
				builder.append(hostlist.get(ins));
				builder.append("</td>");
				builder.append("<td>");
				builder.append(fslist.get(ins));
				builder.append("</td>");
				builder.append("<td>");

				builder.append(gbblocks.get(ins));
				builder.append("</td>");
				builder.append("<td>");
				builder.append(freesp.get(ins));
				builder.append("</td>");
				if(percentage<80)
				{
					builder.append("<td bgcolor='green'>");
					builder.append(percentage);
					builder.append("</td>");
				}
				else 
					if(percentage>=80 && percentage<90)
					{
						builder.append("<td bgcolor='orange'>");
						builder.append(percentage);
						builder.append("</td>");
					}
					else
					{
						builder.append("<td bgcolor='red'>");
						builder.append(percentage);
						builder.append("</td>");
					}
				builder.append("<td>");
				builder.append(used.get(ins));
				builder.append("</td>");
				builder.append("<td>");
				/*
				builder.append(iusedper.get(ins));
				builder.append("</td>");
				builder.append("<td>");
				*/
				builder.append(mountedon.get(ins));
				builder.append("</td>");
				builder.append("</tr>");
			}
			builder.append("</table></body></html>");
			result=builder.toString();
			message.setContent(result,"text/html");

			Transport.send(message);
			System.out.println("Mail successfully sent"); 

		}
		catch (MessagingException mex)  
		{ 
			mex.printStackTrace();
			//System.out.println("here");
		}
		System.exit(0);
	}
	private static List<String> unixSSH(String host, String cmd) throws JSchException, IOException{
		//String command = "df -g";
		List<String> lines = new LinkedList<String>();
		
		String user = "AN533260AD";
		String pwd = "XZaXa28L7fqE65S4X-!S";


		//String user = "srcfnprdauto";
		//String pwd = "W7zGy3YZqLj-qr-SI8dBhQNXQ";	

		Session session = null;
		JSch jSch = new JSch();
		session = jSch.getSession(user, host, 22);
		session.setPassword(pwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		ChannelExec channel = null;
		channel =(ChannelExec) session.openChannel("exec");
		InputStream in = channel.getInputStream();
		channel.setCommand(cmd);
		channel.setErrStream(System.err);
		channel.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		channel.disconnect();
		session.disconnect();
		return lines;

	}
} 


