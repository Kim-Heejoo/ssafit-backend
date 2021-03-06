package com.ssafit.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

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

import com.ssafit.dto.Member;
import com.ssafit.dto.Part;
import com.ssafit.dto.Result;
import com.ssafit.dto.Video;
import com.ssafit.dto.VideoPoint;
import com.ssafit.service.IJwtService;
import com.ssafit.service.IStretchingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000, exposedHeaders = "Authorization", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
@Api(value = "Stretching", description = "Resouces Management 2020")
public class StretchingController {

	public static final Logger logger = LoggerFactory.getLogger(StretchingController.class);

	@Autowired
	private IStretchingService stretchingservice;
	@Autowired
	private IJwtService jwtService;

	@ApiOperation(value = "스트레칭 코치 비디오 리스트", response = Null.class)
	@RequestMapping(value = "/stretching", method = RequestMethod.GET)
	public ResponseEntity<List<Video>> stretchingList() throws Exception {
		logger.info("1-------------stretchingList-----------------------------" + new Date());
		
		List<Video> video = stretchingservice.getAllVideoList();
		for (int i = 0; i < video.size(); i++) {
			List<String> part = stretchingservice.getVideoPart(video.get(i).getVideo_id());
			video.get(i).setPart(part);
		}

		return new ResponseEntity<List<Video>>(video, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "스트레칭 디테일", response = Integer.class)
	@RequestMapping(value = "/stretching/detail/{video_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> detail(@PathVariable int video_id, HttpServletRequest rs, HttpServletResponse hr) throws Exception {
		logger.info("1-------------detail-----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		
		Video stretching = stretchingservice.getVideo(video_id);
		List<Integer> video = stretchingservice.getVideoList();
		VideoPoint point = stretchingservice.getVideoPoint(video_id);
		
		//다음영상
		String path = "/detail/";
		if(video_id==2)
			path += 4;
		else if(video_id==4)
			path += 6;
		else {
			for (int i = 0; i < video.size(); i++) {
				if (video.get(i) == video_id) {
					if (i < video.size() - 1) {
						path += video.get(i + 1);
						break;
					} else {
						path = "/main";
					}
				}
			}
		}
		
		resultMap.put("video_id", stretching.getVideo_id());
		resultMap.put("title", stretching.getTitle());
		resultMap.put("file", stretching.getFile());
		resultMap.put("description", stretching.getDescription());
		resultMap.put("time", stretching.getTime());
		resultMap.put("next", path);
		resultMap.put("first_stop", point.getFirst_stop());
		resultMap.put("second_stop", point.getSecond_stop());
		resultMap.put("excellent", point.getExcellent());
		resultMap.put("great", point.getGreat());
		resultMap.put("good", point.getGood());

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "스트레칭 결과 페이지(결과 저장)", response = Map.class)
	@RequestMapping(value = "/stretching/result", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> result(@RequestBody Result result) throws Exception {
		logger.info("1-------------result-----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		
		int memberid = jwtService.getMemberid();
		if(memberid != result.getMember_id()) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.UNAUTHORIZED);
		}
		
		stretchingservice.setResult(result);
		resultMap.put("status", "ok");
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "소개", response = Null.class)
	@RequestMapping(value = "/intro", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> intro() throws Exception {
		logger.info("1-------------intro-----------------------------" + new Date());
		Map<String, Object> resultMap = new HashMap<>();
		
		int total_cnt = stretchingservice.getTodayStretchingCnt();
		int total_users = stretchingservice.getStretchingMem()-1;
		List<Integer> video = stretchingservice.getVideoList();
		int total_time = 0;
		for (int i = 0; i < video.size(); i++) {
			total_time += stretchingservice.getStretchingTime(video.get(i)) * stretchingservice.getVideoStretchingCnt(video.get(i));
		}
		
		resultMap.put("total_cnt", total_cnt);
		resultMap.put("total_users", total_users);
		resultMap.put("total_time", total_time);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}


//	@ApiOperation(value = "스트레칭 코치 비디오(파일)", response = Integer.class)
//	@RequestMapping(value="/contents/{num}", method= RequestMethod.GET) 
//	public String getContentMediaVideo(@PathVariable int num,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
//		
//		Video stretching = stretchingservice.getVideoPath(num);
//		File file = new File("/aaa/"+ stretching.getFile());
//
//		RandomAccessFile randomFile = new RandomAccessFile(file, "r");
//		long rangeStart = 0;
//		long rangeEnd = 0;
//		boolean isPart=false;
//		try{
//			long movieSize = randomFile.length();
//			String range = request.getHeader("range");
//			if(range!=null) {
//				if(range.endsWith("-")){
//					range = range+(movieSize - 1);
//
//				}
//				int idxm = range.trim().indexOf("-");
//				rangeStart = Long.parseLong(range.substring(6,idxm));
//				rangeEnd = Long.parseLong(range.substring(idxm+1));
//				if(rangeStart > 0){
//					isPart = true;
//				}
//			}else{
//				 rangeStart = 0;
//				 rangeEnd = movieSize - 1;
//			}
//			long partSize = rangeEnd - rangeStart + 1;
//			response.reset();
//			response.setStatus(isPart ? 206 : 200);
//			response.setContentType("video/mp4");
//			response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+movieSize); 
//			response.setHeader("Accept-Ranges", "bytes"); 
//			response.setHeader("Content-Length", ""+partSize);
//			OutputStream out = response.getOutputStream();
//			randomFile.seek(rangeStart);
//			int bufferSize = 8*1024;
//			byte[] buf = new byte[bufferSize];
//			do{
//				int block = partSize > bufferSize ? bufferSize : (int)partSize; 
//				int len = randomFile.read(buf, 0, block); 
//				out.write(buf, 0, len); 
//				partSize -= block;
//			}while(partSize > 0);
//		}catch(IOException e){
//			
//		}finally{ 
//			randomFile.close(); 
//		} return null;
//	}


}
