package com.example.backend.etc.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.common.enums.CategoryType;
import com.example.backend.common.enums.UseStatus;
import com.example.backend.etc.dto.RestaurantResponse.RestaurantInfo;
import com.example.backend.etc.entity.Restaurant;
import com.example.backend.etc.entity.RestaurantCategory;
import com.example.backend.etc.entity.RestaurantCategoryMap;
import com.example.backend.etc.repository.RestaurantCategoryMapRepository;
import com.example.backend.etc.repository.RestaurantCategoryRepository;
import com.example.backend.etc.repository.RestaurantRepository;
import com.example.backend.owner.entity.Owner;
import com.example.backend.table.dto.Grid;
import com.example.backend.table.entity.Table;
import com.example.backend.table.repository.TableRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

	@InjectMocks
	private RestaurantServiceImpl restaurantService;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private RestaurantCategoryRepository restaurantCategoryRepository;
	@Mock
	private RestaurantCategoryMapRepository restaurantCategoryMapRepository;
	@Mock
	private TableRepository tableRepository;

	@DisplayName("유저의 화면범위에 존재하는 가맹점을 조회할 수 있다")
	@Test
	void getRestaurantByCoord() {

		//given
		Owner owner = Owner.builder()
			.id(1)
			.email("sujipark2009@gmail.com")
			.code("ABCDEFG")
			.name("김영표")
			.build();


		Restaurant restaurant1 = Restaurant.builder()
			.id(1)
			.owner(owner)
			.name("용수의식당")
			.address("멀티캠퍼스10층")
			.phoneNumber("000-111-222")
			.latitude(10.0)
			.longitude(130.0)
			.open(true)
			.build();

		Restaurant restaurant2 = Restaurant.builder()
			.id(2)
			.owner(owner)
			.name("영표집")
			.address("멀티캠퍼스11층")
			.phoneNumber("000-111-222")
			.latitude(5.0)
			.longitude(130.0)
			.open(true)
			.build();

		Restaurant restaurant3 = Restaurant.builder()
			.id(3)
			.owner(owner)
			.name("용수집")
			.address("멀티캠퍼스13층")
			.phoneNumber("000-111-222")
			.latitude(10.0)
			.longitude(150.0)
			.open(true)
			.build();

		List<Restaurant> restaurants = List.of(restaurant1, restaurant2,restaurant3);

		RestaurantCategory restaurantCategory1 = RestaurantCategory.builder()
			.id(1)
			.name("일식")
			.categoryType(CategoryType.MAJOR)
			.build();

		RestaurantCategory restaurantCategory2 = RestaurantCategory.builder()
			.id(2)
			.name("중식")
			.categoryType(CategoryType.MAJOR)
			.build();

		RestaurantCategory restaurantCategory3 = RestaurantCategory.builder()
			.id(3)
			.name("한식")
			.categoryType(CategoryType.MINOR)
			.build();

		RestaurantCategory restaurantCategory4 = RestaurantCategory.builder()
			.id(4)
			.name("양식")
			.categoryType(CategoryType.MAJOR)
			.build();

		RestaurantCategory restaurantCategory5 = RestaurantCategory.builder()
			.id(5)
			.name("술집")
			.categoryType(CategoryType.MINOR)
			.build();

		List<RestaurantCategory> restaurantCategories = List.of(restaurantCategory1, restaurantCategory2, restaurantCategory3, restaurantCategory4,restaurantCategory5);

		RestaurantCategoryMap restaurantCategoryMap1 = RestaurantCategoryMap.builder()
			.restaurant(restaurant1)
			.restaurantCategory(restaurantCategory1)
			.build();

		RestaurantCategoryMap restaurantCategoryMap2 = RestaurantCategoryMap.builder()
			.restaurant(restaurant1)
			.restaurantCategory(restaurantCategory2)
			.build();

		RestaurantCategoryMap restaurantCategoryMap3 = RestaurantCategoryMap.builder()
			.restaurant(restaurant2)
			.restaurantCategory(restaurantCategory2)
			.build();

		RestaurantCategoryMap restaurantCategoryMap4 = RestaurantCategoryMap.builder()
			.restaurant(restaurant3)
			.restaurantCategory(restaurantCategory3)
			.build();

		RestaurantCategoryMap restaurantCategoryMap5 = RestaurantCategoryMap.builder()
			.restaurant(restaurant3)
			.restaurantCategory(restaurantCategory4)
			.build();

		List<RestaurantCategoryMap> restaurantCategoryMaps = List.of(restaurantCategoryMap1, restaurantCategoryMap2, restaurantCategoryMap3, restaurantCategoryMap4,restaurantCategoryMap5);

		when(restaurantRepository.findByCoord(anyDouble(),anyDouble(),anyDouble(),anyDouble()))
			.thenReturn(restaurants);

		when(restaurantCategoryRepository.findAllMajor())
			.thenReturn(restaurantCategories);

		when(restaurantCategoryMapRepository.findByRestaurandIds(anyList()))
			.thenReturn(restaurantCategoryMaps);


		Table table1 = Table.builder()
			.restaurantId(restaurant1.getId())
			.color("#fffff")
			.name("영표의식탁1")
			.people(6)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		Table table2 = Table.builder()
			.restaurantId(restaurant1.getId())
			.color("#fffff")
			.name("영표의식탁2")
			.people(4)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		Table table3 = Table.builder()
			.restaurantId(restaurant2.getId())
			.color("#fffff")
			.name("영표의식탁3")
			.people(3)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		Table table4 = Table.builder()
			.restaurantId(restaurant2.getId())
			.color("#fffff")
			.name("영표의식탁4")
			.people(2)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		Table table5 = Table.builder()
			.restaurantId(restaurant3.getId())
			.color("#fffff")
			.name("영표의식탁5")
			.people(5)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		Table table6 = Table.builder()
			.restaurantId(restaurant3.getId())
			.color("#fffff")
			.name("영표의식탁6")
			.people(6)
			.useStatus(UseStatus.AVAILABLE)
			.build();

		List<Table> findTables = List.of(table1,table2,table3,table4,table5,table6);

		// when(tableRepository.findByRestaurantId(anyInt()))
		// 	.thenReturn(findTables);

		// 식당 ID에 따라 테이블 리스트를 필터링하여 반환하는 Answer 설정
		when(tableRepository.findByRestaurantId(anyInt()))
			.thenAnswer(invocation -> {
				int restaurantId = invocation.getArgument(0); // 호출된 restaurantId 값을 가져옴
				return findTables.stream()
					.filter(table -> table.getRestaurantId() == restaurantId) // restaurantId와 일치하는 테이블만 필터링
					.collect(Collectors.toList());
			});


		//when
		RestaurantInfo res1 = restaurantService.getNearRestaurant(0.0, 50.0, 0.0, 150.0, 0, false);

		log.warn("res1 : {}",res1);

		//then
		assertThat(res1.majorCategories())
			.containsExactly("일식","중식","한식","양식","술집");
		
		// assertThat(res1.restaurants())
		// 	.extracting("restTableNum","restSeatNum")
		// 	.containsExactly(Tuple.tuple(1,6),
		// 		Tuple.tuple(1,3),
		// 		Tuple.tuple(1,5));

		//when
		RestaurantInfo res2 = restaurantService.getNearRestaurant(0.0, 50.0, 0.0, 150.0, 4, true);

		// //then
		// assertThat(res2.restaurants())
		// 	.extracting("restaurantName")
		// 	.containsExactly(
		// 		"용수의식당",
		// 		"용수집"
		// 	);

		assertThat(res2.restaurants().size()).isEqualTo(2);


		//when
		RestaurantInfo res3 = restaurantService.getNearRestaurant(0.0, 50.0, 0.0, 150.0, 4, false);

		// //then
		// assertThat(res3.restaurants())
		// 	.extracting("restaurantName")
		// 	.containsExactly(
		// 		"용수의식당",
		// 		"용수집"
		// 	);

		assertThat(res3.restaurants().size()).isEqualTo(3);
	}


}