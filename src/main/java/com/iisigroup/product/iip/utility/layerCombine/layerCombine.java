package com.iisigroup.product.iip.utility.layerCombine;

import java.util.ArrayList;

import com.streambase.sb.CompleteDataType;
import com.streambase.sb.client.CustomFunctionResolver;

public class layerCombine {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ArrayList<String> GL=new ArrayList<String>();
//		ArrayList<String> DL=new ArrayList<String>();
//		GL.add("病床");
//		GL.add("病房");
//		GL.add("大樓");
//		GL.add("院區");
//		GL.add("縣市");
//		GL.add("國家");
//		DL.add("A");
//		DL.add("B");
//		DL.add("C");
//		DL.add("D");
//		DL.add("E");
//		DL.add("F");
//		DL.add("G");
//		System.out.println(combineSrc(GL,DL));
//	}
	@CustomFunctionResolver("SrcCombine")
	public static String combineSrc(ArrayList<String> GroupList, ArrayList<String> DefineList) {
		ArrayList<String> CorrectGList = new ArrayList<String>();
		String result="";
		for(String s:GroupList) {
			if(s.length()>0) {
				CorrectGList.add(s);
			}
		}
		for(int i=0;i<CorrectGList.size();i++) {
			result+=CorrectGList.get(i)+"："+DefineList.get(i);
			if(i<(CorrectGList.size()-1)) {
				result+=", ";
			}
		}
		return result;
	}
	public static CompleteDataType SrcCombine(CompleteDataType arg1, CompleteDataType arg2) {
		return CompleteDataType.forString();
	}

}
