package com.elecwatt.ghg.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * 温室气体盘算模型 确定企业排放边界后，根据国家相关核算标准建立企业温室气体排放计算模型，包括了需要核查的大类、计算的项目以及适用的计算公式和排放因子
 * 一个企业一次盘算可以建立一个也可以建立多个模型。如发电企业可以为每一个发电机组建立一个排放模型，也可以为全公司建立一个模型
 * 
 * @author Surface
 *
 */
@XmlRootElement(name = "GPGCalculateModel")
@XmlType(propOrder = { "id", "lable", "version", "author", "description", "emissionCatalog" })
public class GPGCalModel implements EmissionModel {

	private String id;
	private String lable;
	private String description;
	private String version;
	private String author;
	private List<EmissionCatalog> catalogList = new ArrayList<EmissionCatalog>();
	private EmissionModel parent = null;

	@XmlTransient
	public EmissionModel getParent() {
		return parent;
	}

	public void setParent(EmissionModel parent) {
		this.parent = parent;
	}

	public GPGCalModel() {

	}

	public GPGCalModel(String id, String lable) {
		this.id = id;
		this.lable = lable;
	}

	@XmlAttribute
	@XmlID
	public String getId() {

		return id;
	}

	public void SetId(String id) {
		this.id = id;

	}

	@XmlAttribute
	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;

	}

	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;

	}

	@XmlAttribute
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlAttribute
	public String getAuthor() {

		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;

	}

	@XmlElement(name = "EmissionCatalog")
	public List<EmissionCatalog> getEmissionCatalog() {
		return catalogList;
	}

	public void addEmissionCatalog(EmissionCatalog catalog) {
		this.catalogList.add(catalog);
		return;
	}

	public void removeEmissionCatalog(String catalogLable) {
		Iterator<EmissionCatalog> it = this.catalogList.iterator();
		while (it.hasNext()) {
			EmissionCatalog catalog = (EmissionCatalog) it.next();
			if (catalog.getLable().equalsIgnoreCase(catalogLable)) {
				this.catalogList.remove(catalog);
				return;
			}
		}
	}

	@XmlTransient
	public List<EmissionModel> getChild() {
		List<EmissionModel> list = new ArrayList<EmissionModel>();
		list.addAll(catalogList);
		return list;
	}
	@XmlTransient
	public String getModelType() {
		return EmissionModel.MODEL;
	}
	@XmlTransient
	public boolean isEnable() {

		return true;
	}
}
