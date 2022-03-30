package com.elecwatt.ghg.engine.model;

/**
**
* 温室气体盘算排放条目
* 为方便计算，同时也为排放模型更加清晰易读，暂定每一个排放条目只计算一种温室气体数据
* 该模式对于温室气体排放盘算是适用的，如果用于碳足迹计算，则还是一个计算条目可以同时计算几种温室气体值更加易用
* 
* @author Surface
*
*/
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "EmissionItem")
@XmlType(propOrder = { "id","lable", "emissionType","unit","formulaType", "enable", "formula" })
public class EmissionItem implements EmissionModel {
	private EmissionModel parent = null;
	
	@XmlTransient
	public EmissionModel getParent() {
		return parent;
	}

	public void setParent(EmissionModel parent) {
		this.parent = parent;
	}
	private String lable = "外购电力-华东电网";
	private String emissionType = "co2";
	private String unit = "t";
	private String id;
	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	@XmlAttribute
	public void setUnit(String unit) {
		this.unit = unit;
	}

	private boolean enable = false;
	private String formulaType = "free";
	private String formula = null;

	public EmissionItem() {
	}

	public EmissionItem(String id,String lable, String formulaType, String formula) {
		this.id = id;
		this.lable = lable;
		this.formula = formula;
		this.formulaType = formulaType;
	}

	@XmlAttribute
	@XmlID
	public String getLable() {

		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;

	}

	@XmlAttribute
	public String getEmissionType() {
		return emissionType;
	}

	public void setEmissionType(String emissionType) {
		this.emissionType = emissionType;
		return;
	}

	public boolean isEnable() {

		return enable;
	}

	@XmlAttribute
	public void setEnable(boolean enable) {
		this.enable = enable;
		return;
	}

	public String getFormulaType() {
		return formulaType;
	}

	@XmlAttribute
	public String setFormulaType(String formulaType) {
		this.formulaType = formulaType;
		return null;
	}

	public String getFormula() {

		return formula;
	}

	@XmlValue
	public void setFormula(String formula) {
		this.formula = formula;

	}
	@XmlTransient
	public List<EmissionModel> getChild(){
		return null;
	}
	@XmlTransient
	public String getModelType() {
		return EmissionModel.ITEM;
	}

}