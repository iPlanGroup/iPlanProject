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
		
		//����socket�ӿڻ�ȡָ��������Ϣ
		Socket socket=new Socket() ;   // 
		InetSocketAddress isa=new InetSocketAddress(HostIP,8085);	
		socket.connect(isa,5000);
		System.out.println("-------------------------------");
		
		//�����������������
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		//������±���
		String outdata="<#refreshcontent#>"+tille;
		out.writeUTF(outdata);
		
		//��ȡ���ص�����
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
	
	//�����ṩ��ȡ����
	public String[]  content() {
		return contentlist;
	}
	
/*	
//ת��Ϊ�ļ��洢����	
	public void filetranslater(String title,String content ,String mtabId) {
		
		  String  rootdirName="cquptscie/";
		  String dirName=mtabId+"//";
		  String downloadpath="/sdcard/"+rootdirName+dirName+title+".txt";
		  
		  getFileFromBytes(content,downloadpath);
	}
	
// String ת��Ϊ�ļ�	
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
