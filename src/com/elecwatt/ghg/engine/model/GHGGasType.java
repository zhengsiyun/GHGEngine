package com.elecwatt.ghg.engine.model;
/**
 * 温室气体类型
 * 为方便加入新排放气体、兼容碳足迹，暂用字符串表示。后期可以改为enum
 * @author Surface
 *
 */
public interface GHGGasType {
	public static final String CO2  = "CO2";	
	public static final String NH4  = "NH4";
	public static final String[] GASSet = {GHGGasType.CO2,GHGGasType.NH4};
}
