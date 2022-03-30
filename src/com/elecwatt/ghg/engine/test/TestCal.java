package com.elecwatt.ghg.engine.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.elecwatt.ghg.engine.impl.EmssionItemCal;
import com.elecwatt.ghg.engine.impl.JavacGHGCalculator;
import com.elecwatt.ghg.engine.impl.XMLEmissionFactorFactory;
import com.elecwatt.ghg.engine.model.EmissionFactor;
import com.elecwatt.ghg.engine.model.EmissionItem;
import com.elecwatt.ghg.engine.model.*;
import com.elecwatt.ghg.engine.model.SourceData;
import com.elecwatt.ghg.engine.model.SourceMetaData;

public class TestCal {

	public static void testMetaData() {
		//System.out.println(item);
		String[][] items = {{"燃煤","吨","燃煤台账"},{"外购电力","兆瓦时","外购电力台账"},{"供电电力","兆瓦时","对外供应电力台账"}};
		SourceMetaData data = SourceMetaData.newInstance(items, 4, true);
		System.out.println(data.getItemNum());
		System.out.println(data.getItemIndex("外购电力"));
	}
	
	
	public static SourceData getSourceData() {
		String[][] items= {{"外购电力电表","MWH",""},{"输出电力电表","MWH",""}};
		SourceMetaData meta = SourceMetaData.newInstance(items, 8, true);
		String[] lables = {"1","2","3","4","5","6","7","8"};
		double[][] matrix = {{0,0},{100,1000},{500,5000},{1000,10000},{2000,50000},{4000,120000},{6000,200000},{7000,250000}};
		SourceData data= SourceData.newInstance(meta, matrix, lables);

		System.out.println("getSourceData");
//		for(int i = 0 ;i < data.getMatrix().length;i++) {
//			for(int j=0;j<data.getMatrix()[0].length;j++) {
////				System.out.println(data.getMatrix()[0].length);
//				System.out.print(data.getMatrix()[i][j]);
//				System.out.print("   ");
//				System.out.println(data.getMatrix().length);
//			}
//			System.out.println();
//		}
	return data;
	}
	
	public static EmissionItem  getItem() {
		EmissionItem item = new EmissionItem("301","条目", "free", "(($输出电力电表$ * #f1#) - $外购电力电表$) * #f2#");
		return item;
	}
	public static EmissionItem loadEmissionItemFromXML() {
		 File file = new File("item.xml");
	        JAXBContext jc = null;
	        EmissionItem item= null;
	        try {
	            jc = JAXBContext.newInstance(EmissionItem.class);	           
	            Unmarshaller ma = jc.createUnmarshaller();
	            item = (EmissionItem)ma.unmarshal(file);	            
	            //System.out.println(item.getFormula());
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	        return item;
	}
	
	public static EmissionCatalog loadEmissionCatalogFromXML() {
		 File file = new File("catalog.xml");
	        JAXBContext jc = null;
	        EmissionCatalog catalog= null;
	        try {
	            jc = JAXBContext.newInstance(EmissionCatalog.class,EmissionItem.class);	           
	            Unmarshaller ma = jc.createUnmarshaller();
	            catalog = (EmissionCatalog)ma.unmarshal(file);	           
	            
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	        return catalog;
	}
	public static GPGCalModel loadEmissionModelFromXML() {
		 File file = new File("GHG.xml");
	        JAXBContext jc = null;
	        GPGCalModel model= null;
	        try {
	            jc = JAXBContext.newInstance(GPGCalModel.class,EmissionCatalog.class,EmissionItem.class);	           
	            Unmarshaller ma = jc.createUnmarshaller();
	            model = (GPGCalModel)ma.unmarshal(file);	           
	            
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	        return model;
	}
	
	public static Map<String, EmissionFactor> getFactors(){
		File file = new File("factors.xml");
		XMLEmissionFactorFactory fac = XMLEmissionFactorFactory.init(file);
		return fac.getFactorsMap();
	}
	
	public static void testItemCal() {
		Map<String, EmissionFactor> factors = getFactors();		
		EmssionItemCal cal = new EmssionItemCal(getItem());	
		cal.setEmissionFactors(factors);
		SourceData data = getSourceData();
		cal.cal(data);
		
	}
	public static void testJavacGHGCalculatorItem() throws Exception {
		//将xml转换成对象结构
		EmissionModel model = loadEmissionItemFromXML();
		JavacGHGCalculator cal = new JavacGHGCalculator(model);
		cal.setEmissionFactors(getFactors());
		cal.bindSouce(model, getSourceData());
		EmissionCalResult result=cal.cal();
		printCalResult(result);
	}
	public static void testJavacGHGCalculatorCatalog() throws Exception {
		//将xml转换成对象结构
		EmissionModel model = loadEmissionCatalogFromXML();
		JavacGHGCalculator cal = new JavacGHGCalculator(model);
		//获取排放因子对象结构
		cal.setEmissionFactors(getFactors());
		//获取源数据结构和值
		cal.bindSouce(model, getSourceData());
		//根据模型结构和排放因子计算结果
		EmissionCalResult result=cal.cal();
		printCalResult(result);
	}
	
	public static void testJavacGHGCalculatorModel() throws Exception {
		EmissionModel model = loadEmissionModelFromXML();
		JavacGHGCalculator cal = new JavacGHGCalculator(model);
		cal.setEmissionFactors(getFactors());
		cal.bindSouce(model, getSourceData());
		EmissionCalResult result=cal.cal();
		printCalResult(result);
	}
	
	
	public static void printCalResult(EmissionCalResult result) {
		System.out.println(result.getEmessionModel().getModelType()+" "+result.getEmessionModel().getLable() +" Sum="+result.getSumResult());
		for(EmissionCalResult.Record record: result.getRecord()) {
			System.out.println("   "+record);
		}
		List<EmissionCalResult> child = result.getChildResult();
		if(child !=null && child.size()>0) {
			for(EmissionCalResult ch:child) {
				printCalResult(ch);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		testMetaData();
//		getSourceData();
		//testItemCal();
		//testJavacGHGCalculatorItem();
		testJavacGHGCalculatorCatalog();
//		testJavacGHGCalculatorModel();
	}
	
	

}
