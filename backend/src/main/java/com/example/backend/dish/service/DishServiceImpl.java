package com.example.backend.dish.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.common.dto.CommonResponse.ResponseWithMessage;
import com.example.backend.common.dto.CommonResponse.ResponseWithData;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.common.exception.JDQRException;
import com.example.backend.dish.dto.DishRequest;
import com.example.backend.dish.dto.DishRequest.DishInfo;
import com.example.backend.dish.dto.DishResponse;
import com.example.backend.dish.dto.DishResponse.DishSummaryInfo;
import com.example.backend.dish.dto.DishResponse.DishSummaryResultDto;
import com.example.backend.dish.entity.Dish;
import com.example.backend.dish.entity.DishCategory;
import com.example.backend.dish.entity.DishOptionGroup;
import com.example.backend.dish.entity.DishTag;
import com.example.backend.dish.entity.OptionGroup;
import com.example.backend.dish.entity.Tag;
import com.example.backend.dish.repository.DishCategoryRepository;
import com.example.backend.dish.repository.DishOptionGroupRepository;
import com.example.backend.dish.repository.DishRepository;
import com.example.backend.dish.repository.DishTagRepository;
import com.example.backend.dish.repository.OptionGroupRepository;
import com.example.backend.dish.repository.TagRepository;
import com.example.backend.owner.entity.Owner;
import com.example.backend.owner.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
	private final DishRepository dishRepository;
	private final DishCategoryRepository dishCategoryRepository;
	private final OwnerRepository ownerRepository;
	private final DishTagRepository dishTagRepository;
	private final TagRepository tagRepository;
	private final OptionGroupRepository optionGroupRepository;
	private final DishOptionGroupRepository dishOptionGroupRepository;

	@Override
	@Transactional
	public ResponseWithMessage addDish(Integer userId, DishInfo dishInfo) {

		//해당하는 가게 주인이 존재하는지 찾는다.
		Owner owner = ownerRepository.findById(userId)
			.orElseThrow(() -> new JDQRException(ErrorCode.USER_NOT_FOUND));

		//이제 dish를 만들어야 한다..
		//우선 dishCategory를 가지고 와야한다
		DishCategory dishCategory = dishCategoryRepository.findById(dishInfo.dishCategoryId())
			.orElseThrow(() -> new JDQRException(ErrorCode.FUCKED_UP_QR));

		// s3에서 저장 한 후 반환된 imageUrl
		String imageUrl = "";

		Dish dish = Dish.of(dishInfo,dishCategory,imageUrl);

		Dish savedDish = dishRepository.save(dish);

		// dishTag 엔티티 생성
		for(int tagId : dishInfo.tagIds()){
			Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new JDQRException(ErrorCode.TAG_NOT_FOUND));
			DishTag dishTag = DishTag.of(tag, savedDish);
			dishTagRepository.save(dishTag);

		}

		// 메뉴에 대한 옵션그룹 엔티티를 생성
		for(int optionGroupId : dishInfo.optionIds()){
			OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
				.orElseThrow(() -> new JDQRException(ErrorCode.OPTIONGROUP_NOT_FOUND));
			DishOptionGroup dishOptionGroup = DishOptionGroup.of(savedDish,optionGroup);
			dishOptionGroupRepository.save(dishOptionGroup);
		}


		return new ResponseWithMessage(HttpStatus.OK.value(), "메뉴목록에 추가했습니다");
	}


	@Override
	@Transactional
	public ResponseWithMessage removeDish(Integer userId, Integer dishId) {
		//해당하는 가게 주인이 존재하는지 찾는다.
		Owner owner = ownerRepository.findById(userId)
			.orElseThrow(() -> new JDQRException(ErrorCode.USER_NOT_FOUND));

		//가게 주인이 없애려는 메뉴가 db에 존재하는지 찾는다
		Dish dish = dishRepository.findById(dishId)
			.orElseThrow(() -> new JDQRException(ErrorCode.DISH_NOT_FOUND));

		//매뉴옵션그룹(dish_options) 테이블에서 dish_id컬럼이 dishId인 행들을 삭제
		List<DishOptionGroup> dishOptionGroups = dishOptionGroupRepository.findByDishId(dishId);
		for(DishOptionGroup dishOptionGroup : dishOptionGroups){
			dishOptionGroupRepository.delete(dishOptionGroup);
		}
		//메뉴태그(dish_tag) 테이블에서 dis_id컬럼이 dishId인 행들을 삭제
		List<DishTag> dishTags = dishTagRepository.findByDishId(dishId);
		for (DishTag dishTag : dishTags) {
			dishTagRepository.delete(dishTag);
		}
		//메뉴(dish) 테이블에서 id컬럼이 dishId인 행 삭제
		dishRepository.delete(dish);

		return new ResponseWithMessage(HttpStatus.OK.value(), "메뉴에서 삭제되었습니다.");
	}

	@Override
	@Transactional
	public ResponseWithMessage updateDish(Integer userId, Integer dishId, DishInfo dishInfo) {

		//해당하는 가게 주인이 존재하는지 찾는다.
		Owner owner = ownerRepository.findById(userId)
			.orElseThrow(() -> new JDQRException(ErrorCode.USER_NOT_FOUND));

		//가게 주인이 수정하려는 메뉴가 db에 존재하는지 찾는다
		Dish dish = dishRepository.findById(dishId)
			.orElseThrow(() -> new JDQRException(ErrorCode.DISH_NOT_FOUND));

		//메뉴의 정보(이름, 가격, 설명)를 수정한다.
		dish.setName(dishInfo.dishName());
		dish.setPrice(dishInfo.price());
		dish.setDescription(dishInfo.description());

		//메뉴의 정보(이미지)를 수정한다.
		String imageUrl = "";
		dish.setImage(imageUrl);

		//메뉴의 정보(카테고리)를 수정한다.
		Integer dishCategoryId = dishInfo.dishCategoryId();
		if(dishCategoryId != null && dishCategoryId > 0){
			DishCategory dishCategory = dishCategoryRepository.findById(dishInfo.dishCategoryId())
				.orElseThrow(() -> new JDQRException(ErrorCode.FUCKED_UP_QR));
			dish.setDishCategory(dishCategory);
		}

		//메뉴의 정보(태그)를 수정한다.
		//메뉴id가 dishId인 기존의 dishTag 삭제
		List<DishTag> existingTags = dishTagRepository.findByDishId(dishId);
		for(DishTag dishTag : existingTags){
			dishTagRepository.delete(dishTag);
		}
		//새로운 dishTag 추가
		for(int tagId : dishInfo.tagIds()){
			Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new JDQRException(ErrorCode.TAG_NOT_FOUND));
			DishTag newDishTag = DishTag.of(tag, dish);
			dishTagRepository.save(newDishTag);
		}

		//메뉴의 정보(옵션그룹)를 수정한다.
		//메뉴id가 dishId인 기존의 optionGroup 삭제
		List<DishOptionGroup> existingOptionGroups = dishOptionGroupRepository.findByDishId(dishId);
		for(DishOptionGroup dishOptionGroup : existingOptionGroups){
			dishOptionGroupRepository.delete(dishOptionGroup);
		}
		//새로운 optionGroup 추가
		for(int optionGroupId : dishInfo.optionIds()){
			OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
				.orElseThrow(() -> new JDQRException(ErrorCode.OPTIONGROUP_NOT_FOUND));
			DishOptionGroup newDishOptionGroup = DishOptionGroup.of(dish, optionGroup);
			dishOptionGroupRepository.save(newDishOptionGroup);
		}

		dishRepository.save(dish);

		return new ResponseWithMessage(HttpStatus.OK.value(), "메뉴가 수정되었습니다.");
	}

}
