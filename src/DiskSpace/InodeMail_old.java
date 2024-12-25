package DiskSpace;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*; 
import javax.mail.internet.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import javax.mail.Transport; 
  
  
public class InodeMail_old
{
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
			String Username = prop1.getProperty("Username");
			String password = prop1.getProperty("password");
			
		
			return Username+" "+password;	
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
		String Username = prop1.getProperty("Username");
		String password = prop1.getProperty("password");
				
	
		return Username+" "+password;	

			
	}
	*/
	public static void main(String [] args)
	{
	/*
		InodeMail se=new InodeMail();
		String credentials=se.getCredentials();
		String credentialsplit[]=credentials.split(" ");
		String Username=credentialsplit[0];
		String password=credentialsplit[1];
		*/
		String Username = "srcfnprdauto";
		String password = "PRD4#Aut213";
		
		java.util.Date date = Calendar.getInstance().getTime(); 
		final Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		date = cal.getTime(); 
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  
		String strDate = dateFormat.format(date);
		
		String hosts[]={"va10puvwbs016","va10puvwbs017","va10puvwbs026","va10puvwbs027"};
		
		String command1	="df -g";
		String strtmp	=null;
		String result	=null;
		String status=null;
		
		List<String> fslist		=new ArrayList<String> ();
		List<String> hostlist	=new ArrayList<String> ();
		List<String> iused		=new ArrayList<String> ();
		List<String> iusedper	=new ArrayList<String> ();
		List<String> perused	=new ArrayList<String> ();
		List<String> gbblocks	=new ArrayList<String> ();
		List<String> mountedon	=new ArrayList<String> ();
		List<String> freesp		=new ArrayList<String> ();
		
		for(int index=0;index<4;index++)
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
	
				byte[] tmp=new byte[8192];
				while(true)
				{
				while(in.available()>0)
				{
			
						int i=in.read(tmp, 0,8192);
						if(i<0)break;
						strtmp=new String(tmp, 0, i);
						String strArr[]=strtmp.split("\n");  
						for(int ind=1;ind<strArr.length+1;ind++)
						{
						   	String newstr[]=strArr[ind].split("\\s+");
						   	
						   	for(int ind1=0;ind1<newstr.length;ind1++)
						   	{
						   		if(newstr[ind1].contains("%"))
						   		{
						   			String substrr=newstr[ind1].substring(0, newstr[ind1].length() - 1);
						   			System.out.println("substrr : "+substrr+"  ind1 : "+ind1);
						   			if(((ind1%5==0) && (ind1/5==1) && Integer.valueOf(substrr)>70) && (!(Integer.valueOf(substrr)==100) && !(newstr[ind1].contains("msar"))))
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
	     // String to = "DL-FileNetLightsOnSupport@anthem.com";  
	      String to = "sakya.samanta@elevancehealth.com"; 
	      //String recipient = "Himgauri.Khaladkar@anthem.com";
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
} 
