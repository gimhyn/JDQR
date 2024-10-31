package com.example.backend.order.service;

import com.example.backend.order.dto.CartRequest;

public interface OrderService {

	String redirectUrl(String tableName,String uuid);

	void addItem(String tableId, CartRequest.ProductInfo productInfo);
}