package com.elecwatt.ghg.engine.model;

import java.util.List;

public interface  EmissionModel {
	
	public EmissionModel getParent();
	public void setParent(EmissionModel parent);
	public List<EmissionModel> getChild();
	public String getModelType();
	public boolean isEnable();
	public String getId();
	public String getLable();
	public static String ITEM = "EmissionItem";
	public static String CATALOG = "EmissionCatalog";
	public static String MODEL = "GPGCalculateModel";
	
}
