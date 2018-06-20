package nCountServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	public Stopwatch st;
	public ServerSocket ssock;
	
	public Server(Stopwatch st) throws IOException
	{
		this.st = st;
		Chocolat.println("[" + st.elapsedTime() + "] Server Listener initialized.");
		ssock = new ServerSocket(12253);
	}
	
	public void run() throws IOException
	{
	    Chocolat.println("[" + st.elapsedTime() + "] Server Listening...");
	    while (true) 
	    {
	    	Socket sock = ssock.accept();
	    	Chocolat.println("[" + st.elapsedTime() + "] Server Connected.");
	        new Thread(new ServerThread(sock, st)).start();
	    }
	}
	
}
class ServerThread implements Runnable
{
	Socket sock;
	Stopwatch st;
	BufferedWriter bw;
	
	public ServerThread(Socket sock, Stopwatch st)
	{
		this.sock = sock;
		this.st = st;
	}

	@Override
	public void run() 
	{
		try
		{
			String receiveMessage;
			bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			InputStream istream = sock.getInputStream();
			BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
			// for(;;)
			// {
				try
				{	
					String s = "HTTP/1.1 200 OK\r\n";
				    s += "Content-Type: text/html\r\n\r\n";
					if((receiveMessage = receiveRead.readLine()) != null)
					{
						Chocolat.println(receiveMessage);
						if (receiveMessage.contains("GET /index.html"))
						{
							s += "\n" + 
									"<!DOCTYPE HTML>\n" + 
									"<html>\n" + 
									"<head>\n" + 
									"	<meta charset='utf-8'>\n" + 
									"	<title>nCount webUI</title> \n" + 
									"    	<meta name=\"theme-color\" content=\"#00549e\">\n" + 
									"	<link rel=\"top\" title=\"nCount\" href=\"/\">			\n" + 
									"	<style type=\"text/css\">\n" + 
									"		body,div,h1,h2,h3,h4,h5,h6,p,ul,li,dd,dt {\n" + 
									"			font-family:verdana,sans-serif;\n" + 
									"			color:white;\n" + 
									"			margin:0;\n" + 
									"			padding:0;\n" + 
									"			background:none;\n" + 
									"		}\n" + 
									"\n" + 
									"		body {\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-color:#012e57;\n" + 
									"		}\n" + 
									"\n" + 
									"		div#content2 {\n" + 
									"			text-align: center;\n" + 
									"			position:absolute;\n" + 
									"			top:28em;\n" + 
									"			left:0;\n" + 
									"			right:0;\n" + 
									"		}\n" + 
									"\n" + 
									"		.mascotbox {\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			margin-left: auto;\n" + 
									"			margin-right: auto;\n" + 
									"			margin-top:10px;\n" + 
									"			margin-bottom:10px;\n" + 
									"			padding:2px 0px;\n" + 
									"			width:480px;\n" + 
									"			border-radius: 5px;\n" + 
									"			box-shadow: 0px 0px 5px #000;\n" + 
									"			text-shadow:0px 0px 2px black, 0px 0px 6px black;\n" + 
									"		}\n" + 
									"\n" + 
									"		#searchbox { padding-bottom:5px; }\n" + 
									"		#searchbox3 { font-size: 80%; }\n" + 
									"		#searchbox4 { font-size: 60%; }\n" + 
									"	</style>\n" + 
									"</head>\n" + 
									"<body>\n" + 
									"	<div id=\"notices\">\n" + 
									"		\n" + 
									"			<div id=\"notice\" style=\"display:none;\">\n" + 
									"				<div class=\"closebutton\" onclick=\"noticeClose(this.parentNode);\">X</div>\n" + 
									"				<p></p>\n" + 
									"			</div>\n" + 
									"		\n" + 
									"\n" + 
									"		\n" + 
									"			<div id=\"warning\" style=\"display: none;\"></div>\n" + 
									"		\n" + 
									"\n" + 
									"		\n" + 
									"			<div id=\"error\" style=\"display:none;\">\n" + 
									"				<div class=\"closebutton\" onclick=\"noticeClose(this.parentNode);\">X</div>\n" + 
									"				<p></p>\n" + 
									"			</div>\n" + 
									"		\n" + 
									"	</div>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<div id=\"searchbox\" class='mascotbox'>\n" + 
									"	<div id=\"static-index\">\n" + 
									"		<center>\n" + 
									"		<h1 style=\"font-size: 3em;\">nCount Console</h1>\n" + 
									"		<div>\n" + 
									"			<form action=\"/post\" method=\"get\">\n" + 
									"			<div>\n" + 
									"				<input autofocus=\"autofocus\" id=\"tags\" name=\"tags\" size=\"30\" type=\"text\" value=\"\"><br>\n" + 
									"				<input type=\"submit\" value=\"Look up sensor ID...\" >\n" + 
									"			</div>\n" + 
									"			</form>\n" + 
									"		</div>\n" + 
									"		</center>\n" + 
									"	</div>\n" + 
									"	<div id='mascot_artist'></div>\n" + 
									"</div>\n" + 
									"\n" + 
									"<div id='searchbox2' class='mascotbox'>\n" + 
									"		<center>\n" + 
									"		<p>Total attendance count (" +
									DataStore.getNumSensors() +
									" sensors):</p>\n" + 
									"		<br>\n" + 
									//"		<img alt=\"0\" src=\"altcounters/0.gif\">\n" + 
									//"		<img alt=\"0\" src=\"altcounters/0.gif\">\n" + 
									//"		<img alt=\"0\" src=\"altcounters/0.gif\">\n" + 
									//"		<img alt=\"0\" src=\"altcounters/0.gif\">\n" + 
									//"		<img alt=\"2\" src=\"altcounters/2.gif\">\n" + 
									//"		<img alt=\"1\" src=\"altcounters/1.gif\">\n" + 
									//"		<img alt=\"4\" src=\"altcounters/4.gif\">\n" + 
									DataStore.mainCounter +
									"		</center>\n" + 
									"	\n" + 
									"</div>\n" + 
									"\n" + 
									"<div id='searchbox3' class='mascotbox'>\n" + 
									"	<center>\n" + 
									"	<p>\n" + 
									DataStore.getNumSensors() + 
									"		sensors online<br />\n" + 
									"		version: " +
									DataStore.VERSION_ID +
									"<br />\n" + 
									"		<a href=\"/about.html\" title=\"About nCount\">About</a>\n" + 
									"	</p>\n" + 
									"	</center>\n" + 
									"</div>\n" + 
									"\n" + 
									"<div id='searchbox4' class='mascotbox'>\n" + 
									"	<center>\n" + 
									"	<p>\n" + 
									"	Device List\n" + 
									"	<br>\n" + 
									"	ID    |    MAC\n" + 
									"	<br>\n" + 
									"	--------------\n" + 
									"	<br>\n" + 
									"	1     |   xxxx\n" + 
									"	<br>\n" + 
									"	--------------\n" + 
									"	<br>\n" + 
									"	2     |   xxxx\n" + 
									"	<br>\n" + 
									"	--------------\n" + 
									"	<br>\n" + 
									"	3     |   xxxx\n" + 
									"	<br>\n" + 
									"\n" + 
									"</div>\n" + 
									"</div>\n" + 
									"</body>\n" + 
									"</html>\n";
							// break;
						}
						else if (receiveMessage.contains("GET /about.html"))
						{
							s += "\n" + 
									"<!DOCTYPE HTML>\n" + 
									"<html>\n" + 
									"<head>\n" + 
									"	<meta charset='utf-8'>\n" + 
									"	<title>nCount webUI</title> \n" + 
									"    	<meta name=\"theme-color\" content=\"#00549e\">\n" + 
									"	<link rel=\"top\" title=\"nCount\" href=\"/\">			\n" + 
									"	<style type=\"text/css\">\n" + 
									"		body,div,h1,h2,h3,h4,h5,h6,p,ul,li,dd,dt {\n" + 
									"			font-family:verdana,sans-serif;\n" + 
									"			color:white;\n" + 
									"			margin:0;\n" + 
									"			padding:0;\n" + 
									"			background:none;\n" + 
									"		}\n" + 
									"\n" + 
									"		body {\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-color:#012e57;\n" + 
									"		}\n" + 
									"\n" + 
									"		div#content2 {\n" + 
									"			text-align: center;\n" + 
									"			position:absolute;\n" + 
									"			top:28em;\n" + 
									"			left:0;\n" + 
									"			right:0;\n" + 
									"		}\n" + 
									"\n" + 
									"		.mascotbox {\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			margin-left: auto;\n" + 
									"			margin-right: auto;\n" + 
									"			margin-top:10px;\n" + 
									"			margin-bottom:10px;\n" + 
									"			padding:2px 0px;\n" + 
									"			width:480px;\n" + 
									"			border-radius: 5px;\n" + 
									"			box-shadow: 0px 0px 5px #000;\n" + 
									"			text-shadow:0px 0px 2px black, 0px 0px 6px black;\n" + 
									"		}\n" + 
									"\n" + 
									"		#searchbox { padding-bottom:5px; }\n" + 
									"		#searchbox3 { font-size: 80%; }\n" + 
									"		#searchbox4 { font-size: 60%; }\n" + 
									"	</style>\n" + 
									"</head>\n" + 
									"<body>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<div id='searchbox3' class='mascotbox'>\n" + 
									"	<center>\n" + 
									"	<p>\n" + 
									"	nCount Web UI version " +
									DataStore.VERSION_ID +
									"	</p>\n<br>" + 
									"   <p>Server uptime:  \n" +
									st.elapsedTime() +
									"s  </p> \n" +
									"   <br> \n" +
									"   <p>\n" +
									"   By: Victor Du" +
									"   </p>\n"+
									"	</center>\n" + 
									"</div>\n" + 
									"\n" + 
									"</div>\n" + 
									"</body>\n" + 
									"</html>\n";
						}
						else
						{
							s += "\n" + 
									"<!DOCTYPE HTML>\n" + 
									"<html>\n" + 
									"<head>\n" + 
									"	<meta charset='utf-8'>\n" + 
									"	<title>nCount webUI</title> \n" + 
									"    	<meta name=\"theme-color\" content=\"#00549e\">\n" + 
									"	<link rel=\"top\" title=\"nCount\" href=\"/\">			\n" + 
									"	<style type=\"text/css\">\n" + 
									"		body,div,h1,h2,h3,h4,h5,h6,p,ul,li,dd,dt {\n" + 
									"			font-family:verdana,sans-serif;\n" + 
									"			color:white;\n" + 
									"			margin:0;\n" + 
									"			padding:0;\n" + 
									"			background:none;\n" + 
									"		}\n" + 
									"\n" + 
									"		body {\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-color:#012e57;\n" + 
									"		}\n" + 
									"\n" + 
									"		div#content2 {\n" + 
									"			text-align: center;\n" + 
									"			position:absolute;\n" + 
									"			top:28em;\n" + 
									"			left:0;\n" + 
									"			right:0;\n" + 
									"		}\n" + 
									"\n" + 
									"		.mascotbox {\n" + 
									"			background-repeat:no-repeat;\n" + 
									"			background-attachment:fixed;\n" + 
									"			background-position:50% 0%;\n" + 
									"			margin-left: auto;\n" + 
									"			margin-right: auto;\n" + 
									"			margin-top:10px;\n" + 
									"			margin-bottom:10px;\n" + 
									"			padding:2px 0px;\n" + 
									"			width:480px;\n" + 
									"			border-radius: 5px;\n" + 
									"			box-shadow: 0px 0px 5px #000;\n" + 
									"			text-shadow:0px 0px 2px black, 0px 0px 6px black;\n" + 
									"		}\n" + 
									"\n" + 
									"		#searchbox { padding-bottom:5px; }\n" + 
									"		#searchbox3 { font-size: 80%; }\n" + 
									"		#searchbox4 { font-size: 60%; }\n" + 
									"	</style>\n" + 
									"</head>\n" + 
									"<body>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<br>\n" + 
									"<div id='searchbox3' class='mascotbox'>\n" + 
									"	<center>\n" + 
									"	<p>\n" + 
									"	ERROR 404: Page Not Found" +
									"	</p>\n" + 
									"	</center>\n" + 
									"</div>\n" + 
									"\n" + 
									"</div>\n" + 
									"</body>\n" + 
									"</html>\n";
						}
						bw.write(s);
						bw.close();
					}
				}
				catch (Exception e)
				{
					Chocolat.println("[" + st.elapsedTime() + "] ServerThread was interrupted: " + e);
					e.printStackTrace();
					// break;
				}
			// }
		}
		catch (IOException ioe)
		{
			Chocolat.println("[" + st.elapsedTime() + "] ServerThread failed: " + ioe);
		}
		// TODO Auto-generated method stub
		
	}
}