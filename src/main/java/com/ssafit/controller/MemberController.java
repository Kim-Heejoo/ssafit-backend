package com.ssafit.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafit.dto.History;
import com.ssafit.dto.Member;
import com.ssafit.dto.Result;
import com.ssafit.dto.Video;
import com.ssafit.service.IJwtService;
import com.ssafit.service.IMemberService;
import com.ssafit.service.IStretchingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000, exposedHeaders = "Authorization", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
@Api(value = "Stretching", description = "Resouces Management 2020")
public class MemberController {

	public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private IMemberService memberservice;
	@Autowired
	private IJwtService jwtService;
	@Autowired
	private IStretchingService stretchingservice;
	
	@ApiOperation(value = "로그인", response = Member.class)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody Member member) throws Exception {
		logger.info("1-------------login-----------------------------" + new Date());
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> resultMap = new HashMap<>();
		
		Member login = memberservice.login(member);
		
		if(login==null)
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		
		String token = jwtService.create("member", login, "login");
		headers.set("Authorization", token);
		
		resultMap.put("memberid", login.getMemberid());
		resultMap.put("email", login.getEmail());
		resultMap.put("name",login.getName()); 

		return new ResponseEntity<Map<String, Object>>(resultMap, headers, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "마이페이지-점수별 개수", response = Member.class)
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> mypage() throws Exception {
		logger.info("1-------------mypage-----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		int memberid = jwtService.getMemberid();
		
		//calendar : 이번달 스트레칭한 날짜
		List<String> stretching_date = memberservice.getStretchingDate(memberid);
		resultMap.put("stretching_date", stretching_date);
		
		//history : 동영상 기록
		List<Video> list = stretchingservice.getAllVideoList();
		PriorityQueue<History> pq = new PriorityQueue<>();
		
		for (int i = 0; i < list.size(); i++) {
			History latest = new History(list.get(i).getVideo_id(), memberid);
			String latestDate = memberservice.getLatestDate(latest);
			if(latestDate==null) continue;
			latest.setDate(latestDate);
			pq.add(latest);
		}
		
		int size = pq.size();
		Map<String, Object>[] history = new HashMap[pq.size()];
		for (int i = 0; i < size; i++) {
			History last = pq.poll();

			Video video = stretchingservice.getVideo(last.getVideo_id());
			
			history[i] = new HashMap<>();
			history[i].put("video_id", last.getVideo_id());
			history[i].put("date", last.getDate());
			history[i].put("title", video.getTitle());
			history[i].put("thumbnail", video.getThumbnail());
			history[i].put("description", video.getDescription());
		}
		
		resultMap.put("history", history);
		
		//ranking : 점수 순위
		Map<String, Object> ranking = new HashMap<>();
		
		int total_users = stretchingservice.getStretchingMem()-1;
		ranking.put("total_users", total_users);
		
		int cnt = memberservice.getMemberStretchingCnt(memberid);
		if(cnt==0) {
			ranking.put("rank", total_users);			
		}else {
			ranking.put("rank", memberservice.getRanking(memberid)-1);
		}
		
		resultMap.put("ranking", ranking);
		
		//score : 내 점수 통계
		Map<String, Object> score = new HashMap<>();
		Result myScore = memberservice.getScore(memberid);
		if(myScore==null) {
			score.put("excellent", 0);
			score.put("great", 0);
			score.put("good", 0);
			score.put("bad", 0);
		}else {
			score.put("excellent", myScore.getExcellent());
			score.put("great", myScore.getGreat());
			score.put("good", myScore.getGood());
			score.put("bad", myScore.getBad());
		}
		resultMap.put("score", score);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
}
