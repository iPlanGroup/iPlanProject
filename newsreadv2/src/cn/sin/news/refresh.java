package cn.sin.news;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class refresh implements HostAddr{
	
	private String[] titlelist=new String[20];
	
	public  String[] refresh(String mtabId) throws UnknownHostException, IOException 
	{
		
		titlelist[0]="error";
		
		//建立socket获取题目列表
		Socket socket=new Socket() ;     
		InetSocketAddress isa=new InetSocketAddress(HostIP,8085);	
		socket.connect(isa,5000);
		System.out.println("-------------------------------");
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		//输出要获取的信息类别
		String outdata="<#refreshtitle#>"+mtabId;
		out.writeUTF(outdata);
		
			while(true)
			{
				for (int j = 0; j < titlelist.length; j++) {
					String titleString=in.readUTF();
					if (titleString.equals("end")  ) 
					{
						return titlelist;
					}
					titlelist[j]=titleString;
				}
			}
	}
	
	public String[]  title() {
		return titlelist;
	}
   
}
