/*
 *Author:wmm
 *Email:wu.mengemgn@foxmail.com
 *Date:2012-3-24
 */
package cn.sin.news;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainAvtivity extends Activity implements OnItemClickListener,HostAddr  {
	GridView  toolbarGrid;
	/*-- Toolbar�ײ��˵�ѡ���±�--*/
	private final int TOOLBAR_ITEM_PAGEHOME = 0;// ѧԺ����
	private final int TOOLBAR_ITEM_BACK = 1;// У�����
	private final int TOOLBAR_ITEM_FORWARD = 2;// ѧ������
	private final int TOOLBAR_ITEM_NEW = 3;// ѧ���
	private final int TOOLBAR_ITEM_MENU = 4;// У���ؿ�
	/** �ײ��˵�ͼƬ **/
	int[] menu_toolbar_image_array = { 
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable,
			R.drawable.controlbar_window, 
			R.drawable.controlbar_homepage,
			R.drawable.controlbar_showtype_list };
	/** �ײ��˵����� **/
	String[] menu_toolbar_name_array = { "ѧԺ����", "У�����", "ѧ������", "ѧ���", "У���ؿ�" };
	private final static int ITEM0=Menu.FIRST;
  private final static int ITEM1=Menu.FIRST+1;
	
	private ListView list1; //��ʾ�����list�б�����
	private TextView colorshow;//������������ɫ
	boolean state=false;  
	private String  dirName="cquptscie/";  
	private String mtabId="collage";  //������ǩ
	private int color;
	
    	private String[] titlelist={ "Main Title1","Main Title2","Main Title3","Main Title4", };
      public String[] mImageIds = {
				 "http://192.168.0.100:8080/news/a1.jpg", 
				 "http://192.168.0.100:8080/news/a2.jpg", 
				 "http://192.168.0.100:8080/news/a3.jpg", 
				 "http://192.168.0.100:8080/news/a4.jpg", 
				 "http://192.168.0.100:8080/news/a5.jpg", 
				 "http://192.168.0.100:8080/news/a6.jpg",
				 };
    	
    	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
	
		list1=(ListView)this.findViewById(R.id.list1);
		colorshow=(TextView)this.findViewById(R.id.color);
		 
    list1.setOnItemClickListener(this) ; 
         
		// �����ײ��˵� Toolbar
		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		toolbarGrid.setBackgroundResource(R.drawable.channelgallery_bg);// ���ñ���
		toolbarGrid.setNumColumns(5);// ����ÿ������
		
		toolbarGrid.setGravity(Gravity.CENTER);// λ�þ���
		toolbarGrid.setVerticalSpacing(10);// ��ֱ���
		toolbarGrid.setHorizontalSpacing(10);// ˮƽ���
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,menu_toolbar_image_array));// ���ò˵�Adapter
		
		//����������Ϣ
		refresh updataRefreshe=new refresh();
		try {
			
			updataRefreshe.refresh(mtabId); 
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		//��ȡ���±���
		titlelist=updataRefreshe.title();
		reload(mtabId,titlelist);

		/** �����ײ��˵�ѡ�� **/
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(MainAvtivity.this,menu_toolbar_name_array[arg2], 
					Toast.LENGTH_SHORT).show();
	
				switch (arg2) {
				
					case TOOLBAR_ITEM_PAGEHOME:
						mtabId="collage";
					  Color=Color.rgb(0, 102, 204);
						break;
						
					case TOOLBAR_ITEM_BACK:
						mtabId="enterprice";	
						color=Color.rgb(255, 0, 0);
						break;
						
					case TOOLBAR_ITEM_FORWARD:;
						mtabId="communication";
						color=Color.rgb(0, 153, 0);
						break;
						
					case TOOLBAR_ITEM_NEW:
						mtabId="student";
						color=Color.rgb(255, 102, 0);
						break;
					
					case TOOLBAR_ITEM_MENU:
						mtabId="schoolmate";
						color=Color.rgb(204, 0, 153);
						break;
				}
		
				updatenews(mtabId,color);
				
			}
		}
		);
		
	}
  //ͨ����ǩ��ȡ��ͬ����ĵ�����
	public void  updatenews(String mtabId , int color )
	{
		refresh updataRefresh=new refresh();
		
		//���ö�����ɫ
	    colorshow.setBackgroundColor(color);
	    
		try {
			
			updataRefresh.refresh(mtabId);
			
			} catch (UnknownHostException e) {
				errordeale(); 
			} catch (IOException e) {
				errordeale() ;
			}
		//��ȡ�����б�
		titlelist=updataRefresh.title();
		
		//��ӡ��ȡ�ı��⵽��̨
//			for (int i = 0; i < titlelist.length; i++)
//			{
//				System.out.println("pp"+titlelist[i]);
//			}
	
		reload(mtabId,titlelist);	//������Ϣ
		
	}
	
	
	
	public boolean reload(String tabId,String[] title) {
		List<ImageAndText> dataArray = new ArrayList<ImageAndText>();
		String[] titlename=new String[10];
    	String[] identinum=new String[10];
    	
    //����ı����ʽΪ��������+ʶ���������������	
    	int i=0;
    	while(title[i]!="error" && title[i]!=null) 
    	{
    		
    		 int index=title[i].indexOf("&&");
      	     titlename[i]=title[i].substring(0,index);
      	     identinum[i]=title[i].substring(index+2);
//      	   System.out.println("titlename>>>" +titlename[i]);
//      	   System.out.println("identinum>>>>"+ identinum[i]);
      	   ImageAndText test=new ImageAndText(mImageIds[i], titlename[i],identinum[i]);
      	   dataArray.add(test);
      	   i++;
 
	        ImageAndTextListAdapter adapter=new ImageAndTextListAdapter(this, dataArray, list1);
	        list1.setAdapter(adapter);
	        
		}
		  
      return true;
     
	}
	
	//listview�ĵ�������¼�
    public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened 
            View v,//The view within the AdapterView that was clicked
            int position,//The position of the view in the adapter
            long id//The row id of the item that was clicked
            ) {
			//�ڱ�����arg2=arg3
		    //HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(position);
			//��ʾ��ѡItem��ItemText
    	
    		//��ȡlist�����е�����ͼƬ����
    	    ImageAndText item=(ImageAndText)arg0.getItemAtPosition(position);
    	    
    	    //��ȡ���ֺ�ʶ���
			String titlename=(String)item.getText();     //��ȡ����
			String identinum=(String)item.getidentinum(); 
			
			//	System.out.println("NewsreadActivity"+titlename);
			
			//������ת��ͼ
			Intent intent =new Intent();
			
			intent.setClass(MainAvtivity.this, details.class);
			intent.putExtra("category", mtabId);
			intent.putExtra("title", titlename);
			intent.putExtra("identi", identinum);
			
			startActivity(intent); 
			
			}	
    
    
    
//���쳣���д����������û������쳣��ԭ��
    public void errordeale() {
    	ProgressDialog dialog2=new ProgressDialog(MainAvtivity.this);
			dialog2.setTitle("�������");
			dialog2.setMessage("��������");
			dialog2.setButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog2, int which) {
					
				}
			});
          dialog2.setButton2("�˳�", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog2, int which) {
					finish();
				}
			});
			dialog2.show();
	}
	
	

	/**
	 * ����ײ��˵�Adapter
	 * 
	 * @param menuNameArray
	 *            ����
	 * @param imageResourceArray
	 *            ͼƬ
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	

	//��ʼ������ʽ�˵�
		public boolean onCreateOptionsMenu(Menu menu) {
			//��һ����������ID ���ڶ����������˵���ID��������������˳��š����ĸ��������˵�������ʾ������
			menu.add(0,ITEM0,0,"����");
			menu.add(0,ITEM1,0,"�˳�");
			return true;
		}

	//��������ʽ�˵��ĵ���¼�
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case ITEM0:
					try {
	
						refresh updataRefresh=new refresh();
						updataRefresh.refresh(mtabId);
						titlelist=updataRefresh.title();
						reload(mtabId,titlelist);
						
						} catch (UnknownHostException e) {
							errordeale();
						} catch (IOException e) {
							errordeale();
						}
					break;
					
				case ITEM1:
					//�˳�
					finish();
					break;
				
			}
			return true;
		}
		

    	//���߻�ȡ�ļ�
    	public SimpleAdapter getfiles(String tabId) {
    		//��ȡ·��  
			String key="txt";
			String sdpath= Environment.getExternalStorageDirectory().getPath()+"//"; 
			String rootpath=sdpath+dirName;
			  //�жϲ�����Ŀ¼
		        File rootdirfile = new File(rootpath); 
		        if(!rootdirfile.isDirectory())
		        {
		        	rootdirfile.mkdirs();
		        }
		        
		        File picdirfile = new File(rootpath+"picdir/"); 
		        if(!picdirfile.isDirectory())
		        {
		        	picdirfile.mkdirs();
		        }
		        
		    String pathString=sdpath+dirName+tabId+"//"; 
		    
		    File cagodirfile = new File(pathString);
		    
		    if(!cagodirfile.isDirectory())
	        {
		    	cagodirfile.mkdirs();
	        }
		        
			ArrayList<HashMap<String, Object>> filelist =searchFile(key, pathString);
		    SimpleAdapter listadapter = new SimpleAdapter(this, 
					 filelist,//������Դ 
                     R.layout.night_item,//night_item��XMLʵ��
                     //��̬������ImageItem��Ӧ������        
                     new String[] {"ItemImage","ItemText"}, 
                     //ImageItem��XML�ļ������һ��ImageView,����TextView ID
                     new int[] {R.id.ItemImage,R.id.ItemText});
					//���Ӳ�����ʾ
			 return listadapter;
		}
    	
      /*
         * searchFile �����ļ������뵽ArrayList ����ȥ
         *  @String keyword ���ҵĹؼ���
         *  @File filepath  ���ҵ�Ŀ¼
         * 
         * */
        private ArrayList<HashMap<String, Object>> searchFile(String keyword,String filepath)
        { 
        	HashMap<String, Object>  rowItem = null;
        	File newfile = new File(filepath);
        	int index = 0;
        	 ArrayList<HashMap<String, Object>> fileList = new ArrayList<HashMap<String, Object>>();
           //�ж�SD���Ƿ����
             if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED))
             {
              File[] files = newfile.listFiles();
                        for (File file : files)
                        {
                           //�ж����ļ���������ļ����ж�
                                    try {   
		                                     if (file.getName().indexOf(keyword) > -1||file.getName().indexOf(keyword.toUpperCase()) > -1) 
		                                     {   
			                                    	
			                                     		   rowItem = new HashMap<String, Object>();
			                                                 rowItem.put("ItemImage", mImageIds[index]);    // �������к�
			                                                 rowItem.put("ItemText", file.getName().substring(0, file.getName().length()-4));// ��������
			                                
			                                                 fileList.add(rowItem);
			                                                 index++;
		                                     }   
                                    	} catch(Exception e) {   
                                           Toast.makeText(this,"���ҷ�������", Toast.LENGTH_SHORT).show();   
                                    	}   
                           
                        }
               
            }
			return fileList;
        }
        
		
}


