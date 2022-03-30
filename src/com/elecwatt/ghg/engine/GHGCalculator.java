package com.elecwatt.ghg.engine;

import com.elecwatt.ghg.engine.model.*;

public interface GHGCalculator {
	/**
	 * 每个温室气体计算器都绑定一个温室气体排放计算模型，且在整个计算器对象生命周期中该模型不可更改
	 * @return
	 */
	public EmissionModel getEmessionModel(); 
	/**
	 * 根据绑定的源数据计算温室气体排放
	 * 计算可以从任何一级开始，父节点的排放数据等于其所有子节点的排放数据的总和
	 * @return
	 * @throws Exception 
	 */
	public EmissionCalResult cal() throws Exception;
	
	public void bindSouce(EmissionModel model,SourceData sourceData);
	
}
