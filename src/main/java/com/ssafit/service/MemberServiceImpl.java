package com.ssafit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafit.dao.MemberDaoImpl;
import com.ssafit.dto.History;
import com.ssafit.dto.Member;
import com.ssafit.dto.Result;


@Service
public class MemberServiceImpl implements IMemberService {
	
	@Autowired
	private MemberDaoImpl memberdao;


	@Override
	@Transactional(readOnly=true)
	public Member login(Member member) {
		return memberdao.login(member);
	}


	@Override
	@Transactional(readOnly=true)
	public int getMemberid(String email) {
		return memberdao.getMemberid(email);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<String> getStretchingDate(int memberid) {
		return memberdao.getStretchingDate(memberid);
	}


	@Override
	@Transactional(readOnly=true)
	public String getLatestDate(History latest) {
		return memberdao.getLatestDate(latest);
	}


	@Override
	@Transactional(readOnly=true)
	public int getRanking(int memberid) {
		return memberdao.getRanking(memberid);
	}


	@Override
	@Transactional(readOnly=true)
	public Result getScore(int memberid) {
		return memberdao.getScore(memberid);
	}


	@Override
	@Transactional(readOnly=true)
	public int getMemberStretchingCnt(int memberid) {
		return memberdao.getMemberStretchingCnt(memberid);
	}



}
