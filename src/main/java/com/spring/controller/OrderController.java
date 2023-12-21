package com.spring.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.wordnik.swagger.annotations.ApiParam;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.AddItem;
import com.spring.dto.ApiResponse;
import com.spring.dto.CheckoutDto;
import com.spring.iservice.IOrderService;
import com.spring.model.Order;
import com.spring.model.OrderItem;
import com.spring.model.Refund;
import com.spring.dto.CheckoutItemDto;
import com.spring.dto.CourseRequest;
import com.spring.dto.MyResponse;
import com.spring.dto.StripeResponse;

//import com.stripe.model.checkout.Session;
@RestController
@RequestMapping("/payment")
public class OrderController extends BaseController{
	@Autowired
	private IOrderService orderService;

	static Logger logger = Logger.getLogger(OrderController.class);
	@RequestMapping(value = "/create-checkout-session", method = RequestMethod.POST, headers = "content-type=application/json")	 
	 public ResponseEntity<ApiResponse> createCheckoutSession(@RequestBody CheckoutDto checkoutItem) throws  StripeException {
			JSONObject json=new JSONObject();
		     //List<Order> orderList;
			
			
			//	HttpServletRequest req = (HttpServletRequest) request;
				//String auth = req.getHeader("Token");
				//String validateToken=validateToken(auth,"USER");
				//if(validateToken.equalsIgnoreCase("true")) {
				int userId=1;
				  // Order order = orderService.createSession(checkoutItem,userId);
				String sessionId=orderService.createSession(checkoutItem,userId);
				ApiResponse response = new ApiResponse(true,sessionId,200);
			        // send the stripe session id in response
			        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);	      //  StripeResponse stripeResponse = new StripeResponse(session.getId());
	        // send the stripe session id in response
	       
			
		
		//else
		//{
			//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		//}


	}
	@RequestMapping(value = "/create-checkout-session1", method = RequestMethod.POST, headers = "content-type=application/json")	 
	 public ResponseEntity<ApiResponse> createCheckoutSession1(@RequestBody CheckoutDto checkoutItem) throws  StripeException {
		       // create the stripe session
	      //  Session session = orderService.createSession(checkoutItemDtoList);
		 ApiResponse response = new ApiResponse(true,orderService.createPaymentIntent(checkoutItem.getCheckoutItemList()),200);
		   
	     //   StripeResponse stripeResponse = new StripeResponse(orderService.createPaymentIntent(checkoutItemDtoList));
	        // send the stripe session id in response
	        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	    }
	
	@RequestMapping(value = "/getAllOrders", method = RequestMethod.POST, headers = "content-type=application/json")	 
	 public ResponseEntity<?>  getAllOrders(@RequestBody JSONObject req) throws SQLException {
			JSONObject json=new JSONObject();
		     List<Map<String, Object>> orderList;
			try {
			//	HttpServletRequest req = (HttpServletRequest) request;
				//String auth = req.getHeader("Token");
				//String validateToken=validateToken(auth,"USER");
				//if(validateToken.equalsIgnoreCase("true")) {
				int userId=1;
		   orderList= orderService.getAllOrders(userId);
	      //  StripeResponse stripeResponse = new StripeResponse(session.getId());
	        // send the stripe session id in response
	       if(orderList!=null) {
	        	json.put("list",orderList);
	        	// return new ResponseEntity<String>(uniqueId, HttpStatus.OK);
	        }
			
		}
		//else
		//{
			//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		//}


	catch (Exception e) {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		logger.error(stackTrace);
		return new ResponseEntity<>("service failed due to some exceptions", HttpStatus.INTERNAL_SERVER_ERROR);
	}
			return new ResponseEntity<>(json, HttpStatus.OK);
	       
		}
		@RequestMapping(value = "/getAllPurchasedCourses", method = RequestMethod.POST, headers = "content-type=application/json")	 
	 public ResponseEntity<MyResponse>   getAllPurchasedCourses(@RequestBody CourseRequest req) throws SQLException {
			JSONObject json=new JSONObject();
		     Set<Integer> orderList=new HashSet<Integer>();
		     MyResponse response=new MyResponse();
				
			try {
			//	HttpServletRequest req = (HttpServletRequest) request;
				//String auth = req.getHeader("Token");
				//String validateToken=validateToken(auth,"USER");
				//if(validateToken.equalsIgnoreCase("true")) {
				int userId=1;
		   orderList= orderService.getAllPurchasedCourses(req.getInstitute());
		   response.setMySet(orderList);
	      //  StripeResponse stripeResponse = new StripeResponse(session.getId());
	        // send the stripe session id in response
	  //   if(orderList!=null) {
	        //	json.put("list",orderList);
	        	 //return new ResponseEntity<String>(uniqueId, HttpStatus.OK);
	      // }
			
		}
		//else
		//{
			//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		//}


	catch (Exception e) {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		logger.error(stackTrace);
		return new ResponseEntity<MyResponse> (response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
			return new ResponseEntity<MyResponse> (response, HttpStatus.OK);
	       
		}
	@RequestMapping(value = "/getAllRefunds", method = RequestMethod.POST, headers = "content-type=application/json")	 
	 public ResponseEntity<?>  getAllRefunds(@RequestBody JSONObject req) throws SQLException {
			JSONObject json=new JSONObject();
		     List<Map<String, Object>> orderList;
			try {
			//	HttpServletRequest req = (HttpServletRequest) request;
				//String auth = req.getHeader("Token");
				//String validateToken=validateToken(auth,"USER");
				//if(validateToken.equalsIgnoreCase("true")) {
				int userId=1;
		   orderList= orderService.getAllRefunds(userId);
	      //  StripeResponse stripeResponse = new StripeResponse(session.getId());
	        // send the stripe session id in response
	       if(orderList!=null) {
	        	json.put("list",orderList);
	        	// return new ResponseEntity<String>(uniqueId, HttpStatus.OK);
	        }
			
		}
		//else
		//{
			//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
		//}


	catch (Exception e) {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		logger.error(stackTrace);
		return new ResponseEntity<>("service failed due to some exceptions", HttpStatus.INTERNAL_SERVER_ERROR);
	}
			return new ResponseEntity<>(json, HttpStatus.OK);
	       
		}
	@RequestMapping(value = "/requestRefund", method = RequestMethod.POST, headers = "content-type=application/json")	 
	    public ResponseEntity<String> refundCourses(@RequestBody Refund refund) {
	        try {
	        	int userId=1;
	        	ResponseEntity<String> result= orderService.refundCourses(refund,userId);
	            return ResponseEntity.ok(result.getBody());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Refund failed: " + e.getMessage());
	        }
	    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, headers = "content-type=application/json")	 
	public ResponseEntity<?> updateStatus(HttpServletRequest request,
			@ApiParam(value = "json object", required = true, name = "json")@RequestBody JSONObject json)throws SQLException  {
	//JSONObject response=new JSONObject();
		
		try {
			//HttpServletRequest req = (HttpServletRequest) request;
			//String auth = req.getHeader("Token");
			//String validateToken=validateToken(auth,"USER");
			//if(validateToken.equalsIgnoreCase("true")) {
			//org.json.JSONObject jsonObject = new org.json.JSONObject(json);
		//	org.json.JSONObject metadata = jsonObject.getJSONObject("metadata");

				orderService.UpdateStatus(Integer.parseInt(json.get("orderId").toString()),json.get("status").toString(),json.get("sessionId").toString());
									//}
			//else
			//{
				//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
			//}
		}
		catch (Exception e) {

			String stackTrace = ExceptionUtils.getStackTrace(e);
			logger.error(stackTrace);
			return new ResponseEntity<>("service failed due to some exceptions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(true, new HttpHeaders(), HttpStatus.OK);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reorder", method = RequestMethod.POST, headers = "content-type=application/json")	 
	public ResponseEntity<?> reorder(HttpServletRequest request,
			@ApiParam(value = "json object", required = true, name = "json")@RequestBody JSONObject json)throws SQLException  {
	//JSONObject response=new JSONObject();
		
		try {
			//HttpServletRequest req = (HttpServletRequest) request;
			//String auth = req.getHeader("Token");
			//String validateToken=validateToken(auth,"USER");
			//if(validateToken.equalsIgnoreCase("true")) {
			//org.json.JSONObject jsonObject = new org.json.JSONObject(json);
		//	org.json.JSONObject metadata = jsonObject.getJSONObject("metadata");

				orderService.UpdateReorderStatus(Integer.parseInt(json.get("orderId").toString()),json.get("status").toString(),json.get("paymentId").toString());
									//}
			//else
			//{
				//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
			//}
		}
		catch (Exception e) {

			String stackTrace = ExceptionUtils.getStackTrace(e);
			logger.error(stackTrace);
			return new ResponseEntity<>("service failed due to some exceptions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(true, new HttpHeaders(), HttpStatus.OK);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getRevenueByAdmin", method = RequestMethod.POST, headers = "content-type=application/json")	 
	public ResponseEntity<?> getRevenueByAdmin(HttpServletRequest request,
			@ApiParam(value = "json object", required = true, name = "json")@RequestBody CheckoutItemDto json)throws SQLException  {
	//JSONObject response=new JSONObject();
		 List<Map<String, Object>> revenue = null ;
		 try {
			//HttpServletRequest req = (HttpServletRequest) request;
			//String auth = req.getHeader("Token");
			//String validateToken=validateToken(auth,"USER");
			//if(validateToken.equalsIgnoreCase("true")) {
			//org.json.JSONObject jsonObject = new org.json.JSONObject(json);
		//	org.json.JSONObject metadata = jsonObject.getJSONObject("metadata");
				int userId=3;
			 revenue=orderService.getRevenueByAdmin(json,userId);
											//}
			//else
			//{
				//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
			//}
		}
		catch (Exception e) {

			String stackTrace = ExceptionUtils.getStackTrace(e);
			logger.error(stackTrace);
			return new ResponseEntity<>("service failed due to some exceptions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(revenue, new HttpHeaders(), HttpStatus.OK);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAdminCoursePurchases", method = RequestMethod.POST, headers = "content-type=application/json")	 
	public ResponseEntity<?> getCoursePurchases(HttpServletRequest request,
			@ApiParam(value = "json object", required = true, name = "json")@RequestBody CheckoutItemDto json)throws SQLException  {
	//JSONObject response=new JSONObject();
		 List<Map<String, Object>> revenue = null ;
		 try {
			//HttpServletRequest req = (HttpServletRequest) request;
			//String auth = req.getHeader("Token");
			//String validateToken=validateToken(auth,"USER");
			//if(validateToken.equalsIgnoreCase("true")) {
			//org.json.JSONObject jsonObject = new org.json.JSONObject(json);
		//	org.json.JSONObject metadata = jsonObject.getJSONObject("metadata");
			 int userId=3;
			 int branchId=452;
				revenue=orderService.getCoursePurchases(userId,branchId);
													//}
			//else
			//{
				//return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
			//}
		}
		catch (Exception e) {

			String stackTrace = ExceptionUtils.getStackTrace(e);
			logger.error(stackTrace);
			return new ResponseEntity<>(stackTrace, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(revenue, new HttpHeaders(), HttpStatus.OK);
	}
	
}
