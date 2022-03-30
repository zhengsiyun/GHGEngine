package com.elecwatt.ghg.engine.impl;

import java.util.*;

import com.elecwatt.ghg.engine.model.*;

public class EmssionItemCal {
	private EmissionItem item = null;
	// 表达式中全部变量数组，包括源数据项目和排放因子。根据这个数组，结合源数据和因子库就可以得到每个公式的输入参数
	private String[] expressionParamName;
	// 公式中依次出现的排放因子ID
	private String[] factorsName;
	// 公式中依次出现的排放因子
	private EmissionFactor[] factors;
	private Map<String, EmissionFactor> factorMap;
	private String formuaString;

	public EmssionItemCal(EmissionItem item) {
		this.item = item;
		getCalExpression(item);
	}

	/**
	 * 获取可用于计算的表达式 如输入 $外购电量$ * &华东电网排放因子& 则 公式 $ * $ 参数表 $外购电量 ，&华东电网排放因子
	 * 
	 * @param item
	 */
	private void getCalExpression(EmissionItem item) {
		formuaString = item.getFormula().trim();
		// 源数据变量用 $标记，如 $外购电量$
		// 因子数据变量用#标记，如 #华东电网排放因子#
		ArrayList<String> paramList = new ArrayList<String>();
		ArrayList<String> facs = new ArrayList<String>();
		// System.out.println("处理前：" + formuaString);
		char[] formulaChars = formuaString.toCharArray();
		StringBuffer formulaBuffer = new StringBuffer();
		StringBuffer paramBuffer = new StringBuffer();
		boolean inSourceParam = false;
		boolean inFactorParam = false;

		for (char c : formulaChars) {
			if (c == '$') {
				if (inSourceParam) { // 到了变量名称结尾
					paramList.add("$" + paramBuffer.toString());
					inSourceParam = false;
					// formulaBuffer.append('$').append(paramList.size() - 1).append('$');
					formulaBuffer.append('$');
				} else { // 到了变量开始
					inSourceParam = true;
					paramBuffer = new StringBuffer();
				}

			} else if (c == '#') {
				if (inFactorParam) { // 到了变量名称结尾
					paramList.add("#" + paramBuffer.toString());
					facs.add(paramBuffer.toString());
					inFactorParam = false;
					// formulaBuffer.append('$').append(paramList.size() - 1).append('$');
					formulaBuffer.append('$');
				} else { // 到了变量开始
					inFactorParam = true;
					paramBuffer = new StringBuffer();
				}

			} else {
				if (inSourceParam || inFactorParam) {
					paramBuffer.append(c);
				} else {
					formulaBuffer.append(c);
				}
			}
		}
		formuaString = formulaBuffer.toString();
		expressionParamName = new String[paramList.size()];
		expressionParamName = paramList.toArray(expressionParamName);
		factorsName = facs.toArray(new String[facs.size()]);
	}

	public void setEmissionFactors(Map<String, EmissionFactor> factorMap) {
		this.factorMap = factorMap;
		factors = new EmissionFactor[factorsName.length];
		for (int index = 0; index < factorsName.length; index++) {
			factors[index] = factorMap.get(factorsName[index]);
		}
		return;
	}

	public EmissionCalResult cal(SourceData sourceData) {
		// 准备好参数数据
		SourceMetaData meta = sourceData.getMetaData();
		double[][] param = new double[meta.getLineNum()][expressionParamName.length];

		for (int paramIndex = 0; paramIndex < expressionParamName.length; paramIndex++) {
				String paramName = expressionParamName[paramIndex];
				double paramValue = 0;
				 if (paramName.charAt(0) == '#') {
					// 因子数据
					 paramValue = factorMap.get(paramName.substring(1)).getValue();
					 for(int lineNum=0;lineNum<meta.getLineNum();lineNum++) {
						 param[lineNum][paramIndex] = paramValue;
					 }
				}
				 else if (paramName.charAt(0) == '$') {
					 //源数据
					 int soueItemIndex = meta.getItemIndex(paramName.substring(1));
					 for(int lineNum=0;lineNum<meta.getLineNum();lineNum++) {
						param[lineNum][paramIndex] = sourceData.getMatrix()[lineNum][soueItemIndex];
					 }
				 }			
		}
		//开始计算
		FormulaEngine engine =FormulaEngine.getFormulaEngine(formuaString);
		double[] result=engine.exec(formuaString, param);
		/**
		for(int i = 0 ;i < param.length;i++) {
			for(int j=0;j<param[0].length;j++) {
				System.out.print(param[i][j]);
				System.out.print("   ");
			}			
			System.out.println(result[i]);
		}
		**/
		
		//把结果封装成EmissionCalResult
		EmissionCalResult calResult = new  EmissionCalResult(item);
		for(int index = 0; index< result.length; index++) {
			calResult.addRecord(item.getEmissionType(), item.getUnit(), result[index]);
		}
		Map<String,Object> information = new HashMap<String,Object>();
		information.put("EmissionFactor",factors);
		calResult.setInformation(information);
		return calResult;
	}

	public static void main(String[] args) {
		//test1();
		//test2();
	}



}
