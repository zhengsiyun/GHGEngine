package com.elecwatt.ghg.engine.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public interface FormulaEngine {
	/**
	 * 公式计算器
	 * 
	 * @param expression    公式字符串，格式为 函数名(可选) 四则运算公式
	 * @param paramValues      参数数组。参数格式为 [源数据项] [排放因子] [其他]
	 * @param paramValues 参数位置，依次从上诉参数数组中获取参数的位置，如[0,0],[1,1],[1,2],则表述参数依次是
	 *                   第一个源数据项，第二个因子和第三个因子
	 * @return
	 */
	// double exec (String formula,double[][] param,int paramIndex[][]);

	double[] exec(String expression, double[][] paramValues);
	/**
	 * 根据公式获得合适的公式计算引擎
	 * 考虑到主要的计算公式就是简单的连乘或连加，为保证效率，把这两个算法单独提取出来用
	 * @param expression
	 * @return
	 */
	public static FormulaEngine getFormulaEngine(String expression) {
		char[] exStr = expression.toCharArray();
		Set<Character> legalCh = new HashSet<Character>();
		legalCh.add('(');
		legalCh.add(')');
		legalCh.add('+');
		legalCh.add('-');
		legalCh.add('*');
		legalCh.add('/');
		legalCh.add(' ');
		legalCh.add('$');
		boolean onlyMUL = true;
		boolean onlyADD = true;
		for(char ch:exStr) {
			if(!legalCh.contains(ch)) {
				return null;
			}
			if(onlyMUL) {
				if(ch != '$' && ch!=' ' && ch!='*') {
					onlyMUL = false;
				}
			}
			if(onlyADD) {
				if(ch != '$' && ch!=' ' && ch!='+') {
					onlyADD = false;
				}
			}
		}
		if(onlyMUL) {
			return new MULFunction();
		}
		if(onlyADD) {
			return new SUMFunction();
		}
		
		return new ExpressionFunction();
	}

	/**
	 * 连乘算法，碳排放中最常用的计算方法，可以解决大部分计算问题 该方法不需要其他计算公式表达式了，就相当于一个简单的函数调用
	 * 
	 * @author Surface
	 *
	 */
	public static class MULFunction implements FormulaEngine {

		@Override
		public double[] exec(String expression,  double[][] paramValues) {
			double[] results = new double[paramValues.length];
			for (int index = 0; index < results.length; index++) {
				double result = 1;
				for (double param : paramValues[index]) {
					result *=param;
				}
				results[index] = result;
			}
			return results;
		}
	}

	/**
	 * 连加算法， 该方法不需要其他计算公式表达式了，就相当于一个简单的函数调用
	 * 
	 * @author Surface
	 *
	 */
	public static class SUMFunction implements FormulaEngine {

		@Override
		public double[] exec(String expression,  double[][] paramValues) {
			double[] results = new double[paramValues.length];
			for (int index = 0; index < results.length; index++) {
				double result = 0;
				for (double param : paramValues[index]) {
					result += param;
				}
				results[index] = result;
			}
			return results;
		}
	}

	/**
	 * 这个是最通用的表达式计算方法
	 * 
	 * @author Surface
	 *
	 */
	public static class ExpressionFunction implements FormulaEngine {
		/**
		 * 公式中用$表示变量，变量出现的顺序就是paramValues中列的顺序
		 * @param postfixExpression
		 * @param paramValues
		 * @return
		 */
		public double[] postExpressionCal(String postfixExpression, double[][] paramValues) {
			Stack<double[]> stack = new Stack<double[]>();
			char[] exlist =  postfixExpression.toCharArray();
			//每次输入数据是N行参数，对每一行参数用同样的规则计算
			int paramLines = paramValues.length;
			//参数的顺序，对应paramValues中的列
			int paramIndex=-1;
	        for(int index=0; index<exlist.length; index++){
	            char item = exlist[index];	            
	            if(item=='$'){
	                //是变量
	            	paramIndex++;
	            	//这时取到所有该参数列数据
	            	double[] params = new double[paramLines];
	            	for(int line =0; line <paramLines;  line++) {
	            		params[line] = paramValues[line][paramIndex];
	            	}
	                stack.push(params);
	            }else {
	                //是操作符，取出栈顶两个元素
	                double[] num2 = stack.pop();
	                double[] num1 = stack.pop();
	                double[] res = new double[paramLines];
	                if(item=='+'){
	                	for(int resultIndex = 0; resultIndex<paramLines; resultIndex++) {
	                		res[resultIndex] = num1[resultIndex]+num2[resultIndex];
	                	}

	                }else if(item=='-'){
	                	for(int resultIndex = 0; resultIndex<paramLines; resultIndex++) {
	                		res[resultIndex] = num1[resultIndex]-num2[resultIndex];
	                	}
	                }else if(item=='*'){
	                	for(int resultIndex = 0; resultIndex<paramLines; resultIndex++) {
	                		res[resultIndex] = num1[resultIndex]*num2[resultIndex];
	                	}
	                }else if(item=='/'){
	                	for(int resultIndex = 0; resultIndex<paramLines; resultIndex++) {
	                		res[resultIndex] = num1[resultIndex]/num2[resultIndex];
	                	}
	                }else {
	                    throw new RuntimeException("运算符错误！");
	                }
	                stack.push(res);
	            }
	        }
	        return stack.pop();			
		}
		@Override
		public double[] exec(String expression, double[][] paramValues) {
			// 第一步把expression转换成后缀表达式
			InToPost theTrans = new InToPost(expression);
			String postfixExpression = theTrans.doTrans(); 
			return postExpressionCal(postfixExpression,paramValues);
		}
		
		public static void main(String[] args) {
			String expression = "$+$+$";
			double[][] paramValues = new double[10][3];
			for(int index=0;index<paramValues.length;index++) {
				for(int cow=0;cow<paramValues[0].length;cow++) {
					paramValues[index][cow] = index;
				}
			}
			FormulaEngine function =FormulaEngine.getFormulaEngine(expression);
			System.out.println(function);
			double[] result = function.exec(expression, paramValues);
			System.out.println("测试");
			for(int index=0;index<result.length;index++) {
				System.out.println(result[index]);
			}
			
		}

	}

	
}
