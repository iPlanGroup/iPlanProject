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
	//控件声明
	private TextView tv1,tittletv;
	private ImageView showpic;

	 //进度条声明  
    int maxSize=0;
	int nowSize=0;
    private static final int LOADING=1;
    private static final int END=2;
	
	//文件路径声明
	private String sdpath= Environment.getExternalStorageDirectory().getPath()+"//"; 
	private String  rootdirName="cquptscie/";
	private String  catedirName="";
	//变量名声明
	private String content = "";
	private String title = "";
	private String identinum="";
    private Bitmap bitmap;
	private String imgHttp1="http://+HostIP"+":8080"+"+/MM11.jpg";
	
    //组件声明
	private Handler handler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.details);
		 
		 //获得控件
		 showpic=(ImageView)this.findViewById(R.id.detailspic);
		 tv1=(TextView)this.findViewById(R.id.tv1);
		 tittletv=(TextView)this.findViewById(R.id.tittletv);
		    
		 //获取上个Activity传来的数据
		Intent intent=this.getIntent();
		catedirName=intent.getStringExtra("category");
	    title=intent.getStringExtra("title");
	    identinum=intent.getStringExtra("identi");
	    
       System.out.println("details.onCreate()..."+title);
	    
	   String checkpathString=sdpath+rootdirName+catedirName+"/"+ title+".txt";
	   
	    //检查该文件是否已经存在
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
		    
		    //现实标题和内容
		    tittletv.setText(title);
	        tv1.setText(content);
	        
	    	imgHttp1="http://"+HostIP+":8080/news/"+identinum+".jpg";
	//        imgHttp1="http://i3.vanclimg.com/users/18/20120929/focus_fbx_120929.jpg";
			//1、清空图片和进度条
			if(null!=bitmap)
			{
			nowSize=0;
			}
			System.out.println("downstart...");

			//2、开始下载
		    new MyThread(imgHttp1).start();
		    
		    
	        //图片点击事件
            showpic.setOnClickListener(new View.OnClickListener() 
            {
				@Override
				public void onClick(View v)
				{
					
				}
			
		});
            
          //句柄处理  
            handler=new Handler(){
    			@Override
    		public void handleMessage(Message msg) {
    					
    					 //当前已经下载的值
    					  nowSize+=msg.getData().getInt("loadingSize");
    					  //设置进度条的当前进度值
    				
    						if(msg.what==LOADING){
    						//显示已经下载的值
    						Log.e("Download_Progressbar", "正在下载："+nowSize);
    						}
    						if(msg.what==END){
    						//下载完成显示图片
    							if (bitmap==null) {
    								showpic.setVisibility(View.GONE);
								}else {
									showpic.setImageBitmap(bitmap);
								}
    					
    				/*		
    						//图片保存路径
    						String picname=title+".jpg";
    					   	String picdirName="picdir/";
    				    	String savepth=sdpath+rootdirName+picdirName+ picname;
    				    	System.out.println("savepath..."+savepth);
    				    	
    						//将图片保存到sdcard中
    						saveImg(savepth,bitmap);
    					*/	
    						//结束当前线程
    						Thread.currentThread().interrupt();
    						
    						}
    				}
    	};
           
	}
	
	//保存图片方法
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
	
//图片下载进程	
	private class MyThread extends Thread{
		   String httpImg;
	       public MyThread(String httpImg){
	       this.httpImg=httpImg;
	       }
	       
		@Override
		public void run() {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			//连接获取图片
			URL url=new URL(httpImg);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setDoInput(true);
			con.connect();
			//获取输入流
			InputStream is=con.getInputStream();
			//获取文件的大小
			maxSize=con.getContentLength();
			byte []buffer=new byte[1024*3];
			int len=-1;
			while((len=is.read(buffer))!=-1){
				
			    //读入数据
				bos.write(buffer,0,len);
				bos.flush();
				
				//发送消息给主线程
				Message msg=new Message();
				msg.what=LOADING;
				Bundle bundle=new Bundle();
				bundle.putInt("loadingSize", len);
				msg.setData(bundle);
				Thread.sleep(10);
				handler.sendMessage(msg);
				
			}
			
			//关闭输入流和连接
			is.close();
			con.disconnect();
			
			//获取图片字节流并转化成图片
			byte []imgBytes=bos.toByteArray();
			bitmap=BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
	
			//下载结束后发送消息
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
    
//检查文章是否存在	
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
		    	 //将字节流以utf-8形式解码
				 content = new String(buffer,"utf-8" );
			 }
    	return content;
    	
    }
    
//服务器获取文章内容
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
//检查图片是否存在
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

    
  //异常处理 
    public void errordeale() {
    	    ProgressDialog dialog2=new ProgressDialog(details.this);
			dialog2.setTitle("网络繁忙");
			dialog2.setMessage("请检查网络");
			dialog2.setButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog2, int which) {
				}
			});
			
            dialog2.setButton2("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog2, int which) {
					dialog2.cancel(); 
				}
			});
            
			dialog2.show();
	}   

}
