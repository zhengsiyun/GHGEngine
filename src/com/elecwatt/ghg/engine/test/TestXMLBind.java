package com.elecwatt.ghg.engine.test;


import com.elecwatt.ghg.engine.model.* ;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



public class TestXMLBind {
	public static void testItemMarshaller() {
		EmissionItem item = new EmissionItem("301","条目","free","(($输出电力电表$ * #f1#) - $外购电力电表$) * #f2#");
		item.setEnable(true);
		File file = new File("item.xml");		
	        JAXBContext jc = null;
	        try {
	            jc = JAXBContext.newInstance(EmissionItem.class);
	  	        Marshaller ma = jc.createMarshaller();
	            //以下是为生成xml做的一些配置
	            //格式化输出，即按标签自动换行，否则就是一行输出
	            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            //设置编码（默认编码就是utf-8）
	            ma.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	            //是否省略xml头信息，默认不省略（false）
	            ma.setProperty(Marshaller.JAXB_FRAGMENT, false);
	 
	            //编组
	            ma.marshal(item, file);
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void testItemUnmarshaller() {
		 File file = new File("item.xml");
	        JAXBContext jc = null;
	        try {
	            jc = JAXBContext.newInstance(EmissionItem.class);	           
	            Unmarshaller ma = jc.createUnmarshaller();
	            EmissionItem item = (EmissionItem)ma.unmarshal(file);	            
	            System.out.println(item.getFormula());
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	}

	public static void testCatalogMarshaller() {
		EmissionItem item1 = new EmissionItem("301","条目1","free","(($输出电力电表$ * #f1#) - $外购电力电表$) * #f2#");
		EmissionItem item2 = new EmissionItem("302","条目2","free","(($输出电力电表$ * #f1#) - $外购电力电表$) * #f2#");
		EmissionItem item3 = new EmissionItem("303","条目3","free","(($输出电力电表$ * #f1#) - $外购电力电表$) * #f2#");
		item1.setEnable(true);
		item2.setEnable(true);
		List<EmissionItem> itemList = new ArrayList<EmissionItem>();
		itemList.add(item1);
		itemList.add(item2);
		itemList.add(item3);
		
		EmissionCatalog  catalog = new EmissionCatalog("201","类型1");
		catalog.setEmissionItem(itemList);
		catalog.setEnable(true);
		catalog.setScope(2);		
		
		 File file = new File("catalog.xml");
	        JAXBContext jc = null;
	        try {
	            //根据Person类生成上下文对象
	            jc = JAXBContext.newInstance(EmissionCatalog.class,EmissionItem.class);
	            //从上下文中获取Marshaller对象，用作将bean编组(转换)为xml
	            Marshaller ma = jc.createMarshaller();
	            //以下是为生成xml做的一些配置
	            //格式化输出，即按标签自动换行，否则就是一行输出
	            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            //设置编码（默认编码就是utf-8）
	            ma.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	            //是否省略xml头信息，默认不省略（false）
	            ma.setProperty(Marshaller.JAXB_FRAGMENT, false);
	 
	            //编组
	            ma.marshal(catalog, file);
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	}
	public static EmissionCatalog testCatalogUnmarshaller() {
		 File file = new File("catalog.xml");
	        JAXBContext jc = null;
	        try {
	            jc = JAXBContext.newInstance(EmissionCatalog.class,EmissionItem.class);	           
	            Unmarshaller ma = jc.createUnmarshaller();
	            EmissionCatalog catalog = (EmissionCatalog)ma.unmarshal(file);	            
	            System.out.println(catalog.getLable());
	            System.out.println(catalog.getEmissionItem());
	            System.out.println(catalog.getEmissionItem().size());
	            return catalog;
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	        return null;
	}
	
	public static void testGHGMarshaller() {
		EmissionCatalog catalog = testCatalogUnmarshaller();
		GPGCalModel model = new GPGCalModel("001","测试模型-电厂");
		model.addEmissionCatalog(catalog);
		model.addEmissionCatalog(catalog);
		model.addEmissionCatalog(catalog);
		model.setDescription("这是一个电厂的排放计算模型");
		 File file = new File("C:\\Users\\郑思允\\Documents\\碳引擎\\GHGEngine\\GHG.xml");
	        JAXBContext jc = null;
	        try {
	            //根据Person类生成上下文对象
	            jc = JAXBContext.newInstance(GPGCalModel.class,EmissionCatalog.class,EmissionItem.class);
	            //从上下文中获取Marshaller对象，用作将bean编组(转换)为xml
	            Marshaller ma = jc.createMarshaller();
	            //以下是为生成xml做的一些配置
	            //格式化输出，即按标签自动换行，否则就是一行输出
	            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            //设置编码（默认编码就是utf-8）
	            ma.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	            //是否省略xml头信息，默认不省略（false）
	            ma.setProperty(Marshaller.JAXB_FRAGMENT, false);
	 
	            //编组
	            ma.marshal(model, file);
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void testEmissionFactorMa() {
		EmissionFactor factor = new EmissionFactor("1","华东电网",GHGGasType.CO2,GPGCalUnit.tCO2e_PER_MWh, 0.788);
		 File file = new File("factor.xml");
	        JAXBContext jc = null;
	        try {
	            jc = JAXBContext.newInstance(EmissionFactor.class);
	  	        Marshaller ma = jc.createMarshaller();
	            //以下是为生成xml做的一些配置
	            //格式化输出，即按标签自动换行，否则就是一行输出
	            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            //设置编码（默认编码就是utf-8）
	            ma.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	            //是否省略xml头信息，默认不省略（false）
	            ma.setProperty(Marshaller.JAXB_FRAGMENT, false);
	 
	            //编组
	            ma.marshal(factor, file);
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void main(String[] args)
	{
		testItemMarshaller();
		testItemUnmarshaller();
		testCatalogMarshaller();
		testCatalogUnmarshaller();
		testGHGMarshaller();
		testEmissionFactorMa();
	}
}
