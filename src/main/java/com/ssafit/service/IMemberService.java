package com.ssafit.service;

import java.util.List;

import com.ssafit.dto.History;
import com.ssafit.dto.Member;
import com.ssafit.dto.Result;

public interface IMemberService {

	Member login(Member member);

	int getMemberid(String email);

	List<String> getStretchingDate(int memberid);

	String getLatestDate(History latest);

	int getRanking(int memberid);

	Result getScore(int memberid);

	int getMemberStretchingCnt(int memberid);

}
