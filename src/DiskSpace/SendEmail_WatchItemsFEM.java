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


public class SendEmail_WatchItemsFEM
{

	public static void main(String [] args)
	{
		java.util.Date date = Calendar.getInstance().getTime(); 
		final Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		date = cal.getTime(); 
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  
		String strDate = dateFormat.format(date);

		String hosts[]={"va10puvfns003","va10puvfns001b","vaathmr463","vaathmr466","mom9puvwbs031","mom9puvwbs032",
				"mom9puvwbs304","mom9puvwbs305","va10puvwbs322","va10puvwbs323","va10puvcbi300","va10puvcbi301",
				"va10puvorc004","va10puvorc005","va10puvwbs007","va10puvwbs009","va10puvwbs010","va10puvwbs011","va10puvwbs012",
				"va10puvwbs016","va10puvwbs017","va10puvwbs018","va10puvwbs019","va10puvwbs020","va10puvwbs021","va10puvwbs022",
				"va10puvwbs026","va10puvwbs027","va10p10127","va10p10128","va10p10129","va10p10130","va10p10231","va10p10232","va10p10233",
				"va10p10234","va10p10235","va10p10236","va10p10237.anthem.com","va10p10238.anthem.com","va10p10248","va10p10249","va10p10250",
				"dc04puvwbs300","dc04puvwbs301","dc04puvwbs302","dc04puvwbs303","dc04puvwbs304","dc04puvwbs305","dc04puvwbs306",
				"dc04puvfns300","dc04puvfns301","va10puvwbs301","va10puvwbs125"};

		
		String cmd="df -g";
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

		List<String> hostlist = new ArrayList<String> ();
		List<String> fslist = new ArrayList<String> ();
		List<String> gbblocks = new ArrayList<String> ();
		List<String> freesp = new ArrayList<String> ();
		List<String> perused = new ArrayList<String> ();
		List<String> iused = new ArrayList<String> ();
		List<String> iusedper = new ArrayList<String> ();
		List<String> mountedon = new ArrayList<String> ();

		List<String> resultUnix = new ArrayList<String>();
		//boolean tag=true;
		
		for(int index=0;index<54;index++)
		{
			try {
				resultUnix = unixSSH(hosts[index],cmd);
			
			for(int i=0;i<resultUnix.size();i++) {
				System.out.println(resultUnix.get(i));
				String str[]=resultUnix.get(i).split("\\s+");

				for (int j=0;j<str.length;j++) {
					if(i==0)
						break;
					//System.out.println(str[j]);
					String everyData = str[j];
					if(everyData.contains("%")) {
						//System.out.println("everyData : "+everyData);
						String substrr=str[j].substring(0, str[j].length() - 1);
						//System.out.println("substrr : "+substrr);
						//System.out.println(" J : "+j);
						//if(((j%5==0) && (j/5==1) && Integer.valueOf(substrr)>70) && (!(Integer.valueOf(substrr)==100) && !(str[j].contains("msar"))))
						//!(rs.contains(str[6])) && !(str[6].contains("msar"))
						if((j%3==0) && (j/3==1) && !(rs.contains(str[6])) && Integer.valueOf(substrr)>49 && Integer.valueOf(substrr)<81
								&& !(str[6].equals("/ProdFileStore/Spec")) 
								&& !(str[6].equals("/ProdFileStore/Prov")) 
								&& !(str[6].equals("/ProdFileStore/Enroll"))
								&& !(str[6].equals("/ProdFileStore/Claim"))
								&& !(str[6].equals("/msar06"))
								&& !(str[6].equals("/msar09"))){
							/*
							if(str[6].contains("msar") && Integer.valueOf(substrr)>99) {
								break;
							}*/
							hostlist.add(hosts[index]);
							fslist.add(str[0]);
							gbblocks.add(str[1]);
							freesp.add(str[2]);
							perused.add(str[3]);
							iused.add(str[4]);
							iusedper.add(str[5]);
							mountedon.add(str[6]);
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
		//System.out.println("HII6");
		// email ID of Recipient. 
		String to = "DL-FileNetLightsOnSupport@anthem.com";  
		//String to = "sakya.samanta@elevancehealth.com"; 
		//String recipient = "Himgauri.Khaladkar@anthem.com";
		//String cc = "manjusha.veed@elevancehealth.com";
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
			message.setSubject("WatchItems for Disk Space check-Production Servers - "+strDate); 


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
							"<th>Iused</th>"+
							"<th>%Iused</th>"+
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
				if(percentage>=70)
					{
						builder.append("<td bgcolor='orange'>");
						builder.append(percentage);
						builder.append("</td>");
					}
					else
					{
						builder.append("<td bgcolor='green'>");
						builder.append(percentage);
						builder.append("</td>");
					}
				builder.append("<td>");
				builder.append(iused.get(ins));
				builder.append("</td>");
				builder.append("<td>");
				builder.append(iusedper.get(ins));
				builder.append("</td>");
				builder.append("<td>");
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
		String user = "srcfnprdauto";
		String pwd = "W7zGy3YZqLj-qr-SI8dBhQNXQ";
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


