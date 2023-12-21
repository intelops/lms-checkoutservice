package com.spring.serviceImpl;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.dto.AddItem;
import com.spring.dto.CheckoutDto;
import com.spring.dto.CheckoutItemDto;
import com.spring.dto.CourseRequest;
import com.spring.dto.LmsResponse;
import com.spring.dto.QueryResponse;
import com.spring.iservice.IOrderService;
import com.spring.model.Order;
import com.spring.model.OrderItem;
import com.spring.model.Refund;
import com.spring.repository.IOrderRepository;
import com.spring.service.BaseServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
@Transactional
public class OrderService extends BaseServiceImpl implements IOrderService {
	@Autowired
	private IOrderRepository repository;

	/*@Override
	public Order createSession(CheckoutDto checkoutItem, int userId) throws StripeException  {

		// TODO Auto-generated method stub
		 Order newOrder = new Order();
		  try {
		String sessionId=createSession(checkoutItem);
	     if(sessionId!=null) {
	    	  newOrder.setCreatedDate(new Date());
		        newOrder.setSessionId(sessionId);
		        newOrder.setUserId(userId);
		        newOrder.setTotalPrice(checkoutItem.getTotalPrice());
		        genericDao.create(newOrder);

		        for (CheckoutItemDto itemDto : checkoutItem.getCheckoutItemList()) {
		            // create orderItem and save each one
		            OrderItem orderItem = new OrderItem();
		            orderItem.setPrice(itemDto.getPrice());
		            orderItem.setProductId(itemDto.getProductId());
		            orderItem.setQuantity(itemDto.getQuantity());
		            orderItem.setOrderId(newOrder.getId());
		            // add to order item list
		            genericDao.create(orderItem);
		        }
	        }
	        if(newOrder!=null) {
	        removeAllItemsFromcart(userId);
	        }
		  }
		  catch(Exception e){
			  throw e;
		  }
	        return newOrder;
	}*/
	private String baseURL="http://localhost:8081/";

	//  @Value("${STRIPE_SECRET_KEY}")
	private String apiKey="sk_test_51LuZLeKHclTRBfJZSeoAM6zLnf38sOt5g0NiYOOcpYKNQJ2KyQ7k1yOMgGxPJh6rulthuUQ7HaAvWnMNxnqWom1L00FcJdciuQ";

	SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
		return SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("usd")
				.setUnitAmount((long)(checkoutItemDto.getPrice()*100))
				.setProductData(
						SessionCreateParams.LineItem.PriceData.ProductData.builder()
						.setName(checkoutItemDto.getProductName())
						.build())
				.build();
	}

	// build each product in the stripe checkout page
	SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
		return SessionCreateParams.LineItem.builder()
				// set price for each product
				.setPriceData(createPriceData(checkoutItemDto))
				// set quantity for each product
				.setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
				.build();
	}
	@Override
	public String createPaymentIntent(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
		Stripe.apiKey = apiKey;
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setCurrency("usd")
				.setAmount(100L)
				.build();
		PaymentIntent paymentIntent = PaymentIntent.create(params);
		return paymentIntent.getClientSecret();
	}
	@Override
	public String createSession(CheckoutDto checkoutItem, int userId) throws StripeException  {
		// TODO Auto-generated method stub
		Order newOrder = new Order();
		try {
			String successURL =  baseURL+"dashboard/myTransaction";
			String failedURL = baseURL+"checkout";
			// set the private key
			Stripe.apiKey = apiKey;

			List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

			// for each product compute SessionCreateParams.LineItem
			for (CheckoutItemDto checkoutItemDto : checkoutItem.getCheckoutItemList()) {
				sessionItemsList.add(createSessionLineItem(checkoutItemDto));
			}

			// build the session param
			SessionCreateParams params = SessionCreateParams.builder()
					.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setCancelUrl(failedURL)
					.addAllLineItem(sessionItemsList)
					.setSuccessUrl(successURL)
					.build();
			//   if(Session.create(params).getId()!=null) {
			// 	newOrder=placeOrder(checkoutItem, userId,Session.create(params).getId());
			//  }
			// if(newOrder!=null) {
			// / removeAllItemsFromcart(userId);
			// }
			return Session.create(params).getId();
		}
		catch(StripeException e){
			throw e;
		}

	}
	public Order placeOrder(CheckoutDto checkoutItem,int userId, String sessionId) {
		// first let get cart items for the user
		Order newOrder = new Order();
		newOrder.setCreatedDate(new Date());
		newOrder.setSessionId(sessionId);
		newOrder.setUserId(userId);
		newOrder.setTotalPrice(checkoutItem.getTotalPrice());
		genericDao.create(newOrder);

		for (CheckoutItemDto itemDto : checkoutItem.getCheckoutItemList()) {
			// create orderItem and save each one
			OrderItem orderItem = new OrderItem();
			orderItem.setPrice(itemDto.getPrice());
			orderItem.setProductId(itemDto.getProductId());
			orderItem.setQuantity(itemDto.getQuantity());
			orderItem.setOrderId(newOrder.getId());
			// add to order item list
			genericDao.create(orderItem);
		}
		//
		//i have to write code to save courses in course table

		return newOrder;
	}

	@Override
	public List<Map<String, Object>> getAllOrders(int userId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> mapList= new ArrayList<Map<String,Object>>();

		try {
			List<Order> orderList=repository.getAllOrders(userId);
			//	List<OrderItem> orderitemList=orderList.get
			for(int i=0;i<orderList.size();i++) {
				Map<String,Object> list=new HashMap<String,Object>();
				List<Object> courseList=new ArrayList<Object>();
				list.put("totalPrice",orderList.get(i).getTotalPrice());
				list.put("orderId",orderList.get(i).getId());
				list.put("createdDate",orderList.get(i).getCreatedDate());
				//	list.put("quantity",orderList.get(i).getTotalPrice());
				List<OrderItem> orderitemList=orderList.get(i).getOrderitems();
				for(int j=0;j<orderitemList.size();j++) {
					//	Map<String,Object> course=getCourseById(orderList.get(i).getProductId()).getBody();
					LmsResponse course=(getCourseById(orderitemList.get(j).getProductId())).getBody();
					//	course
					courseList.add(course.getResponse());


				}
				list.put("courseList", courseList);
				mapList.add(list);
			}
			return mapList;
		}
		catch(Exception e) {
			throw e;
		}
	}
	@Override
	public List<Map<String, Object>> getAllRefunds(int userId) {
		List<Map<String,Object>> mapList= new ArrayList<Map<String,Object>>();

		// TODO Auto-generated method stub
		try {
			List<Refund> refundList=repository.getAllRefunds(userId);
			//List<OrderItem> orderitemList=orderList.get
			for(int i=0;i<refundList.size();i++) {
				Map<String,Object> list=new HashMap<String,Object>();	
				List<Object> courseList=new ArrayList<Object>();
				genericDao.setClazz(Order.class);
				Order order=(Order) genericDao.findOne(refundList.get(i).getOrderId());
				if(order!=null && order.getRefund()!=null) {
				list.put("totalPrice",order.getTotalPrice());
				list.put("orderId",order.getId());
				list.put("createddate",order.getCreatedDate());
				list.put("refund", refundList.get(i));
				List<OrderItem> orderitemList=order.getOrderitems();
				for(int j=0;j<orderitemList.size();j++) {
					LmsResponse course=(getCourseById(orderitemList.get(j).getProductId())).getBody();
					courseList.add(course.getResponse());
				}
				}
				list.put("courseList", courseList);
				mapList.add(list);
			}
			return mapList;
		}
		catch(Exception e) {
			throw e;
		}
	}

	@Override
	public void UpdateStatus(int orderId, String status, String paymentId)   {
		//	JSONObject response=new JSONObject();
		try {
			//	if(status.equals("succeeded")) {
			//repository.updateStatus(orderId,paymentId,status);

			//response.put("message",status);
			//}
			//else {
			//repository.updatStatus(orderId,status,id);
			//}

		}
		catch (Exception e) {
			throw e;
		}
		// return response;
	}
	@Override
	public void UpdateReorderStatus(int orderId, String status, String sessionId) {
		//	JSONObject response=new JSONObject();
		try {
			//	if(status.equals("succeeded")) {
			//repository.updatReorderStatus(orderId,status,sessionId);

			//response.put("message",status);
			//}
			//else {
			//repository.updatStatus(orderId,status,id);
			//}

		}
		catch (Exception e) {
			throw e;
		}
		// return response;
	}

	@Override
	public ResponseEntity<String> refundCourses(Refund refund,int userId) throws Exception {
		// TODO Auto-generated method stub
		try {
			genericDao.setClazz(Order.class);
			Order order=(Order)genericDao.findOne(refund.getOrderId());
			Calendar start = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			String startDate=formatter.format(start.getTime());
			if(order==null) {
				return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
			}
			Refund refundSave = new Refund();
			//  refundSave.setOrderId(refund.getOrderId());
			//  refundSave.setTotalPrice(refund.getTotalPrice());
			refund.setUserId(userId);
			refund.setReasonId(refund.getReasonId());
			// refundSave.setReasonDiscription(refund.getReasonDiscription());
			refund.setRequestDate(formatter.parse(startDate));
			// refundSave.setProcessedDate(null);
			//refund.setApproved(false);
			genericDao.create(refund);
			repository.updatOrderRefund(refund.getOrderId());
			return new ResponseEntity<>("Refund request processed successfully", HttpStatus.OK);     
		}
		catch (Exception e) {
			throw e;
		}
	}
	private Double calculateRefundAmount(Double coursePrice, Double requestedAmount) {
		if (requestedAmount > coursePrice) {
			return coursePrice;
		} else {
			return requestedAmount;
		}
	}

	@Override
	public Set<Integer> getAllPurchasedCourses(int userId) {
		// TODO Auto-generated method stub
		Set<Integer> courseList=new HashSet<Integer>();
		try {
			List<Order> orderList=repository.getAllOrders(userId);
			for(int i=0;i<orderList.size();i++) {
				List<OrderItem> orderitemList=orderList.get(i).getOrderitems();
				for(int j=0;j<orderitemList.size();j++) {
					courseList.add(orderitemList.get(j).getProductId());
				}
			}
			return courseList;
		}
		catch(Exception e) {
			throw e;
		}
	}

	@Override
	public List<Integer> getBatchMembersHighestPurchasedCourses(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	private Double getYearData(CheckoutItemDto json, Set<Integer> users, List<Integer> list, List<Order> purchases, int month2,
			LocalDate date)	{	
		double revenue = 0.0; 
		for(int o=0;o<purchases.size();o++)
		{
			List<OrderItem> itemList=purchases.get(o).getOrderItems();
			//	if(users.contains(purchases.get(o).getUserId())){
			for(int i=0;i<itemList.size();i++) {		
			//	for(int j=0;j<list.size();j++) {
				if(list.contains(itemList.get(i).getProductId())){
				//	List<OrderItem> courseList=repository.getPurchasedCount(list.get(j));
					//	if(Integer.parseInt(courseList.get(j).get("id").toString())==itemList.get(i).getProductId()) {
					Map<String,Object> data=new HashMap<String,Object>();

					if (json.getPeriod().equals("year")) {

						//for (Month month : Month.values()) {
						if(month2==purchases.get(o).getCreatedDate().getMonth()+1) {
							// Map<String, Object> data = repository.findByBranchAndMonth(filter);
							System.out.println("year"+itemList.get(i).getPrice()+"     "+month2+"    "+itemList.get(i).getId());
							revenue+=itemList.get(i).getPrice();
						//	List<OrderItem> courseList=repository.getPurchasedCount(list.get(j),month2);


						}
					//}
//
					}
				}
			}
		}
		return revenue;
	}
	private Double getWeekData(CheckoutItemDto json, Set<Integer> users, List<Integer> list, List<Order> purchases, int month2,
			LocalDate date)	{	
		double revenue = 0.0; 
		for(int o=0;o<purchases.size();o++)
		{
			List<OrderItem> itemList=purchases.get(o).getOrderItems();
			//	if(users.contains(purchases.get(o).getUserId())){
			for(int i=0;i<itemList.size();i++) {		
				//	for(int j=0;j<courseList.size();j++) {
				if(list.contains(itemList.get(i).getProductId())){
					//	if(Integer.parseInt(courseList.get(j).get("id").toString())==itemList.get(i).getProductId()) {
					Map<String,Object> data=new HashMap<String,Object>();

					if (json.getPeriod().equals("week")) {


						if(date.getDayOfWeek().getValue()==purchases.get(o).getCreatedDate().getDay()) {
							//  Map<String, Object> data = repository.findByBranchAndMonth(filter);
							revenue+=itemList.get(i).getPrice();
						}
						// }
						//}
						// revenue+=itemList.get(i).getPrice();

					}
				}
			}
		}
		return revenue;
	}
	
	//@Override
	/*private Double getRevenueByAdmin(CheckoutItemDto json, Set<Integer> users, List<Integer> list, List<Order> purchases, int month2,
			LocalDate date)	{	
		try {
			//	
			//LocalDate currentDate = LocalDate.now();
			double revenue = 0.0; 
			for(int o=0;o<purchases.size();o++)
			{
				List<OrderItem> itemList=purchases.get(o).getOrderItems();
				//	if(users.contains(purchases.get(o).getUserId())){
				for(int i=0;i<itemList.size();i++) {		
					//	for(int j=0;j<courseList.size();j++) {
					if(list.contains(itemList.get(i).getProductId())){
						//	if(Integer.parseInt(courseList.get(j).get("id").toString())==itemList.get(i).getProductId()) {
						Map<String,Object> data=new HashMap<String,Object>();

						if (json.getPeriod().equals("year")) {

							//for (Month month : Month.values()) {
							if(month2==purchases.get(o).getCreatedDate().getMonth()+1) {
								// Map<String, Object> data = repository.findByBranchAndMonth(filter);
								System.out.println("year"+itemList.get(i).getPrice()+"     "+month2+"    "+itemList.get(i).getId());
								revenue+=itemList.get(i).getPrice();

							}


						}

						else if (json.getPeriod().equals("week")) {


							if(date.getDayOfWeek().getValue()==purchases.get(o).getCreatedDate().getDay()) {
								//  Map<String, Object> data = repository.findByBranchAndMonth(filter);
								revenue+=itemList.get(i).getPrice();
							}
							// }
							//}
							// revenue+=itemList.get(i).getPrice();

						}



					}

				}
				//}
				//}

				//}
				// result.add(data);
				// return revenue; 
				//  data.put("revenue",expenditure-purchases.get(o).getTotalPrice());
				//result.add
			}
			return revenue;  
		}
		catch(Exception e) {
			throw e;
		}

	}*/
	@Override
	public List<Map<String, Object>> getRevenueByAdmin(CheckoutItemDto json, int userId){
		LocalDate currentDate = LocalDate.now();
		CourseRequest req=new CourseRequest();
		CourseRequest req1=new CourseRequest();
		req1.setInstitute(userId);
		List<Integer> list=new ArrayList<Integer>();
		List<Map<String, Object>> courseList=getAllCourse(req1).getBody().getList();
		for(int i=0;i<courseList.size();i++) {
			list.add(Integer.parseInt(courseList.get(i).get("id").toString()));
		}
		System.out.println(list);
		Set<Integer> users=new HashSet<Integer>();
		List<Order> purchases=null;
		if(json.getBranch()!=0) {
			req.setBatchId(json.getBranch());
			users=getNoOfUsers(req).getMySet();
			purchases = repository.findByBranchAndMonth(json.getPeriod(),users);
		}
		else {
			purchases = repository.findByBranchAndMonth(json.getPeriod(),users);

		}
		//LocalDate currentDate = LocalDate.now();
		//double revenue = 0.0; 
		List<Map<String, Object>> result = new ArrayList<>();
		if(json.getPeriod().equals("year") || json.getPeriod().equals("3") || json.getPeriod().equals("6")) {
			for (Month month : Month.values()) {
				Map<String, Object> data = new HashMap<>();
				Month month1 = Month.of(month.getValue());
				Double revenue=getYearData(json,users,list,purchases,month.getValue(),currentDate);
				data.put("name", month1.toString());
				Double expenditure=4000.0;
				data.put("Earnings",revenue);
				data.put("Expenditure",expenditure);
				data.put("revenue",revenue-expenditure);
				result.add(data);
			}
		}
		else if(json.getPeriod().equals("week") ){
			//	List<Map<String, Object>> result = new ArrayList<>();
			LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
			LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

			for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
				// Map<String,Object> data=new HashMap<String,Object>();
				Double revenue=getWeekData(json,users,list,purchases,currentDate.getMonth().getValue(),date);
				//if(date.getDayOfWeek().getValue()==purchases.get(o).getCreatedDate().getDay()) {
				Map<String, Object> data = new HashMap<>();
				data.put("name", date.getDayOfWeek());
				Double expenditure=4000.0;
				data.put("Earnings",revenue);
				data.put("Expenditure",expenditure);
				data.put("revenue",revenue-expenditure);
				result.add(data);
			}
		}

		return result;
	}

	/*@Override
	public List<Map<String, Object>> getWeekData(CheckoutItemDto json, int userId){
		LocalDate currentDate = LocalDate.now();

		List<Map<String, Object>> result = new ArrayList<>();
		LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
			// Map<String,Object> data=new HashMap<String,Object>();
			//	Double revenue=getRevenueByAdmin(json, userId,currentDate.getMonth().getValue(),date);
			//if(date.getDayOfWeek().getValue()==purchases.get(o).getCreatedDate().getDay()) {
			Map<String, Object> data = new HashMap<>();
			data.put("name", date.getDayOfWeek());
			Double expenditure=4000.0;
			//	data.put("Earnings",revenue);
			data.put("Expenditure",expenditure);
			//	data.put("revenue",revenue-expenditure);
			result.add(data);
		}
		// }
		//}
		return result;
	}
	@Override
	public List<Map<String, Object>> getInterval(CheckoutItemDto json, int userId){
		LocalDate currentDate = LocalDate.now();

		List<Map<String, Object>> result = new ArrayList<>();
		for (Month month : Month.values()) {
			Map<String, Object> data = new HashMap<>();
			Month month1 = Month.of(month.getValue());
			//	Double revenue=getRevenueByAdmin(json, userId,month.getValue(),currentDate);
			data.put("name", month1.toString());
			Double expenditure=4000.0;
			//data.put("Earnings",revenue);
			data.put("Expenditure",expenditure);
			//data.put("revenue",revenue-expenditure);
			result.add(data);
		}
		return result;
	}*/

	@Override
	public List<Map<String, Object>> getCoursePurchases(int userId,int branchId) {
		// TODO Auto-generated method stub
		CourseRequest req1=new CourseRequest();
		CourseRequest req=new CourseRequest();
		List<Map<String, Object>> result = new ArrayList<>();

		req1.setInstitute(userId);
		//req1.setBranchId(branchId);
		List<Integer> list=new ArrayList<>();
		List<Map<String, Object>> courseList=getAllCourse(req1).getBody().getList();
		for(int i=0;i<courseList.size();i++) {
			list.add(Integer.parseInt(courseList.get(i).get("id").toString()));
		}
		Set<Integer> users=new HashSet<Integer>();
		List<Object[]> purchases=null;
		if(branchId!=0) {
			req.setBatchId(branchId);
			users=getNoOfUsers(req).getMySet();

			purchases = repository.getPurchasedCount(list,users);
		}
		else {
			purchases = repository.getPurchasedCount(list,users);

		}
		//List<Order> orderList=repository.getCoursePurchases(list);
		//for(Order order:orderList) {
		for (Object[] row : purchases) {
			for(int k=0;k<courseList.size();k++) {
				
            int productId = (int) row[0];
            double percentage = (double) row[1];
            if(Integer.parseInt(courseList.get(k).get("id").toString())==productId) {
				Map<String, Object> data=new HashMap<String, Object>();
				data.put("courseName",courseList.get(k).get("courseName"));
				data.put("percentage", percentage/100);
				result.add(data);
			//	if(list.contains(items.getProductId()) ){
					//List<OrderList> 
				}
			}
		}
		return result;
	}
}