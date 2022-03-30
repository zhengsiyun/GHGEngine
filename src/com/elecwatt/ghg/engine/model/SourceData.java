package com.elecwatt.ghg.engine.model;
/**
 * 一个用于计算温室气体排放的源数据表，一般是记录能源消耗的台账。如2022年度，每月 燃煤、燃气、外购电力的数据汇总表
 * 在温室气体排放计算时，首先要把数据（不管数据时来自数据库、在线填报、excel文件或者iot系统）
 * 整理为合乎规定的台账数据（SourceData）后，与排放模型共同作为参数传递给温室气体排放计算器计算
 * 对于特殊的数据源，可以构建SourceData的子类。如IoT对接的仪器数据可以把汇接算法加入到子类中。但是基于功能分类的设计原则，不建议采用该类做法
 * 为提高计算效率，源数据内容采用double[][]二维double数组存储
 * @author Surface
 *
 */
public class SourceData {
	/**
	 * metaData中记录了数据行数的数据，且为只读。这意味着在源数据在计算过程中是只读的，确保不同的碳计算模型、算子在引用该
	 * 源数据对象时，获得的数据是一样的。
	 */
	private SourceMetaData metaData ;	
	private double[][] matrix ;
	private String[] lables;
	private SourceData(){
		
	}
	/**
	 * 根据源数据内容创建一个源数据对象	
	 * @param metaData
	 * @param matrix
	 * @param lables
	 * @return
	 */
	 public static SourceData newInstance(SourceMetaData metaData,double[][] matrix,String[] lables) {
		 SourceData data = new SourceData();
		 //合法性检查
		 int lineNum = metaData.getLineNum();
		 if (lables.length!=lineNum){
			 System.out.println("输入的标签行数不一致");
			 return data;
		 }else if(matrix.length!=lineNum){
			 System.out.println("数据行数不一致");
		 }
		 int itemNum = metaData.getItemNum();
		 for(int index = 0; index < matrix.length; index++) {
			 if (matrix[index].length!=itemNum){
				 System.out.println("数据列数不一致");
				 return data;
			 }
		 }
		 data.metaData = metaData;
		 data.matrix =new  double[matrix.length][];
		 for(int index = 0; index < data.matrix.length; index++) {
			 data.matrix[index] = matrix[index].clone();
		 }
		 if(lables != null) {
			 data.lables = lables.clone();
		 }
		 return data;
	 }
	public String[] getLables() {
		return lables;
	}
	public SourceMetaData getMetaData() {
		return metaData;
	}
	public double[][] getMatrix(){
		return matrix;
	}

}
