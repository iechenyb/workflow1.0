package com.kiiik.pub;

import java.util.Arrays;
import java.util.List;

public class FakeLdapService {
	//<userTask id="task" name="My Task" activiti:assignee="${ldapService.findManagerForEmployee(emp)}"/>
	public String findManagerForEmployee(String employee) {
		return "Kermit The Frog";
	}
    //<userTask id="task" name="My Task" activiti:candidateUsers="${ldapService.findAllSales()}"/>
	public List<String> findAllSales() {
		return Arrays.asList("kermit", "gonzo", "fozzie");
	}
}
