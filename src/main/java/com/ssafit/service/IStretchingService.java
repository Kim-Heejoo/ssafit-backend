package com.ssafit.service;

import java.util.List;

import com.ssafit.dto.Result;
import com.ssafit.dto.Video;
import com.ssafit.dto.VideoPoint;

public interface IStretchingService {

	Video getVideo(int video_id);

	List<Video> getAllVideoList();

	List<String> getVideoPart(int video_id);

	void setResult(Result result);

	int getTodayStretchingCnt();

	int getStretchingMem();

	int getStretchingTime(int video_id);

	List<Integer> getVideoList();

	int getVideoStretchingCnt(int video_id);

	VideoPoint getVideoPoint(int video_id);

}
