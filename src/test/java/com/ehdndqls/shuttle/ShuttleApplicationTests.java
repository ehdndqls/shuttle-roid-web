package com.ehdndqls.shuttle;

import com.ehdndqls.shuttle.busserver.BusService;
import com.ehdndqls.shuttle.courses.CourseId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ShuttleApplicationTests {

	@Autowired
	private BusService busService;

	@Test
	public void testGetBusData() {
		Integer testOrgId = 1;
		Map<String, Object> result = busService.getBusData(testOrgId);

		// 결과 출력
		System.out.println(result);

		// 예시: stopList, routeList가 비어있지 않은지 확인
		assertFalse(((List<?>) result.get("stopList")).isEmpty());
		assertFalse(((List<?>) result.get("routeList")).isEmpty());
	}

	@Test
	public void testGetCourseData() {
		Integer testOrgId = 1;
		Integer testDriverId = 9;
		CourseId courseId = new CourseId(testOrgId, testDriverId);
		List<Map<String, Object>> result = busService.getCourseData(courseId);

		System.out.println(result);

	}
}