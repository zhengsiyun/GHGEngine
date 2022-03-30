package com.elecwatt.ghg.engine.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 排放因子
 * @author Surface
 *
 */
@XmlRootElement(name = "EmissionFactor")
@XmlType(propOrder = { "id","name", "industry", "year", "value", "emmisionType","emmisionUnit","origin","standard","createTime","lastModefyTime","remark" })
public class EmissionFactor {
	private String id;
	private String name;
	private String industry;
	private int year;
	private String emmisionType = GHGGasType.CO2;
	private String emmisionUnit = GPGCalUnit.tCO2e;
	private double value;	
	private String origin;
	private String standard;
	private Date createTime;
	private Date lastModefyTime;
	private String remark;
	
	
	public EmissionFactor() {
		
	}
	public EmissionFactor(String id,String name,String type,String unit,double value) {
		this.id = id;
		this.name = name;
		this.emmisionType= type;
		this.value = value;
				
	}
/**
 * 唯一标识号
 * @return
 */
	@XmlAttribute 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 显示名称
	 * @return
	 */
	@XmlAttribute 
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

/**
 * 适用的行业类型
 * @return
 */
	@XmlAttribute 
	public String getIndustry() {
		return industry;
	}


	public void setIndustry(String industry) {
		this.industry = industry;
	}

/**
 * 因子发布年份
 * @return
 */
	@XmlAttribute 
	public int getYear() {
		return year;
	}

	
	public void setYear(int year) {
		this.year = year;
	}

/**
 * 排放类型，一般就是温室气体类型
 * @return
 */
	@XmlAttribute 
	public String getEmmisionType() {
		return emmisionType;
	}


	public void setEmmisionType(String emmisionType) {
		this.emmisionType = emmisionType;
	}

/**
 * 排放单位
 * @return
 */
	@XmlAttribute 
	public String getEmmisionUnit() {
		return emmisionUnit;
	}


	public void setEmmisionUnit(String emmisionUnit) {
		this.emmisionUnit = emmisionUnit;
	}

/**
 * 因子值
 * @return
 */
	@XmlAttribute
	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}
/**
 * 该因子数据的出处。指制定这个因子值的最初源头
 * @return
 */
	@XmlAttribute 
	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}

/**
 * 该因子所在的标准。指引用这个因子值作为排放计算依据的相关行业、国家标准
 * @return
 */
	@XmlAttribute 
	public String getStandard() {
		return standard;
	}


	public void setStandard(String standard) {
		this.standard = standard;
	}

/**
 * 因子数据入库时间
 * @return
 */
	@XmlAttribute 
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
/**
 * 因子数据最后修改时间
 * @return
 */
	@XmlAttribute 
	public Date getLastModefyTime() {
		return lastModefyTime;
	}


	public void setLastModefyTime(Date lastModefyTime) {
		this.lastModefyTime = lastModefyTime;
	}
/**
 * 该数据的备注信息
 * @return
 */
	@XmlElement
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

}
