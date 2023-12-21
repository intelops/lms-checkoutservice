package com.spring.iservice;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.spring.dto.AddItem;
import com.spring.dto.CheckoutDto;
import com.spring.dto.CheckoutItemDto;
import com.spring.model.Order;
import com.spring.model.Refund;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface IOrderService {

	//Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException;

	String createSession(CheckoutDto checkoutItem, int userId) throws StripeException;

	List<Map<String, Object>> getAllOrders(int userId);

	void UpdateStatus(int parseInt, String string, String string2) throws StripeException;

	void UpdateReorderStatus(int parseInt, String string, String string2);

	String createPaymentIntent(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException;

	List<Map<String, Object>> getAllRefunds(int userId);

	ResponseEntity<String> refundCourses(Refund refund, int userId) throws Exception;

	Set<Integer> getAllPurchasedCourses(int userId);

	List<Integer> getBatchMembersHighestPurchasedCourses(int userId);

	//List<Map<String, Object>>  getYearData(CheckoutItemDto json, int userId);

	//List<Map<String, Object>> getWeekData(CheckoutItemDto json, int userId);

	//List<Map<String, Object>> getInterval(CheckoutItemDto json, int userId);

	//List<Map<String, Object>> getCoursePurchases(int userId);

	//List<Map<String, Object>> getCoursePurchases(int userId);

	List<Map<String, Object>> getRevenueByAdmin(CheckoutItemDto json, int userId);

	List<Map<String, Object>> getCoursePurchases(int userId, int branchId);

	///Order saveOrder(CheckoutDto checkoutItem, int userId)throws StripeException;
}
