package com.elecwatt.ghg.engine.model;

import java.util.ArrayList;

/**
 * 该对象描述了一个可用于温室气体排放的源数据的结构信息
 * 源数据特点如下（类excel sheet或数据table）
 * 由多行数据构成，每行数据的结构一致
 * 每一列数据代表了一个数据源项目，一个项目包括名称和单位两个属性定义
 * 所有源数据均为double数字
 * 结构：{{名称、单位}}
 * 数据:{{doule}}
 * 源数据在计算过程中为只读
 * 源数据可选带lable，一个label对应一行值。如 **年**月的各项能耗数据
 * @author Surface
 *
 */
public class SourceMetaData implements Cloneable{

	private ArrayList<SourceMetaData.Item> itemList = new ArrayList<SourceMetaData.Item>();
	private int lineNum =0;
	private boolean hasLable= false;
	
	private SourceMetaData() {
		
	}
	private void addItem(Item item) {
		itemList.add(item);
	}
	public static SourceMetaData newInstance(String[][] items,int lineNum,boolean hasLable) {
		SourceMetaData data = new SourceMetaData();
		data.lineNum = lineNum;
		data.hasLable = hasLable;
		for(String[] item:items) {
			data.addItem(Item.newInstance(item[0], item[1], item[2]));
		}
		return data;
	}
	public int getItemNum() {
		return itemList.size();
	}
	public int getItemIndex(String itemName) {
		for(int index = 0; index<itemList.size(); index++) {
			Item item = itemList.get(index);
			if(item.getName().equals(itemName)){
				return index;
			}
		}
		return -1;
	}
	public Item getItem(int index) {
		return null;
	}
	public int getLineNum() {
		return lineNum;
	}
	public boolean isHasLable() {
		return hasLable;
	}
	
	public static class Item{
		private String name;
		private String unit;
		private String description;
		private Item() {
			
		}
		public static Item newInstance(String name,String unit,String description) {
			Item item = new Item();
			item.name = name;
			item.unit = unit;
			item.description = description;
			//System.out.println(name+"_"+unit+"_"+description);
			return item;
		}
		public String getName() {
			return name;
		}
		public String getUnit() {
			return unit;
		}
		public String getDescription() {
			return description;
		}
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
