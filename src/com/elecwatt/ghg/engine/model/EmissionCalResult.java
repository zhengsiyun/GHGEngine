package com.elecwatt.ghg.engine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmissionCalResult {

	/**
	 * 为程序简单起见，定义所有的结果为温室气体数量信息加一个key-value的附加信息。
	 * 这样把模型计算实现和应用实现耦合起来了，不太合适。以后可以修订	 
	 * 正常的这个对象应该是只读的。 
	 */
	private Map<String,Object> information;
	private List<EmissionCalResult.Record> result=new ArrayList<EmissionCalResult.Record>();;
	private EmissionCalResult parentResult = null;
	private List<EmissionCalResult> childResult = null;
	private EmissionModel model;
	private double sumResultValue = 0;
	public EmissionCalResult(EmissionModel model) {
		this.model = model;
	}
	public EmissionModel getEmessionModel() {
		return model;
	}
	public EmissionCalResult geParentResult() {
		return parentResult;
	}
	public List<EmissionCalResult> getChildResult(){
		return childResult;
	}
	
	public void addChild(EmissionCalResult child) {
		if(childResult == null) {
			childResult = new ArrayList<EmissionCalResult>();				
		}
		childResult.add(child);
		child.parentResult = this;
		sumResultValue+= child.sumResultValue;
	}
	public Record addRecord(String type,String unit,double value) {
		Record record= Record.newInstance(type, unit, value);
		if(result == null) {
			result = new ArrayList<EmissionCalResult.Record>();			
		}
		result.add(record);
		sumResultValue+=value;
		return record;
	}
	
	public List<EmissionCalResult.Record> getRecord(){
		return result;
	}	
	public Map<String,Object> getInformation(){
		return information;
	}
	public void setInformation (Map<String,Object> information){
		this.information = information;
	}
	public double getSumResult() {
		return sumResultValue;
	}
	
	public static class Record{
		private String type;
		private String unit;
		private double value;
	
		private Record() {
			
		}
		public static Record newInstance(String type,String unit,double value) {
			Record record = new Record();
			record.type = type;
			record.unit = unit;
			record.value = value;			
			return record;
		}
		public String getType() {
			return type;
		}
		public String getUnit() {
			return unit;
		}
		public double getValue() {
			return value;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("type=").append(type);
			sb.append(" unit=").append(unit);
			sb.append(" value=").append(value);
			return sb.toString();
			
		}
		
	}

}
