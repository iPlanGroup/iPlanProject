package cn.sin.news;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class refreshcontent implements HostAddr{
	
	private String[] contentlist=new String[20];
	
	
	public  String[] refreshcontent(String tille) throws UnknownHostException, IOException {
		
		contentlist[0]="error";
		System.out.println("refreshcontent.refreshcontent()>>>"+tille);
		
		//建立socket接口获取指定文章信息
		Socket socket=new Socket() ;   // 
		InetSocketAddress isa=new InetSocketAddress(HostIP,8085);	
		socket.connect(isa,5000);
		System.out.println("-------------------------------");
		
		//定义输出流、输入流
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		//输出文章标题
		String outdata="<#refreshcontent#>"+tille;
		out.writeUTF(outdata);
		
		//获取返回的数据
		while(true)
		{
			for (int j = 0; j < contentlist.length; j++) {
				String contentString=in.readUTF();
				if (contentString.equals("end")  ) 
				{
					return contentlist;
				}
				contentlist[j]=contentString;
				System.out.println("conetnt>>>>"+contentlist[0]);
			}
		}
	}
	
	//对外提供获取方法
	public String[]  content() {
		return contentlist;
	}
	
/*	
//转化为文件存储本地	
	public void filetranslater(String title,String content ,String mtabId) {
		
		  String  rootdirName="cquptscie/";
		  String dirName=mtabId+"//";
		  String downloadpath="/sdcard/"+rootdirName+dirName+title+".txt";
		  
		  getFileFromBytes(content,downloadpath);
	}
	
// String 转化为文件	
	private static File getFileFromBytes(String content,String path) {
		
	     byte[] b=content.getBytes(); 
	     BufferedOutputStream stream = null; 
	     File file = null; 
	     
	     try { 
	         file = new File(path); 
	         FileOutputStream fstream = new FileOutputStream(file); 
	         stream = new BufferedOutputStream(fstream); 
	         stream.write(b); 
	         stream.flush();
	     } catch (Exception e) { 
	         e.printStackTrace(); 
	     } finally { 
	    	 
	         if (stream != null) { 
	             try { 
	                 stream.close(); 
	             } catch (IOException e1) { 
	                 e1.printStackTrace(); 
	             } 
	         }
	         
	     } 
	     
	     return file; 
	}

  */
}
