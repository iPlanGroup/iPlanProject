package cn.sin.news;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class details extends Activity  implements HostAddr {
	//�ؼ�����
	private TextView tv1,tittletv;
	private ImageView showpic;

	 //����������  
    int maxSize=0;
	int nowSize=0;
    private static final int LOADING=1;
    private static final int END=2;
	
	//�ļ�·������
	private String sdpath= Environment.getExternalStorageDirectory().getPath()+"//"; 
	private String  rootdirName="cquptscie/";
	private String  catedirName="";
	//����������
	private String content = "";
	private String title = "";
	private String identinum="";
    private Bitmap bitmap;
	private String imgHttp1="http://+HostIP"+":8080"+"+/MM11.jpg";
	
    //�������
	private Handler handler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.details);
		 
		 //��ÿؼ�
		 showpic=(ImageView)this.findViewById(R.id.detailspic);
		 tv1=(TextView)this.findViewById(R.id.tv1);
		 tittletv=(TextView)this.findViewById(R.id.tittletv);
		    
		 //��ȡ�ϸ�Activity����������
		Intent intent=this.getIntent();
		catedirName=intent.getStringExtra("category");
	    title=intent.getStringExtra("title");
	    identinum=intent.getStringExtra("identi");
	    
       System.out.println("details.onCreate()..."+title);
	    
	   String checkpathString=sdpath+rootdirName+catedirName+"/"+ title+".txt";
	   
	    //�����ļ��Ƿ��Ѿ�����
     	 File checkfile = new File(checkpathString); 
		    if (checkfile.exists()) 
		    {
				try {
					content=checktext(title);
				} catch (IOException e) {
					e.printStackTrace();
				}   
			}
		    else 
			{
				content= getcontent(identinum);
			}
		    
		    //��ʵ���������
		    tittletv.setText(title);
	        tv1.setText(content);
	        
	    	imgHttp1="http://"+HostIP+":8080/news/"+identinum+".jpg";
	//        imgHttp1="http://i3.vanclimg.com/users/18/20120929/focus_fbx_120929.jpg";
			//1�����ͼƬ�ͽ�����
			if(null!=bitmap)
			{
			nowSize=0;
			}
			System.out.println("downstart...");

			//2����ʼ����
		    new MyThread(imgHttp1).start();
		    
		    
	        //ͼƬ����¼�
            showpic.setOnClickListener(new View.OnClickListener() 
            {
				@Override
				public void onClick(View v)
				{
					
				}
			
		});
            
          //�������  
            handler=new Handler(){
    			@Override
    		public void handleMessage(Message msg) {
    					
    					 //��ǰ�Ѿ����ص�ֵ
    					  nowSize+=msg.getData().getInt("loadingSize");
    					  //���ý������ĵ�ǰ����ֵ
    				
    						if(msg.what==LOADING){
    						//��ʾ�Ѿ����ص�ֵ
    						Log.e("Download_Progressbar", "�������أ�"+nowSize);
    						}
    						if(msg.what==END){
    						//���������ʾͼƬ
    							if (bitmap==null) {
    								showpic.setVisibility(View.GONE);
								}else {
									showpic.setImageBitmap(bitmap);
								}
    					
    				/*		
    						//ͼƬ����·��
    						String picname=title+".jpg";
    					   	String picdirName="picdir/";
    				    	String savepth=sdpath+rootdirName+picdirName+ picname;
    				    	System.out.println("savepath..."+savepth);
    				    	
    						//��ͼƬ���浽sdcard��
    						saveImg(savepth,bitmap);
    					*/	
    						//������ǰ�߳�
    						Thread.currentThread().interrupt();
    						
    						}
    				}
    	};
           
	}
	
	//����ͼƬ����
		public void saveImg(String fullPath,Bitmap bitmap){
			File file=new File(fullPath);
			if(file.exists())
			{
				file.delete();
			}
			
			try {
				FileOutputStream fos=new FileOutputStream(file);
				boolean isSaved=bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
				if(isSaved)
				{
					fos.flush();
					fos.close();
				}
				} catch (Exception e) {
					errordeale();
				}
	}          
	
//ͼƬ���ؽ���	
	private class MyThread extends Thread{
		   String httpImg;
	       public MyThread(String httpImg){
	       this.httpImg=httpImg;
	       }
	       
		@Override
		public void run() {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			//���ӻ�ȡͼƬ
			URL url=new URL(httpImg);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setDoInput(true);
			con.connect();
			//��ȡ������
			InputStream is=con.getInputStream();
			//��ȡ�ļ��Ĵ�С
			maxSize=con.getContentLength();
			byte []buffer=new byte[1024*3];
			int len=-1;
			while((len=is.read(buffer))!=-1){
				
			    //��������
				bos.write(buffer,0,len);
				bos.flush();
				
				//������Ϣ�����߳�
				Message msg=new Message();
				msg.what=LOADING;
				Bundle bundle=new Bundle();
				bundle.putInt("loadingSize", len);
				msg.setData(bundle);
				Thread.sleep(10);
				handler.sendMessage(msg);
				
			}
			
			//�ر�������������
			is.close();
			con.disconnect();
			
			//��ȡͼƬ�ֽ�����ת����ͼƬ
			byte []imgBytes=bos.toByteArray();
			bitmap=BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
	
			//���ؽ���������Ϣ
			Message msg=new Message();
			msg.what=END;
			handler.sendMessage(msg);  
		
		} catch (MalformedURLException e) {  
			showpic.setVisibility(View.GONE);
			
		} catch (IOException e) {
		//	showpic.setVisibility(View.GONE);
		} catch (InterruptedException e) {
			errordeale();
		}
		
		}
}
    
//��������Ƿ����	
    private String checktext(String title) throws IOException
    {
    	 String titlename=title+".txt";
    	 String checkpathString=sdpath+rootdirName+catedirName+"/"+ titlename;
    	 File file = new File(checkpathString); 
    	 
	     FileInputStream fis = new FileInputStream(file); 
	     
	     int bufferSize = fis.available();
		 byte[] buffer = new byte[bufferSize];
		 int length = -1;
		     while ((length = fis.read(buffer)) != -1) 
		     {
		    	 //���ֽ�����utf-8��ʽ����
				 content = new String(buffer,"utf-8" );
			 }
    	return content;
    	
    }
    
//��������ȡ��������
    private String getcontent(String title) {
		try {
			
			refreshcontent renewcontent=new refreshcontent();
			String[] contentStrings=renewcontent.refreshcontent(title);
			content=contentStrings[0];
			
			} catch (FileNotFoundException e) {
					errordeale();
			} catch (UnsupportedEncodingException e) {
					errordeale();
			} catch (IOException e) {
					errordeale();
			}
		
			return content;
	}
    
    /*
//���ͼƬ�Ƿ����
    public boolean checkpic(String picName)
    {
    	String picname=picName+".jpg";
    	String picdirName="picdir/";
    	String  checkpathString=sdpath+rootdirName+picdirName+ picname;
    	System.out.println("details.getpic()"+"enter>>>>>");
    	file3 = new File(checkpathString);
    	if (file3.exists()) {
    		 options = new BitmapFactory.Options();
     		 options.inSampleSize = 1;
     	    	bm = BitmapFactory.decodeFile(checkpathString, options); 
     	    	showpic.setImageBitmap(bm);
     	    	progressBar.setVisibility(View.INVISIBLE);
     		return true;
		}
 	  return false;
    	
    }
    
   */

    
  //�쳣���� 
    public void errordeale() {
    	    ProgressDialog dialog2=new ProgressDialog(details.this);
			dialog2.setTitle("���緱æ");
			dialog2.setMessage("��������");
			dialog2.setButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog2, int which) {
				}
			});
			
            dialog2.setButton2("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog2, int which) {
					dialog2.cancel(); 
				}
			});
            
			dialog2.show();
	}   

}
