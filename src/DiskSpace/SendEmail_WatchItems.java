package DiskSpace;


import java.io.InputStream;
import java.util.*;
import javax.mail.*; 
import javax.mail.internet.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import javax.mail.Transport; 


public class SendEmail_WatchItems {

	/*
		public String getCredentials()
		{
			Properties prop1 = new Properties();
			String propFileName = "config.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) 
			{
				try {
					prop1.load(inputStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else 
			{
				try {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//String Username = prop1.getProperty("Username");
			//String password = prop1.getProperty("password");

			String Username = "srcfnprdauto";
			String password = "PRD4#Aut213";

			return Username+" "+password;		
		}
	 */
	public static void main(String [] args)
	{
		/*
			SendEmail_WatchItems se=new SendEmail_WatchItems();

			String credentials=se.getCredentials();
			String credentialsplit[]=credentials.split(" ");
			String Username=credentialsplit[0];
			String password=credentialsplit[1];

			//String Username=args[0];
			//String password=args[1];
		 */
		String Username = "srcfnprdauto";
		String password = "xaFN>VpisTJ9GR?p7ad95]>Sw";

		String hosts[]={"va10puvfns001b","mom9puvwbs032","mom9puvwbs304","mom9puvwbs305","mom9puvwbs031","va10puvfns003","va10puvorc004","va10puvorc005",
				"va10p10232.anthem.com","va10puvwbs007","va10puvwbs009","va10puvwbs010","va10puvwbs011","va10p10248","va10p10249",
				"va10puvwbs012","va10puvwbs016","va10puvwbs017","va10puvwbs018","va10puvwbs019","va10puvwbs020","va10puvwbs021",
				"va10puvwbs022","va10puvwbs026","va10puvwbs027","vaathmr463","va10puvwbs322","va10plvwbs604",
				"va10plvwbs605","va10p10127","va10p10128","va10p10129","va10p10130","va10p10231.anthem.com","va10p10233.anthem.com",
				"va10p10234.anthem.com","va10p10235.anthem.com","va10p10236.anthem.com","va10p10237.anthem.com","va10p10238.anthem.com",
		"va10p10250","va10puvcbi300","va10puvcbi301","dc04puvwbs300","dc04puvwbs301","dc04puvwbs304","dc04puvwbs305","dc04puvwbs306"};
		//String hosts[]={"va10puvwbs027"};

		String command1	="df";
		String strtmp	=null;
		String result	=null;

		List<String> fslist		=new ArrayList<String> ();
		List<String> hostlist	=new ArrayList<String> ();
		List<String> iused		=new ArrayList<String> ();
		List<String> iusedper	=new ArrayList<String> ();
		List<String> perused	=new ArrayList<String> ();
		List<String> gbblocks	=new ArrayList<String> ();
		List<String> mountedon	=new ArrayList<String> ();
		List<String> freesp		=new ArrayList<String> ();


		for(int index=0;index<49;index++)
		{
			try
			{					
				java.util.Properties config = new java.util.Properties(); 
				config.put("StrictHostKeyChecking", "no");
				JSch jsch = new JSch();

				Session session=jsch.getSession(Username, hosts[index], 22);
				session.setPassword(password);
				session.setConfig(config);
				session.connect();

				System.out.println("Connected");

				System.out.println(hosts[index]);

				Channel channel=session.openChannel("exec");

				((ChannelExec)channel).setCommand(command1);

				channel.setInputStream(null);

				((ChannelExec)channel).setErrStream(System.err);

				InputStream in=channel.getInputStream();

				channel.connect();
				//\src\main\java\pageClasses\
				byte[] tmp=new byte[8192];
				while(true)
				{
					while(in.available()>0)
					{

						int i=in.read(tmp, 0,8192);
						if(i<0)break;
						strtmp=new String(tmp, 0, i);
						System.out.println(strtmp);
						String strArr[]=strtmp.split("\n");  
						for(int ind=1;ind<strArr.length+1;ind++)
						{
							String newstr[]=strArr[ind].split("\\s+");

							for(int ind1=0;ind1<newstr.length;ind1++)
							{
								if(newstr[ind1].contains("%"))
								{
									String substrr=newstr[ind1].substring(0, newstr[ind1].length() - 1);
									if(((ind1%3==0)&&(ind1/3==1)&&Integer.valueOf(substrr)>50&&Integer.valueOf(substrr)<76))
									{
										hostlist.add(hosts[index]);
										fslist.add(newstr[0]);
										gbblocks.add(newstr[1]);
										freesp.add(newstr[2]);
										perused.add(newstr[3]);
										iused.add(newstr[4]);
										iusedper.add(newstr[5]);
										mountedon.add(newstr[6]);

										System.out.println(strArr[ind]);
									}
								}
							}
						}
						strtmp=null;
					}


					System.out.println("DONE");
					if(channel.isClosed())
					{
						System.out.println("exit-status: "+channel.getExitStatus());
						break;
					}
					try
					{
						Thread.sleep(1000);
					}
					catch(Exception ee)
					{
						System.out.println();
					}

				}  

				channel.disconnect();
				session.disconnect(); 

				//conn.close();
			}//end of try 
			catch(Exception e)
			{

			}

		}//end of for

		// email ID of Recipient. 
		String recipient = "DL-FileNetLightsOnSupport@anthem.com";
		//String recipient = "sakya.samanta@elevancehealth.com";
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


			// Set To Field: adding recipient's email to from field. 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); 


			// Set Subject: subject of the email 
			message.setSubject("WatchItems for Disk Space check-Production Servers"); 


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
				if(percentage>50 &&percentage<65)
				{
					builder.append("<td bgcolor='green'>");
					builder.append(percentage);
					builder.append("</td>");
				}
				else 
					if(percentage>=65&&percentage<76)
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
		}
		System.exit(0);
	}

}
