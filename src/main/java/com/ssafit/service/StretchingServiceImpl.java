package com.ssafit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafit.dao.StretchingDaoImpl;
import com.ssafit.dto.Result;
import com.ssafit.dto.Video;
import com.ssafit.dto.VideoPoint;


@Service
public class StretchingServiceImpl implements IStretchingService {
	
	@Autowired
	private StretchingDaoImpl stretchingdao;


	@Override
	@Transactional(readOnly=true)
	public Video getVideo(int video_id) {
		return stretchingdao.getVideo(video_id);
	}


	@Override
	@Transactional(readOnly=true)
	public List<Video> getAllVideoList() {
		return stretchingdao.getAllVideoList();
	}


	@Override
	@Transactional(readOnly=true)
	public List<String> getVideoPart(int video_id) {
		return stretchingdao.getVideoPart(video_id);
	}


	@Override
	@Transactional()
	public void setResult(Result result) {
		stretchingdao.setResult(result);
	}


	@Override
	@Transactional(readOnly=true)
	public int getTodayStretchingCnt() {
		return stretchingdao.getTodayStretchingCnt();
	}


	@Override
	@Transactional(readOnly=true)
	public int getStretchingMem() {
		return stretchingdao.getStretchingMem();
	}


	@Override
	@Transactional(readOnly=true)
	public int getStretchingTime(int video_id) {
		return stretchingdao.getStretchingTime(video_id);
	}


	@Override
	@Transactional(readOnly=true)
	public List<Integer> getVideoList() {
		return stretchingdao.getVideoList();
	}


	@Override
	@Transactional(readOnly=true)
	public int getVideoStretchingCnt(int video_id) {
		return stretchingdao.getVideoStretchingCnt(video_id);
	}


	@Override
	@Transactional(readOnly=true)
	public VideoPoint getVideoPoint(int video_id) {
		return stretchingdao.getVideoPoint(video_id);
	}

}
