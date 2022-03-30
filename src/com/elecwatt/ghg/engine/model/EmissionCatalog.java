package com.elecwatt.ghg.engine.model;

/**
 * 温室气体排放大类计算模型
 * @author Surface
 *
 */
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "EmissionCatalog")
@XmlType(propOrder = {"id", "lable", "type", "scope", "enable", "emissionItem" })
public class EmissionCatalog  implements EmissionModel{
	private boolean enable = false;
	private int scope = 2;
	private String type = "外购电力";
	private String lable = "企业-分厂-外购电力";
	private String id;
	public String getId() {
		return id;
	}
	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	private List<EmissionItem> itemList = new ArrayList<EmissionItem>();
	// private List<EmissionItem> itemList = null;
	private EmissionModel parent = null;
	@XmlTransient
	public EmissionModel getParent() {
		return parent;
	}
	public void setParent(EmissionModel parent) {
		this.parent = parent;
	}
	public EmissionCatalog() {
		// TODO Auto-generated constructor stub
	}
	public EmissionCatalog(String id,String lable) {
		this.id = id;
		this.lable = lable;
	}

	/**
	 * 获得排放计算大类类型名称，如企业购入电量排放。根据发改委核算排放指南，每个行业都有固定的几个排放大类。
	 * 由于不同行业规定的排放大类名称不同，所以此处type设置为可编辑 一个企业排放模型中全部排放大类，可以根据排放大类的名称进行聚合。
	 * 
	 * @return 排放计算大类名称，
	 */
	@XmlAttribute
	public int getScope() {
		// TODO Auto-generated method stub
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getLable() {

		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	@XmlAttribute
	public boolean isEnable() {

		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
		return;
	}

	@XmlElement(name = "EmissionItem")
	public List<EmissionItem> getEmissionItem() {
		return itemList;
	}

	public void setEmissionItem(List<EmissionItem> EmissionItem) {
		this.itemList.clear();
		this.itemList.addAll(EmissionItem);
	}

	public void addEmissionItem(EmissionItem item) {
		this.itemList.add(item);
	}
	@XmlTransient
	public List<EmissionModel> getChild(){		
		List<EmissionModel> list= new ArrayList<EmissionModel>();
		list.addAll(itemList);
		return list;
	}
	@XmlTransient
	public String getModelType() {
		return EmissionModel.CATALOG;
	}
}