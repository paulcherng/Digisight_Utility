package com.iisigroup.product.iip.utility.ruleControl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RuleMapping {
	private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	public List<rule> cacheMappingTable;
	public static final String SRCID = "SrcID";
	public static final String SNSRTYPE = "SnsrType";
	public static final String SNSRID = "SnsrID";
	public Map<String, Map<String, BitSet>> mappingTable;
	public List<rule> RuleList = new ArrayList<rule>();
	
	public int clearRuleList() {
		RuleList.clear();
		return RuleList.size();
	}
	public String setRuleList(String SrcID, String SnsrType, String SnsrID, String Rule) {
		rule newRule = new rule();
		newRule.setSrcID(SrcID);
		newRule.setSnsrType(SnsrType);
		newRule.setSnsrID(SnsrID);
		newRule.setRule(Rule);
		System.out.println(newRule);
		RuleList.add(newRule);
		setMappingTable(RuleList);
		String result = "";
		for (int i = 0; i < RuleList.size(); i++) {
			result += "{";
			result += RuleList.get(i).getSrcID() + ",";
			result += RuleList.get(i).getSnsrType() + ",";
			result += RuleList.get(i).getSnsrID() + ",";
			result += RuleList.get(i).getRule() + "}";
			if(i!=RuleList.size()-1)
				result+=",";
		}
		return result;
	}

	public void setMappingTable(List<rule> tmpRules) {
		readWriteLock.writeLock().lock();
		try {
			/**
			 * Process begin to set mapping table
			 */
			mappingTable = new ConcurrentHashMap<>();

			cacheMappingTable = new ArrayList<>();
			if (tmpRules == null) {
				/**
				 * No mapping table data and return null
				 */
				return;
			}
			for (int i = 0; i < tmpRules.size(); i++) {
				rule rule = tmpRules.get(i);
				initMap(SRCID, rule.getSrcID(), i);
				initMap(SNSRTYPE, rule.getSnsrType(), i);
				initMap(SNSRID, rule.getSnsrID(), i);
				cacheMappingTable.add(rule);
//	                System.out.println(cacheMappingTable.get(i).getRule());
			}
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	private void initMap(String col, String key, int index) {
		if (!mappingTable.containsKey(col))
			mappingTable.put(col, new HashMap<String, BitSet>());

		Map<String, BitSet> colMap = mappingTable.get(col);

		if (!colMap.containsKey(key))
			colMap.put(key, new BitSet());

		colMap.get(key).set(index);
	}

	private BitSet doAnd(BitSet bitSet, BitSet bitSet2) {
		if (bitSet == null || bitSet2 == null)
			return null;
//	        System.out.println(bitSet+","+bitSet2);
		bitSet.and(bitSet2);
		return bitSet;
	}

	public String getMatchMappingTable(String SrcID, String SnsrType, String SnsrID) {
		readWriteLock.readLock().lock();
		rule Rule = null;
		try {
			BitSet bitSet = new BitSet();
			bitSet = getBitSet(SRCID, SrcID);
//	            System.out.println(bitSet);
			bitSet = doAnd(bitSet, getBitSet(SNSRTYPE, SnsrType));
//	            System.out.println("mid="+bitSet);
			bitSet = doAnd(bitSet, getBitSet(SNSRID, SnsrID));
//	            System.out.println("finalBitSet="+bitSet);
			int result = bitSet.nextSetBit(0);
			if (result == -1)
				return "false";
			Rule = cacheMappingTable.get(result);
//	            System.out.println(Rule.getRule());
		} finally {
			readWriteLock.readLock().unlock();
		}
		return Rule.getRule();
	}

	private BitSet getBitSet(String field, String key) {
		return getBitSet(field, key, true);
	}

	private BitSet getBitSet(String field, String key, boolean isWildcards) {
		BitSet bitSet = mappingTable.get(field).get(key);
//	        System.out.println("getBitSet1="+bitSet);
		if (bitSet != null)
			bitSet = (BitSet) bitSet.clone();

		if (isWildcards) {
			BitSet wildcards = mappingTable.get(field).get("*");
			if (wildcards != null)
				wildcards = (BitSet) wildcards.clone();
			if (bitSet != null && wildcards != null)
				bitSet.or(wildcards);
			else if (wildcards != null)
				bitSet = wildcards;
		}
//	        System.out.println("getBitSetOut="+bitSet);
		return bitSet;
	}
}
