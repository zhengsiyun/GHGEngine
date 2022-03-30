package com.elecwatt.ghg.engine.impl;
import java.io.File;
/**
 * 这是一个用于验证测试的工具类，用于从XML文件中加载排放因子库，并可以根据因子ID号获取因子对象
 * @author Surface
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.elecwatt.ghg.engine.model.EmissionFactor;

@XmlRootElement(name = "EmissionFactors")
public class XMLEmissionFactorFactory {

	private List<EmissionFactor> factors = new ArrayList<EmissionFactor>();
	private Map<String,EmissionFactor> factorsMap = null;
	
	@XmlElement(name = "EmissionFactor")
	 List<EmissionFactor> getFactors() {
		return factors;
	}

	 void setFactors(List<EmissionFactor> factors) {
		this.factors = factors;
	}

	XMLEmissionFactorFactory() {}		
	
	public Map<String,EmissionFactor>  getFactorsMap() {
		return factorsMap;
	}
	public EmissionFactor getFactor(String id) {
		return factorsMap.get(id);
	}
	public static  XMLEmissionFactorFactory init(File file) {
	        JAXBContext jc = null;
	        try {
	            jc = JAXBContext.newInstance(XMLEmissionFactorFactory.class,EmissionFactor.class);
	            Unmarshaller uma = jc.createUnmarshaller();	            
	         	XMLEmissionFactorFactory factory = (XMLEmissionFactorFactory)uma.unmarshal(file);
	           	Map<String,EmissionFactor> factorsMap = new HashMap<String,EmissionFactor>();
	           	for(EmissionFactor factor:factory.factors) {
	           		factorsMap.put(factor.getId(), factor);
	           	}
	           	factory.factorsMap = factorsMap;
	           	return factory;
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
		return null;
	}

	public static void main(String[] args) {
		 File file = new File("C:\\Users\\郑思允\\Documents\\碳引擎\\GHGEngine\\factors.xml");
		 XMLEmissionFactorFactory fac = XMLEmissionFactorFactory.init(file);
		 System.out.println(fac);

	}

}
