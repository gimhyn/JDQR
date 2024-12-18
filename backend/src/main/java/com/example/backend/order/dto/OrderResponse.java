package com.example.backend.order.dto;


import com.example.backend.order.enums.OrderStatus;
import com.example.backend.order.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

public record OrderResponse() {

    @Schema(name = "전체 주문 내역 조회 결과 반환 dto", description = "주문 내역 조회 api의 응답 결과를 담는다")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public record TotalOrderInfoResponseDto(
        String tableName,
        Integer dishCnt,
        Integer userCnt,
        Integer restDishCnt,
        Integer peopleNum,
        PaymentMethod paymentType,
        Integer price,
        Integer restPrice,
        List<OrderInfoResponseDto> orders
    ) {

    }

    @Schema(name = "상위 주문 내역 조회 결과 반환 dto", description = "상위 주문의 정보를 담은 dto")
    @Builder
    public record ParentOrderInfoResponseDto(
        Integer parentOrderId,
        OrderStatus orderStatus
    ) {

    }

    @Schema(name = "주문 내역 조회 결과 반환 dto", description = "각 주문별 주문 내역 조회 정보를 담는다")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public record OrderInfoResponseDto(
        Integer orderId,
        Integer price,
        Integer restPrice,
        Integer dishCnt,
        Integer restDishCnt,
        List<DishInfoResponseDto> dishes
    ) {

    }

    @Schema(name = "주문 내역 중 메뉴에 대한 정보를 담고 있는 dto", description = "주문한 메뉴들에 대한 세부 정보를 담는다")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    public record DishInfoResponseDto(
        Integer orderItemId,
        Integer dishId,
        String userId,
        String dishName,
        Integer dishCategoryId,
        String dishCategoryName,
        Integer price,
        Integer totalPrice,
        List<OptionDetailDto> options,
        Integer quantity,
        Integer restQuantity
    ) {

    }

    @Schema(name = "결제 정보 업데이트 api 응답 형식", description = " status와 detailMessage를 담고 있음")
    @Builder
    public record PaymentConfirmResponseDto(
        String status,
        String detailMessage
    ) {

    }


}
