package DiskSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*; 
import javax.mail.internet.*;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import javax.mail.Transport; 


public class InodeCheck
{

	public static void main(String [] args) throws Exception, IOException
	{
		java.util.Date date = Calendar.getInstance().getTime(); 
		final Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		date = cal.getTime(); 
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  
		String strDate = dateFormat.format(date);

		String hosts[]={"va10puvwbs016","va10puvwbs017","va10puvwbs026","va10puvwbs027"};

		String strtmp = null;
		String result = null;
		String status = null;

		List<String> hostlist = new ArrayList<String> ();
		List<String> fslist = new ArrayList<String> ();
		List<String> gbblocks = new ArrayList<String> ();
		List<String> freesp = new ArrayList<String> ();
		List<String> perused = new ArrayList<String> ();
		List<String> iused = new ArrayList<String> ();
		List<String> iusedper = new ArrayList<String> ();
		List<String> mountedon = new ArrayList<String> ();

		List<String> resultUnix = new ArrayList<String>();

		for(int index=0;index<4;index++)
		{
			resultUnix = unixSSH(hosts[index]);
			for(int i=0;i<resultUnix.size();i++) {
				System.out.println(resultUnix.get(i));
				String str[]=resultUnix.get(i).split("\\s+");
				
				for (int j=0;j<str.length;j++) {
					if(i==0)
						break;
					System.out.println(str[j]);
					String everyData = str[j];
					if(everyData.contains("%")) {
						String substrr=str[j].substring(0, str[j].length() - 1);
						//System.out.println("substrr : "+substrr+"  ind1 : "+ind1);
						if(((j%5==0) && (j/5==1) && Integer.valueOf(substrr)>70) && (!(Integer.valueOf(substrr)==100) && !(str[j].contains("msar"))))
						{
							hostlist.add(hosts[index]);
							fslist.add(str[0]);
							gbblocks.add(str[1]);
							freesp.add(str[2]);
							perused.add(str[3]);
							iused.add(str[4]);
							iusedper.add(str[5]);
							mountedon.add(str[6]);

							//System.out.println(strArr[ind]);
						}
					}
				}
			}
		}//end of for

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


		try 
		{ 
			// MimeMessage object. 
			MimeMessage message = new MimeMessage(session); 

			// Set From Field: adding senders email to from field. 
			message.setFrom(new InternetAddress(sender)); 

			//Date d=new Date();

			// Set To Field: adding recipient's email to from field. 
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
			//message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));

			// Set Subject: subject of the email 



			StringBuilder builder = new StringBuilder();

			builder.append
			(
					"<html>"+ 
							"<body>"+
							"<table border=1>"+
							"<tr bgcolor='#b2beb5'>"+
							"<th>Server</th>"+
							"<th>Filesystem</th>"+
							"<th>GB blocks</th>"+
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
				int ipercentage=Integer.parseInt(iusedper.get(ins).substring(0, iusedper.get(ins).length() - 1));
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
				builder.append("<td>");
				builder.append(percentage);
				builder.append("</td>");


				builder.append("<td>");
				builder.append(iused.get(ins));
				builder.append("</td>");

				if(ipercentage>75 &&ipercentage<80)
				{
					builder.append("<td bgcolor='green'>");
					builder.append(ipercentage);
					builder.append("</td>");

				}
				else 
					if(ipercentage>=80&&ipercentage<90)
					{
						builder.append("<td bgcolor='orange'>");
						builder.append(ipercentage);
						builder.append("</td>");
						status="Amber";
					}
					else if(ipercentage>=90&&ipercentage<=100)
					{
						builder.append("<td bgcolor='red'>");
						builder.append(ipercentage);
						builder.append("</td>");
						status="Red";
					}
					else 
					{
						builder.append("<td bgcolor='green'>");
						builder.append(ipercentage);
						builder.append("</td>");
						status="Green";
					}

				builder.append("<td>");
				builder.append(mountedon.get(ins));
				builder.append("</td>");
				builder.append("</tr>");
			}
			builder.append("</table></body></html>");
			result=builder.toString();
			message.setContent(result,"text/html");
			message.setSubject("Inode Space check-Production Servers :"+" "+strDate+" "+status);
			Transport.send(message);
			System.out.println("Mail successfully sent"); 

		}
		catch (MessagingException mex)  
		{ 
			mex.printStackTrace(); 
		}
		System.exit(0);
	}
	private static List<String> unixSSH(String host) throws JSchException, IOException{
		String command = "df -g";
		List<String> lines = new LinkedList<String>();
		String user = "srcfnprdauto";
		String pwd = "xaFN>VpisTJ9GR?p7ad95]>Sw";
		Session session = null;
		JSch jSch = new JSch();
		session = jSch.getSession(user, host, 22);
		session.setPassword(pwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		ChannelExec channel = null;
		channel =(ChannelExec) session.openChannel("exec");
		InputStream in = channel.getInputStream();
		channel.setCommand(command);
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

