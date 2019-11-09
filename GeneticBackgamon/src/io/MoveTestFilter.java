package io;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import BG.RulesNew;
import algorithm.MoveTest;


public class MoveTestFilter {

	public List<MoveTest> filter(List<MoveTest> tests) {
		
		List<MoveTest> myt = new ArrayList<MoveTest>();
		myt = tests.stream()
				.filter(test -> ! RulesNew.isHomebound(test.getInitial()))
//				.filter(test -> ! RulesNew.hasPrisoners(test.getInitial()))
			.collect(Collectors.toList());
		
		boolean skip;
		List<MoveTest> filtered = new ArrayList<MoveTest>();
		MoveTest iTest;
		MoveTest jTest;
		for (int i = 0; i < myt.size(); i++) {
			skip = false;
			for (int j=i+1; j<myt.size(); j++) {
				iTest = myt.get(i);
				jTest = myt.get(j);
				if (iTest.hasDifferentExpectation(jTest) ||
						iTest.same(jTest)) {
					skip = true;
					break;
				}
			}
			if (skip == false) {
				filtered.add(myt.get(i));
			}
		}
		return filtered;
	}
}
