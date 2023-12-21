/*package com.spring.util;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.spring.model.Order;
import com.spring.repository.IPaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.exception.ExceptionUtils;
//import com.penna.dao.interfaces.PaymentDao;
//import com.penna.entity.CreditNoteConfirmation;
//import com.penna.dao.interfaces.SFIntegrationDao;
//import com.penna.service.interfaces.SFIntigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import com.penna.util.CommonConstant;

@Component
//Couldn't get it to work with Spring-Cloud-Config
@PropertySource("classpath:application.yml")
public class BatchJobs {

	/*private static Logger log = LogManager.getLogger(BatchJobs.class);

	@SuppressWarnings("unchecked")
	//@Scheduled(cron = "0 0/2 * * * ?")
	//@Scheduled(cron = "${name-of-the-cron:0 0/30 * * * ?}")
	@Autowired
	private IPaymentRepository repository;*/

	//@Scheduled(cron = "0 */5 * ? * *")
/*	@Scheduled(cron = "${cron.expression}")
	public JSONObject createCharge() {
		JSONObject response=new JSONObject();
		log.info("start method");  
		System.out.println("Current time is :: " + LocalDate.now());

		List<Order> list;
		try {
			Stripe.apiKey = "sk_test_51LuZLeKHclTRBfJZSeoAM6zLnf38sOt5g0NiYOOcpYKNQJ2KyQ7k1yOMgGxPJh6rulthuUQ7HaAvWnMNxnqWom1L00FcJdciuQ";
			//Charge charge = Charge.create(stripeCharge.getCharge());
			//  PaymentIntent paymentIntent = PaymentIntent.create(stripeCharge.getCharge());
			String status="failed";
			list=repository.getOrders(status);
			for(int i=0;i<list.size();i++) {
				Session session =
						  Session.retrieve(
						    list.get(i).getSessionId()
						  );
				//System.out.println(paymentIntent);
				if(session.getStatus().equals("requires_payment_method")) {
				
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());
					response.put("message",paymentIntent.getStatus()+paymentIntent.getId());
				}
				else if(paymentIntent.getStatus().equals("canceled")) {
					response.put("message","Your order was canceled"+paymentIntent.getId());
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());

				}
				else if(paymentIntent.getStatus().equals("processing")) {
					response.put("message","Your order is being processed"+paymentIntent.getId());
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());

				}
				else if(paymentIntent.getStatus().equals("failed")) {
					response.put("message","we are sorry, there was an error processing your payment, please try again with different payment method "+paymentIntent.getId());
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());

				}
				else if(paymentIntent.getStatus().equals("requires_action")) {
					response.put("message","we have sent you a text message to your registered number"+paymentIntent.getId());
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());

				}

				else {
					response.put("message",paymentIntent.getStatus()+paymentIntent.getId());
					//repository.updatStatus(list.get(i).getOrderId(),paymentIntent.getStatus(),list.get(i).getPaymentId());

				}

			}
			log.info("end method");  

			//return response;
			//  log.info("end method");  

		}     catch (StripeException e) {
			// Something else happened, completely unrelated to Stripe
			log.error("Message is: " + e.getMessage());
			//return createErrorEndResultModel(systemUserId,ERROR_VIEW_SERVICE_ERROR);
		}
		return response;


	}
}

/*public JSONObject createCh(String id) {
	 JSONObject response=new JSONObject();
	 log.info("start method");  
	 System.out.println("Current time is :: " + LocalDate.now());


	try {
		Stripe.apiKey = "sk_test_51LuZLeKHclTRBfJZSeoAM6zLnf38sOt5g0NiYOOcpYKNQJ2KyQ7k1yOMgGxPJh6rulthuUQ7HaAvWnMNxnqWom1L00FcJdciuQ";
	    //Charge charge = Charge.create(stripeCharge.getCharge());
	 //  PaymentIntent paymentIntent = PaymentIntent.create(stripeCharge.getCharge());
		PaymentIntent paymentIntent =
				  PaymentIntent.retrieve(
				    id
				  );

	    System.out.println(paymentIntent);
	    if(paymentIntent.getStatus().equals("requires_payment_method")) {
	    response.put("message","Fill your payment details");
	    }
	    if(paymentIntent.getStatus().equals("canceled")) {
		    response.put("message","Your order was canceled");

	    }
	    if(paymentIntent.getStatus().equals("processing")) {
		    response.put("message","Your order is being processed");

	    }
	    if(paymentIntent.getStatus().equals("failed")) {
		    response.put("message","we are sorry, there was an error processing your payment, please try again with different payment method ");

	    }
	    if(paymentIntent.getStatus().equals("requires_action")) {
		    response.put("message","we have sent you a text message to your registered number");

	    }

	    else {
	    	//response.put("message",paymentIntent.getStatus());
	    }


	    log.info("end method");  

	    //return response;
	  //  log.info("end method");  

	  }     catch (Exception e) {
         // Something else happened, completely unrelated to Stripe
         log.error("Message is: " + e.getMessage());
         //return createErrorEndResultModel(systemUserId,ERROR_VIEW_SERVICE_ERROR);
     }
	return response;


}*/



