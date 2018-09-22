package com.sy.server.app.controller;
//问题： t_sy_people_information 表字段，密码类型错误，图片地址长度错误
//问题：对于可为空的值，如name 该如何搜索？like吗？	
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sy.common.annotation.CommonDisk;
import com.sy.common.annotation.HttpClient;
import com.sy.common.annotation.MapUtil;
import com.sy.common.annotation.SnCal;
import com.sy.common.bean.SubSyxsjk;
import com.sy.common.bean.Subxxssjk;
import com.sy.common.bean.Syxsjk;
import com.sy.common.bean.Xxssjk;
import com.sy.common.busibean.SyItemInformation;
import com.sy.common.busibean.SyPeopleInformation;
import com.sy.server.app.service.SyItemService;
import com.sy.server.app.service.SyPeopleService;

/**
 * 失物搜索信息  控制层
 * 当使用@RestController时使用返回对象
 * 当使用@Controller时返回视图jsp
 * @author YUMENG
 *
 */
@RestController
@RequestMapping("/Search")
public class SearchRestController {
	
	private  Logger   log  = LoggerFactory.getLogger(SearchRestController.class);
	HttpClient httpclient=new HttpClient();
	SnCal sncal=new SnCal();
	
	@Autowired
	private  SyItemService  itemservice;
	
	@Autowired
	private  SyPeopleService  peopleservice;
	

	
	//web api :http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
	
	/*-----------------------------------------------
	 * 
	Place检索示例：

	城市内检索
	http://api.map.baidu.com/place/v2/search?query=银行&page_size=10&page_num=0&scope=1&region=北京&output=json&ak={您的密钥}

	矩形区域检索
	http://api.map.baidu.com/place/v2/search?query=美食&page_size=10&page_num=0&scope=1&bounds=39.915,116.404,39.975,116.414&output=json&ak={您的密钥}

	圆形区域检索
	http://api.map.baidu.com/place/v2/search?query=酒店&page_size=10&page_num=0&scope=1&location=39.915,116.404&radius=2000&output=json&ak={您的密钥}
	 
	//多关键词并集检索
	http://api.map.baidu.com/place/v2/search?query=酒店$银行&scope=2&output=json&location=39.915,116.404&radius=2000&filter=sort_name:distance|sort_rule:1&ak={您的密钥}
	
	===================================================================================================================================================
        一级行业分类 二级行业分类 
        
	美食          中餐厅、外国餐厅、小吃快餐店、蛋糕甜品店、咖啡厅、茶座、酒吧  
	酒店          星级酒店、快捷酒店、公寓式酒店  
	购物          购物中心、超市、便利店、家居建材、家电数码、商铺、集市  
	生活服务   通讯营业厅、邮局、物流公司、售票处、洗衣店、图文快印店、照相馆、房产中介机构、公用事业、维修点、家政服务、殡葬服务、彩票销售点、宠物服务、报刊亭、公共厕所  
	丽人          美容、美发、美甲、美体  
	旅游景点   公园、动物园、植物园、游乐园、博物馆、水族馆、海滨浴场、文物古迹、教堂、风景区  
	休闲娱乐   度假村、农家院、电影院、KTV、剧院、歌舞厅、网吧、游戏场所、洗浴按摩、休闲广场  
	运动健身   体育场馆、极限运动场所、健身中心  
	教育培训   高等院校、中学、小学、幼儿园、成人教育、亲子教育、特殊教育学校、留学中介机构、科研机构、培训机构、图书馆、科技馆  
	文化传媒   新闻出版、广播电视、艺术团体、美术馆、展览馆、文化宫  
	医疗          综合医院、专科医院、诊所、药店、体检机构、疗养院、急救中心、疾控中心  
	汽车服务   汽车销售、汽车维修、汽车美容、汽车配件、汽车租赁、汽车检测场  
	交通设施   飞机场、火车站、地铁站、长途汽车站、公交车站、港口、停车场、加油加气站、服务区、收费站、桥  
	金融          银行、ATM、信用社、投资理财、典当行  
	房地产       写字楼、住宅区、宿舍  
	公司企业    公司、园区、农林园艺、厂矿  
	政府机构  
    
    ====================================================================================================================================================     
    //返回一组包含"天安门"字段的建议词条列表 
     * 
                参数名称 是否必须 默认值 格式 备注 
		q(query)  是  无  上地、天安、中关、shanghai  输入建议关键字（支持拼音）  
		region  是  无  全国、北京市、131、江苏省等  所属城市/区域名称或代号（指定城市返回结果加权，可能返回其他城市高权重结果）  
		city_limit  否  false  "false"or"true"  取值为"true"，仅返回region中指定城市检索结果  
		location  否  无  40.047857537164,116.31353434477  传入location参数后，返回结果将以距离进行排序  
		output  否  xml  json、xml  返回数据格式，可选json、xml两种  
		ak  是  无  E4805d16520de693a3fe707cdc962045  开发者访问密钥，必选。  
		sn  否  无   用户的权限签名  
		timestamp  否  无   设置sn后该值必选  
        http://api.map.baidu.com/place/v2/suggestion?query=天安门&region=131&output=json&ak={您的密钥}  
     ==================================================================================================================================================     
      Geocoding API包括地址解析和逆地址解析功能： 

             地理编码：即地址解析，由详细到街道的结构化地址得到百度经纬度信息，例如：“北京市海淀区中关村南大街27号”地址解析的结果是“lng:116.31985,lat:39.959836”。
             同时，地理编码也支持名胜古迹、标志性建筑名称直接解析返回百度经纬度，例如：“百度大厦”地址解析的结果是“lng:116.30815,lat:40.056885” ，
             通用的POI检索需求，建议使用Place API。 

             逆地理编码：即逆地址解析，由百度经纬度信息得到结构化地址信息，例如：“lat:31.325152,lng:120.558957”逆地址解析的结果是“江苏省苏州市虎丘区塔园路318号”。 
    
      
               参数   是否必须   默认值  格式举例  含义 
		output  否  xml  json或xml  输出格式为json或者xml  
		ak  是  无  E4805d16520de693a3fe707cdc962045  用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key”  
		sn  否  无   若用户所用ak的校验方式为sn校验时该参数必须。 （sn生成算法）  
		callback  否  无  callback=showLocation(JavaScript函数名)  将json格式的返回值通过callback函数返回以实现jsonp功能  

        http://api.map.baidu.com/geocoder/v2/?address=北京市海淀区上地十街10号&output=json&ak=E4805d16520de693a3fe707cdc962045&callback=showLocation
     
    ====================================================================================================================================================       
              电脑端记录地址定位（通过ip）：       
       http://api.map.baidu.com/location/ip
       https://api.map.baidu.com/location/ip

       
       物品类型： 
       1 失物
       2 拾物
       3 人
              非必填
       
       明细类型：
       1-人
       11-男
       111-老
       112-幼
       113-少年
       114-青年
       115-中年
       12-女
       121-老
       122-幼
       123-少年
       124-青年
       125-中年
       2-物
       21-包
       22-证件
       23-车
       231-自行车
       232-助力车
       233-汽车
       24-钥匙
       25-工具
       26-零件
       27-药品
       28-箱子
   
    证件类型
    1. 身份证
    2. 护照
    3. 驾驶证
    4. 学生证
    5. 其他
    
   积分类型：
    1. 失物招领 
    2. 寻人启事
    
  积分动态：
     ？
     
       消息类型：
       1-拾到物品
       2-寻找物品
       3-悬赏物品
       
        消息状态：
        1-成功
        2-失败
        3-处理中
        
        性别：
        1-男
        2-女
        
     用户状态：
       1-在线
       2-下线
     
     账户状态：
     1-启用
     2-冻结
     3-永久关闭
     
     角色ID：
     1-
     2-
     
     用户组ID：
     1-
     2-
    
    在线状态
      1-在线
      2-下线
      
     是否限制   
      1-启用
      2-冻结
      3-永久关闭
    ----------------------------------------------------*/	
	/**
	 * 
	 * 保存
	 * @param jd //经度
	 * @param wd //纬度
	 * @param ty //失物类型
	 * @param st //操作平台
	 * @param vs //使用版本
	 * @return
	 * 			/*
			 *  *经度(必填)
				*维度(必填)
				*失物类型(失物、拾物、人，非必填)
				*操作平台(Android、iOS)
				*使用版本
               --------------------------------------------
			 	"code": 1,//成功或者失败标志
			    "msg": "",//失败时的原因，成功时为""
			    "data": [//成功时返回的数据集，失败时为""
			{
			    	失物id,//用于查看详情
			    	失物类型,//失物、拾物、人
			    	失物标题,//用于点击位置的时候显示物品简单的描述
					失物经度,
					失物纬度
			 	},
			 {
			    	失物id,//用于查看详情
			    	失物类型,//失物、拾物、人
			    	失物标题,//用于点击位置的时候显示物品简单的描述
					失物经度,
					失物纬度
			 	 }
			    ]
			}";
			https://127.0.0.1:8080/sy/server/Search/syxs?jd=116.395884&wd=39.932154&ty=1&st=Android&vs=4.3.0
			*/
	@RequestMapping(value="/syxs",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Syxsjk returnSyxsjk(@RequestParam(value="jd",required=true)String jd,
			@RequestParam(value="wd",required=true)String wd,@RequestParam(value="ty",required=false)String ty,
			@RequestParam(value="st",required=true)String st,@RequestParam(value="vs",required=true)String vs                            
			){		   
/*-------------------------------------------------------------------------------------------------------------------------
 * web api方式
 * 暂时不需要
 * 保留，方便以后用
 * 
		   //String BAIDU_GEOTABLE_ID="00000";
//		   String BAIDU_SK="mWde8igekw4X6Us2LGGPzjYZwhk1FIXz";
//		   String BAIDU_AK="ririkF1pLLiiq7qCR24ojn9cgbXdLHSj";
//		   String BAIDU_RADIUS="10000";
		   //JD=116.395884,WD=39.932154  39.915,116.404
//		   StringBuffer url=new StringBuffer();
//		   try{
			   //sn校验方式
			   //BAIDU_AK=sncal.toLicCode("","{"+jd+","+wd+"}","1000",BAIDU_SK);
			   //http://api.map.baidu.com/place/v2/search?location=39.915,116.404&radius=2000&output=json&ak={您的密钥}
			
			   
//			   HashMap map=new HashMap();

			   //minlat.doubleValue()
			   //t_sy_item_information  物品表
			   //t_sy_people_information 人物表
			   			   
//			   url.append("http://api.map.baidu.com/place/v2/search?query=酒店$银行&page_size=10&page_num=0&scope=1&");
//			   url.append("&geotable_id=");
//			   url.append(BAIDU_GEOTABLE_ID);
//			   url.append("location=");
//			   url.append(jd);
//			   url.append(",");
//			   url.append(wd);
//			   url.append("&radius=");
//			   url.append(BAIDU_RADIUS);
//			   url.append("&output=json");
//			   url.append("&ak=");
//			   url.append(BAIDU_AK);			   
//			   System.out.println("查找"+url);
			   
//		   }catch(Exception e){
//			  System.out.println("访问错误:"+url);
//		   }
//		   String baidu_result=httpclient.sendGet(url.toString(), new HashMap<String, String>(), new HashMap<String, String>());
//		   System.out.println("IP:"+httpclient.sendGet("http://api.map.baidu.com/location/ip?ak=ririkF1pLLiiq7qCR24ojn9cgbXdLHSj&coor=bd09ll", new HashMap<String, String>(), new HashMap<String, String>()));		  

-------------------------------------------------------------------------------------------------------------------------------*/
		   
//* 通过getErea获得经纬度的范围，用sql语句 查询前台提供的经纬度是否在范围内，例如  (经度>minlat and 经度<maxlat)	 and (纬度>minlng and 纬度<maxlng) 
		   log.info("首页信息查询.....");		   
		   List<SubSyxsjk> data=new ArrayList<SubSyxsjk>();
		   MapUtil jwd=new MapUtil(Double.parseDouble(jd), Double.parseDouble(wd));
		   Double minlat=Double.valueOf(jwd.getMinlat());
		   Double maxlat=Double.valueOf(jwd.getMaxlat());
		   Double minlng=Double.valueOf(jwd.getMinlng());
		   Double maxlng=Double.valueOf(jwd.getMaxlng());  
		   SubSyxsjk subsyxsjk;	
		   Syxsjk syxsjk = new Syxsjk(); 
		   List<SyItemInformation> result=itemservice.selectByItem(jwd);
		   if(result.isEmpty()){
			   syxsjk = new Syxsjk();
			   syxsjk.setCode("0");
			   syxsjk.setMsg("没有找到相应物品......");
		   }else{			   
			   syxsjk.setCode(String.valueOf(result.size()));
			   syxsjk.setMsg("找到"+String.valueOf(result.size())+"个物品");
			   Iterator iterator=result.iterator();	
			   while(iterator.hasNext()){
				   subsyxsjk=new SubSyxsjk();	
				   SyItemInformation temp=(SyItemInformation)iterator.next();
				   subsyxsjk.setSwbt(temp.gettSyItemName());
				   subsyxsjk.setSwlx(temp.gettSyItemType());
				   subsyxsjk.setSwid(String.valueOf(temp.gettSyItemInformationId()));
				   subsyxsjk.setSwjd(temp.gettSyX());
				   subsyxsjk.setSwwd(temp.gettSyY());		
				   data.add(subsyxsjk);	 			 
				   syxsjk.setData(data);
			   }
		   }		      	
		   log.info("查询完毕.....");	
//		   url.setLength(0);
//		   System.out.print(baidu_result);
		   return syxsjk; 
	}
	
	
/*
	详细搜索接口
	1.接口参数
	*经度(必填)
	*维度(必填)
	*失物类型(失物、拾物、人，必填)
	*详细种类(当失物类型为人时，此种类是男/女，当失物类型为物时，此种类是物的具体类型，如：动物/生活用品等等)
	*失物名称
	*搜索日期
	*操作平台(Android、iOS)
	*使用版本
	2.接口返回结果
	{
	 	"code": 1,//成功或者失败标志
	"msg": "",//失败时的原因，成功时为""
	"data": [//成功时返回的数据集，失败时为""
	{
	    	失物id,//用于查看详情
	    	失物种类,//当搜索类型是人的时候，用于展示性别
	    	失物标题,//用于点击位置的时候显示物品简单的描述
			丢失日期
		},
	{
	    	失物id,//用于查看详情
	    	失物种类,//当搜索类型是人的时候，用于展示性别
	    	失物标题,//用于点击位置的时候显示物品简单的描述
	丢失日期
		}
	]
	}
   
	查找物，手表：https://127.0.0.1:8080/sy/server/Search/syxx?jd=116.395884&wd=39.932154&ty=type1&mxty=item1&st=Android&vs=4.3.0
	查找人，男：https://127.0.0.1:8080/sy/server/Search/syxx?jd=116.395884&wd=39.932154&ty=type2&mxty=sex1&st=Android&vs=4.3.0

	详细搜索：搜失物，人，失物，拾物如何区分需要讨论
	*/
	@RequestMapping(value="/syxx",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Xxssjk returnSyxsjk(@RequestParam(value="jd",required=true)String jd,
		@RequestParam(value="wd",required=true)String wd,@RequestParam(value="ty",required=false)String ty,
		@RequestParam(value="mxty",required=false)String mxty,@RequestParam(value="name",required=false)String name,
		@RequestParam(value="vs",required=false)String vs ,@RequestParam(value="ddate",required=false)String ddate,
		@RequestParam(value="st",required=false)String st)
		{	
		  log.info("详细信息查询.....");	
		  MapUtil jwd=new MapUtil(Double.parseDouble(jd), Double.parseDouble(wd));	
		  jwd.setSearchtype((String)CommonDisk.sydisk().get(mxty));
		  Xxssjk  xxssjk = new Xxssjk();
		  Subxxssjk subxxssjk;	
		  System.out.print(ty);
		  if(ty.equalsIgnoreCase("ty2")){		
			   List<SyItemInformation> resultitem=itemservice.selectByDetailItem(jwd);		
			   if(resultitem.isEmpty()){
				   xxssjk.setCode("0");
				   xxssjk.setMsg("没有找到相应物品......");
			   }else{			   
				   xxssjk.setCode(String.valueOf(resultitem.size()));
				   xxssjk.setMsg("找到"+String.valueOf(resultitem.size())+"个相似物");
				   Iterator iterator=resultitem.iterator();	
				   while(iterator.hasNext()){
					   subxxssjk=new Subxxssjk();	
					   SyItemInformation temp=(SyItemInformation)iterator.next();
					   subxxssjk.setSwbt(temp.gettSyItemName());
					   subxxssjk.setSwlx(String.valueOf(temp.gettSyItemType()));
					   subxxssjk.setSwid(String.valueOf(temp.gettSyItemInformationId()));
					   subxxssjk.setSwsj(temp.gettSyLoseDate().toString());
					   xxssjk.getData().add(subxxssjk);	 			 
				   }
			   }	
		  }else{
			   List<SyPeopleInformation> resultpeople=peopleservice.selectByDetailItem(jwd);
			   if(resultpeople.isEmpty()){				
				   xxssjk.setCode("0");
				   xxssjk.setMsg("没有找到相应的人......");
			   }else{			   
				   xxssjk.setCode(String.valueOf(resultpeople.size()));
				   xxssjk.setMsg("找到"+String.valueOf(resultpeople.size())+"个相似人");
				   Iterator iterator=resultpeople.iterator();	
				   while(iterator.hasNext()){
					   subxxssjk=new Subxxssjk();	
					   SyPeopleInformation temp=(SyPeopleInformation)iterator.next();
					   subxxssjk.setSwbt(temp.gettSyName());
					   subxxssjk.setSwlx(String.valueOf(temp.gettSySex()));
					   subxxssjk.setSwid(temp.gettSyCardId());
                        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    //format.format(new Date()));					
					   subxxssjk.setSwsj(temp.gettSyLostDate().toString());
					   xxssjk.getData().add(subxxssjk);	 			 
				   }
			   }
		  }
		  log.info("查询完毕.....");	
		  return xxssjk; 
		
		}
		
	
	
	
	
}
