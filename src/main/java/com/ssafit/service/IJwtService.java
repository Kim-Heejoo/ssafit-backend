package com.ssafit.service;

import java.util.Map;

public interface IJwtService {

	<T> String create(String key, T data, String subject);

	boolean isUsable(String jwt);

	Map<String, Object> get(String key);

	int getMemberid();
}
