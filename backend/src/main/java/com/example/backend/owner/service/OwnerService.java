package com.example.backend.owner.service;

import static com.example.backend.common.dto.CommonResponse.*;
import static com.example.backend.dish.dto.DishRequest.*;
import static com.example.backend.dish.dto.DishResponse.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dish.dto.DishRequest;
import com.example.backend.dish.dto.OptionDto;
import com.example.backend.owner.dto.CategoryDto;
import com.example.backend.owner.dto.OwnerRequest;
import com.example.backend.owner.dto.OwnerRequest.OptionRequestDto;
import com.example.backend.owner.dto.OwnerResponse.*;

public interface OwnerService {

	ResponseWithMessage addDish(Integer userId, DishInfo dishInfo, MultipartFile multipartFile);
	ResponseWithMessage removeDish(Integer userId, Integer dishId);
	ResponseWithMessage updateDish(Integer userId, Integer dishId, DishInfo dishInfo, MultipartFile multipartFile);

	DishSummaryResultDto getAllMenus(Integer userId);
	DishSummaryResultDto getMenu(Integer userId,Integer dishId);

	void createCategory(CategoryDto categoryDto,Integer userId);
	void removeCategory(Integer dishCategoryId,Integer userId);
	CategoryResult getAllCategories(Integer userId);

    WholeOptionResponseDto getWholeOptionInfo(Integer userId);

	OptionResponseDto getIndividualOptionInfo(Integer userId, Integer optionId);

	void createOption(Integer userId,OptionRequestDto optionDto);
	void deleteOption(Integer userId,Integer optionId);
	void updateOption(Integer userId,Integer optionId,OptionRequestDto optionDto);
}
