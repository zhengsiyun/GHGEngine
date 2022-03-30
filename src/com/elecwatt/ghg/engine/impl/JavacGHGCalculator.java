package com.elecwatt.ghg.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elecwatt.ghg.engine.GHGCalculator;
import com.elecwatt.ghg.engine.model.EmissionCalResult;
import com.elecwatt.ghg.engine.model.EmissionCatalog;
import com.elecwatt.ghg.engine.model.EmissionFactor;
import com.elecwatt.ghg.engine.model.EmissionItem;
import com.elecwatt.ghg.engine.model.EmissionModel;
import com.elecwatt.ghg.engine.model.GPGCalModel;
import com.elecwatt.ghg.engine.model.SourceData;

public class JavacGHGCalculator implements GHGCalculator {
	private EmissionModel model = null;
	private Map<EmissionModel, SourceData> modelSoureceMap = new HashMap<EmissionModel, SourceData>();
	private Map<String, EmissionFactor> factorMap;
	public JavacGHGCalculator(EmissionModel model) {
		this.model = model;
		setModelParent(model);
	}

	@Override
	public EmissionModel getEmessionModel() {
		return model;
	}

	/**
	 * 在计算之前要首先绑定数据源
	 * 由于JAXB在生成对象时，没有设置每个catalog和item的父对象，为方便寻找每个模型的datasource，这里额外做了一步 设置每个对象的父模型。
	 */
	void setModelParent(EmissionModel model) {
		List<EmissionModel> child = model.getChild();
		if (child != null) {
			for (EmissionModel ch : child) {
				ch.setParent(model);
				setModelParent(ch);
			}
		}
	}

	/**
	 * 设置父对象关系时，只从计算器绑定的模型算起，这样就强迫每次计算时要为这次计算设置数据源。防止出现混乱。
	 * 比如，虽然为model设置了数据源，但是当单独计算其下属的item时，还需要为item明确的设定一个数据源。
	 * @throws Exception 
	 */
	@Override
	public EmissionCalResult cal() throws Exception {		
		String modelType = model.getModelType();
		if(EmissionModel.ITEM.equals(modelType)) {
			return modelCal((EmissionItem)(this.model));
		}
		if(EmissionModel.CATALOG.equals(modelType)) {
			return catalogCal((EmissionCatalog)(this.model));
		}
		if(EmissionModel.MODEL.equals(modelType)) {
			return modelCal((GPGCalModel)(this.model));
		}
		return null;
	}
	/**
	 * Model计算。目前只是将其下各个排放大类分别计算并求和
	 * 由于排放模型的计算可能会有其他特殊性，所以单独一个方法。以方便改动
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	private  EmissionCalResult modelCal(GPGCalModel model) throws Exception {
		EmissionCalResult result = new EmissionCalResult(model);
		List<EmissionCatalog> items = model.getEmissionCatalog();
		for(EmissionCatalog item:items) {
			if(item.isEnable()) {
				EmissionCalResult itemResult = catalogCal(item);
				result.addChild(itemResult);				
			}
		}
		Map<String,Object> information = new HashMap<String,Object>();
		information.put(EmissionModel.MODEL, model);
		result.setInformation(information);
		return result;
	}
	
	/**
	 * Catalog计算。目前只是将其下各个条目分别计算并求和
	 * 由于排放大类的计算可能会有其他特殊性，所以单独一个方法。以方便改动
	 * @param catalog
	 * @return
	 * @throws Exception 
	 */
	private  EmissionCalResult catalogCal(EmissionCatalog catalog) throws Exception {
		EmissionCalResult result = new EmissionCalResult(catalog);
		List<EmissionItem> items = catalog.getEmissionItem();
		for(EmissionItem item:items) {
			if(item.isEnable()) {
				EmissionCalResult itemResult = modelCal(item);
				result.addChild(itemResult);				
			}
		}
		Map<String,Object> information = new HashMap<String,Object>();
		information.put(EmissionModel.CATALOG, catalog);
		result.setInformation(information);
		return result;
	}
	/**
	 * 只有item是要计算的，其余的都是一个对子类的求和
	 * @param item
	 * @return
	 * @throws Exception 
	 */
	private  EmissionCalResult modelCal(EmissionItem item) throws Exception {
		SourceData data = getSourceData(item);
		if(data == null) {
			throw new Exception("未找到数据源："+item.getLable());
		}
		EmssionItemCal cal = new EmssionItemCal(item);
		cal.setEmissionFactors(factorMap);		
		return cal.cal(data);
	}
	/**
	 * 获取模型适用的源数据
	 * 自己绑定了就用自己的，否则就用最近的父对象的
	 * @param model
	 * @return
	 */
	private  SourceData  getSourceData(EmissionModel model) {
		if(modelSoureceMap.get(model)!=null) {
			return modelSoureceMap.get(model);
		}
		else if(model.getParent()!=null) {
			return getSourceData(model.getParent());
		}else {
			return null;
		}		
	}
	
	@Override
	public void bindSouce(EmissionModel model, SourceData sourceData) {
		modelSoureceMap.put(model, sourceData);

	}
	/**
	 * 由于本算法是把排放因子加载到内存中，故采用预先设置排放因子库的方法
	 * 如果采用在计算时从数据库中动态加载排放因子，则可以不用该方法
	 * @param factorMap
	 */
	public void setEmissionFactors(Map<String, EmissionFactor> factorMap) {
		this.factorMap = factorMap;		
		return;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
