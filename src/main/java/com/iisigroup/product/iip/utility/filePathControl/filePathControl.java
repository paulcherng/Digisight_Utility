package com.iisigroup.product.iip.utility.filePathControl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.client.CustomFunctionResolver;

public class filePathControl {
	public static Schema OutputSchema = null;

	public static void main(String[] args) {
		for(String s:unexpList(3, "yyyyMMdd")) {
			System.out.println(s);
		}
		System.out.println("-------------------------------------");
		for (String s : getExpireFile(unexpList(3, "yyyyMMdd"), "C:/DigiSight", "KD_ResistanceM")) {
			System.out.println(s);
		}
		System.out.println("-------------------------------------");
		for(String s:getAllFile("C:/DigiSight", "KD_CombinationM")) {
			System.out.println(s);
		}
	}

	@CustomFunctionResolver("FolderCreateResolver")
	public static List<String> folderCreate(String rootPath, String SrcID, String year, String month) {

//		Tuple dataTuple=OutputSchema.createTuple();
//		List<String> tupleList=new ArrayList<String>();
		List<String> resultList = new ArrayList<String>();

		String FolderPath = rootPath + "/" + SrcID;
		if (!new File(FolderPath).exists()) {
			if (new File(FolderPath).mkdir()) {
				String tmp = FolderPath + "-----success";
				resultList.add(tmp);
			} else {
				String tmp = FolderPath + "-----failed";
				resultList.add(tmp);
			}
		} else {
			resultList.add(FolderPath + "------exist");
		}
		String nextMonth = "";
		String nextYear = year;
		if (Integer.parseInt(month) == 12) {
			nextMonth = "1";
			nextYear = (Integer.parseInt(year) + 1) + "";
		} else
			nextMonth = (Integer.parseInt(month) + 1)+"";
		String thisFolderPath = FolderPath + "/" + year;
		String nextFolderPath = FolderPath + "/" + nextYear;
		;
		if (!new File(thisFolderPath).exists()) {
			if (new File(thisFolderPath).mkdir()) {
				String tmp = thisFolderPath + "/-----success";
				resultList.add(tmp);
			} else {
				String tmp = thisFolderPath + "/-----failed";
				resultList.add(tmp);
			}
		} else {
			resultList.add(thisFolderPath + "------exist");
		}
		if (month.equals("12")) {
			if (!new File(nextFolderPath).exists()) {
				if (new File(nextFolderPath).mkdir()) {
					String tmp = nextFolderPath + "/-----success";
					resultList.add(tmp);
				} else {
					String tmp = nextFolderPath + "/-----failed";
					resultList.add(tmp);
				}
			} else {
				resultList.add(nextFolderPath + "------exist");
			}
		} else {
			nextFolderPath = FolderPath + "/" + nextYear;
		}
		thisFolderPath += "/" +month;
		nextFolderPath += "/" +nextMonth;
		if (!new File(thisFolderPath).exists()) {
			if (new File(thisFolderPath).mkdir()) {
				resultList.add(thisFolderPath + "-----success");
			} else {
				resultList.add(thisFolderPath + "-----failed");
			}
		} else {
			resultList.add(thisFolderPath + "------exist");
		}
		if (!new File(nextFolderPath).exists()) {
			if (new File(nextFolderPath).mkdir()) {
				resultList.add(nextFolderPath + "-----success");
			} else {
				resultList.add(nextFolderPath + "-----failed");
			}
		} else {
			resultList.add(nextFolderPath + "------exist");
		}
		return resultList;
	}

	public static CompleteDataType FolderCreateResolver(CompleteDataType arg1, CompleteDataType arg2,
			CompleteDataType arg3, CompleteDataType arg4) {
//		ArrayList<Schema.Field> Fields = new ArrayList<Schema.Field>();
//		Fields.add(Schema.createListField("resultList",CompleteDataType.forString()));
//		OutputSchema = new Schema(null,Fields.get(0));
		return CompleteDataType.forStringList();
	}

	@CustomFunctionResolver("fileScan")
	public static List<String> getFileList(String rootPath, String SrcID, Integer StartYear, Integer StartMonth,
			Integer StartDay, Integer StartHour, Integer EndYear, Integer EndMonth, Integer EndDay, Integer EndHour) {
		List<String> existFileList = new ArrayList<String>();
		List<String> tmpList = new ArrayList<String>();
		rootPath = rootPath.replace("\\", "/");
		Calendar Start = Calendar.getInstance();
		Calendar End = Calendar.getInstance();
		DateFormat dateParser = null;
		if (StartHour != null && EndHour != null) {
			Start = withHourCalender(StartYear, StartMonth, StartDay, StartHour);
			End = withHourCalender(EndYear, EndMonth, EndDay, EndHour);
			dateParser = new SimpleDateFormat("yyyyMMdd(HH)");
			while (Start.before(End)) {
				tmpList.add(rootPath + "/" + SrcID + "/" + Start.get(Calendar.YEAR) + "/" +(Start.get(Calendar.MONTH) + 1) + "/"
						+ dateParser.format(Start.getTime()) + ".csv");
				Start.add(Calendar.HOUR, 1);
//				System.out.println(tmpList);
			}
		} else {
			Start = withoutHourCalender(StartYear, StartMonth, StartDay);
			End = withoutHourCalender(EndYear, EndMonth, EndDay);
			dateParser = new SimpleDateFormat("yyyyMMdd");
			while (Start.before(End)) {
				tmpList.add(rootPath + "/" + SrcID + "/" + Start.get(Calendar.YEAR) + "/" + (Start.get(Calendar.MONTH) + 1) + "/"
						+ dateParser.format(Start.getTime()) + ".csv");
				Start.add(Calendar.DATE, 1);
			}
		}
		for (String result : tmpList) {
//			System.out.println(result);
			if (new File(result).exists()) {
				existFileList.add(result);
			}
		}
		return existFileList;
	}

	private static Calendar withHourCalender(Integer Year, Integer Month, Integer Day, Integer Hour) {
		Calendar cal = Calendar.getInstance();
		String dateString = "" + Year + String.format("%02d", Month) + String.format("%02d", Day) + "("
				+ String.format("%02d", Hour) + ")";
		DateFormat dateParser = new SimpleDateFormat("yyyyMMdd(HH)");
		try {
			cal.setTime(dateParser.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}

	private static Calendar withoutHourCalender(Integer Year, Integer Month, Integer Day) {
		Calendar cal = Calendar.getInstance();
		String dateString = "" + Year + String.format("%02d", Month) + String.format("%02d", Day);
		;
		DateFormat dateParser = new SimpleDateFormat("yyyyMMdd");
		try {
			cal.setTime(dateParser.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}

	public static CompleteDataType fileScan(CompleteDataType arg1, CompleteDataType arg2, CompleteDataType arg3,
			CompleteDataType arg4, CompleteDataType arg5, CompleteDataType arg6, CompleteDataType arg7,
			CompleteDataType arg8, CompleteDataType arg9, CompleteDataType arg10) {
		return CompleteDataType.forStringList();
	}

	public static List<String> purgeFolder(List<String> target) {
		List<String> purgeFileList = new ArrayList<String>();
		for (String path : target) {
			if (new File(path).delete()) {
				purgeFileList.add(path + "------deleted");
			} else {
				purgeFileList.add(path + "------delete failed");
			}
		}
		return purgeFileList;

	}

	@CustomFunctionResolver("getUnexpFileList")
	public static List<String> unexpList(int dayRange, String dateFormat) {
		List<String> result = new ArrayList<String>();
		Calendar before = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		before.add(Calendar.DATE, dayRange - (dayRange * 2));
		DateFormat dateParser = new SimpleDateFormat(dateFormat);
		result.add(dateParser.format(now.getTime()));
		if (dateFormat.contains("H")) {
			while (before.before(now)) {
				result.add(dateParser.format(before.getTime()));
				before.add(Calendar.HOUR, 1);
//				System.out.println(tmpList);
			}
		} else {
			while (before.before(now)) {
				result.add(dateParser.format(before.getTime()));
				before.add(Calendar.DATE, 1);
//				System.out.println(tmpList);
			}
		}
		return result;
	}

	public static CompleteDataType getUnexpFileList(CompleteDataType arg1, CompleteDataType arg2) {
		return CompleteDataType.forStringList();
	}

	public static List<String> getAllFile(String rootPath, String SrcID) {
		List<String> result = new ArrayList<String>();
		rootPath = rootPath.replace("\\", "/");
		String FolderPath = rootPath + "/" + SrcID;
		if (new File(FolderPath).isDirectory()) {
//			System.out.println("FolderPath");
			for (File year : new File(FolderPath).listFiles()) {
				if (year.isDirectory()
						&& Integer.parseInt(year.getName()) <= Calendar.getInstance().get(Calendar.YEAR)) {
//					System.out.println("year");
					for (File yearMonth : new File(year.getAbsolutePath()).listFiles()) {
//						System.out.println(yearMonth);
//						System.out.println(Integer.parseInt(yearMonth.getName()));
//						System.out.println(Calendar.getInstance().get(Calendar.MONTH)+1);
						if (yearMonth.isDirectory()
								&& Integer.parseInt(yearMonth.getName()) <= Calendar.getInstance().get(Calendar.MONTH)+1) {
//							System.out.println("yearMonth");
							for (File absolute : new File(yearMonth.getAbsolutePath()).listFiles()) {
								result.add(absolute.getAbsolutePath());
							}
							result.add(yearMonth.getAbsolutePath());
						}
					}
					result.add(year.getAbsolutePath());
				}
			}
		}
		return result;
	}

	@CustomFunctionResolver("getExpFileList")
	public static List<String> getExpireFile(List<String> unexpFile, String rootPath, String SrcID) {
		List<String> result = getAllFile(rootPath, SrcID);
		List<String> finalResult = getAllFile(rootPath, SrcID);
		List<String> deleteResult = new ArrayList<String>();
		for (String s : result) {
//			System.out.println(s);
			for (String f : unexpFile) {
//				System.out.println("f="+f);
//				System.out.println(s.contains(f));
				if (s.contains(f)) {
//					System.out.println(s);
					finalResult.remove(s);
				}
			}
//			System.out.println("--------------------------");
		}
//		for (String s : finalResult) {
//			System.out.println("s=" + s);
//		}
		for (String s : finalResult) {
			if (new File(s).delete()) {
				deleteResult.add(s + "-------------delete success");
			} else {
				deleteResult.add(s + "-------------delete failed");
			}
		}
		return deleteResult;

	}

	public static CompleteDataType getExpFileList(CompleteDataType arg1, CompleteDataType arg2, CompleteDataType arg3) {
		return CompleteDataType.forStringList();
	}
}
