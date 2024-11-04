package com.example.backend.etc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.common.dto.CommonResponse.ResponseWithData;
import com.example.backend.common.dto.CommonResponse.ResponseWithMessage;
import com.example.backend.etc.dto.RestaurantProfileDto;
import com.example.backend.etc.dto.RestaurantResponse.RestaurantInfo;
import com.example.backend.etc.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "가맹점 API", description = "가맹점 관련 Controller입니다")
public class RestaurantController {

	private final RestaurantService restaurantService;

	@Operation(summary = "사업장 조회", description = "사업장을 조회하는 api")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 완료"),
	})
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<ResponseWithData<RestaurantProfileDto>> getRestaurant(@PathVariable("id") Integer restaurantId,
		HttpServletRequest request) {

		String id = (String)request.getAttribute("userId");
		Integer userId = Integer.valueOf(id);

		RestaurantProfileDto restaurant = restaurantService.getRestaurant(restaurantId, userId);

		ResponseWithData<RestaurantProfileDto> responseWithData = new ResponseWithData<>(
			HttpStatus.OK.value(), "사업장 조회에 성공하였습니다.", restaurant);

		return ResponseEntity.status(responseWithData.status())
			.body(responseWithData);
	}


	@Operation(summary = "사업장 등록", description = "사업장을 등록하는 api")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 완료"),
	})
	@PostMapping("/restaurant")
	public ResponseEntity<ResponseWithMessage> createRestaurant(@RequestBody RestaurantProfileDto restaurantProfile,
		HttpServletRequest request) {

		String id = (String)request.getAttribute("userId");
		Integer userId = Integer.valueOf(id);

		restaurantService.createRestaurant(restaurantProfile,userId);

		ResponseWithMessage responseWithMessage = new ResponseWithMessage(HttpStatus.OK.value(),
			"사업장 정보 설정에 성공하였습니다");

		return ResponseEntity.status(responseWithMessage.status())
			.body(responseWithMessage);
	}


	@Operation(summary = "가맹점 위치 조회", description = "유저의 화면범위에 있는 가맹점을 조회하는 api")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 완료"),
	})
	@GetMapping("/restaurant")
	public ResponseEntity<ResponseWithData<RestaurantInfo>> getNearRestaurant(
		@RequestParam("minLat") double minLat,@RequestParam("maxLat") double maxLat,
		@RequestParam("minLng") double minLng,@RequestParam("maxLng") double maxLng,
		@RequestParam(value = "people",required = false, defaultValue = "0") int people,
		@RequestParam(value = "together",required = false, defaultValue = "false") boolean together){

		RestaurantInfo restaurant = restaurantService.getNearRestaurant(minLat, maxLat, minLng, maxLng,
			people, together);

		ResponseWithData<RestaurantInfo> responseWithData = new ResponseWithData<>(HttpStatus.OK.value(),
			"가맹점 조회에 성공하였습니다.", restaurant);

		return ResponseEntity.status(responseWithData.status())
			.body(responseWithData);
	}
}
