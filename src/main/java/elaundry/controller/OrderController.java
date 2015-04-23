package elaundry.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

//import org.json.JSONArray;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import elaundry.domain.ItemDummy;
import elaundry.domain.LaundryItem;
import elaundry.domain.LaundryService;
import elaundry.domain.OrderDummy;
import elaundry.domain.OrderItem;
import elaundry.domain.SOrder;
import elaundry.service.LaundryItemService;
import elaundry.service.LaundryServiceService;
import elaundry.service.OrderItemService;
import elaundry.service.SOrderService;

@Controller
@RequestMapping("orders")
public class OrderController {
	
	@Autowired
	private LaundryServiceService laundryServiceService;
	
	@Autowired 
	private LaundryItemService laundryItemService;
	
	@Autowired
	private SOrderService orderService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@RequestMapping(value = "/placeOrder", method = RequestMethod.GET)
	public String PlaceOrderForm(Model model, HttpServletRequest request) {
		model.addAttribute("orderDummy", new OrderDummy());
		model.addAttribute("services", laundryServiceService.getLaundryServcies());
		return "placeOrder";
	}
	
	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public String ProcessOrder(Model model, @ModelAttribute("orderDummy") OrderDummy orderDummy, HttpServletRequest request) {
		
		try {
			SOrder order = new SOrder();
			order.setCustomerId(1); //should be get customerId from session
			order.setOrderStatus("Placed");
			order.setOrderDate(new Date().toString());
			order.setExpDeliveryDate(new Date().toString());
			order.setActDeliveryDate(new Date().toString());
			
			SOrder savedOrder = orderService.addOrder(order);
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			
			/*JSONArray jsonArray = new JSONArray(orderDummy.getItemList());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i);

				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(savedOrder.getOrderId());
				orderItem.setLaundryItemId(Integer.parseInt(item.get("itemId").toString()));
				orderItem.setQuantity(Integer.parseInt(item.get("quantity").toString()));
				OrderItem savedItem = orderItemService.addOrderItem(orderItem);
				orderItemList.add(savedItem);
			}
			*/
			return "redirect:/orders/viewOrder/" + savedOrder.getOrderId();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		model.addAttribute("orders", orderService.getOrders());
		return "orderList";
	}
	
	@RequestMapping(value = "/changeOrderStatus", method = RequestMethod.GET)
//	public String ChangeOrderStatus(Model model, @ModelAttribute("order") SOrder order, @Param("orderId") int orderId, HttpServletRequest request) {
	public String ChangeOrderStatus(Model model, @ModelAttribute("order") SOrder order, HttpServletRequest request) {
				
		List<SOrder> orders;
		List<OrderDummy> orderList = new ArrayList<OrderDummy>();
		
		try {
			orderService.updateOrderStatus(order.getOrderStatus(), order.getOrderId());	
			orders = orderService.getOrders();
			
			SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			for(SOrder sorder : orders) {			
				OrderDummy orderDummy = new OrderDummy();
				orderDummy.setOrderId(sorder.getOrderId());
				orderDummy.setOrderDate(sorder.getOrderDate());
				orderDummy.setExpDeliveryDate(sorder.getExpDeliveryDate());
				orderDummy.setCustomerId(sorder.getCustomerId());
				orderDummy.setOrderStatus(sorder.getOrderStatus());
				
//				System.out.println(dtFormat.format(sorder.getOrderDate()));
				/*List<OrderItem> items = orderItemService.getOrderItemsByOrderId(sorder.getOrderId());
				double totalPrice = 0;
				for(OrderItem item : items) {
					LaundryItem lItem = laundryItemService.getLaundryItemById(item.getLaundryItemId());
					totalPrice += item.getQuantity() * lItem.getPrice();
				}
				orderDummy.setTotalPrice(totalPrice);*/
				orderList.add(orderDummy);
			}				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("orders", orderList);		
		return "orderList";
	}
	
	@RequestMapping(value = "/viewOrder/{orderId}", method = RequestMethod.GET)
	public String ViewOrderForm(Model model, @PathVariable("orderId") int orderId, HttpServletRequest request) {
		model.addAttribute("order", orderService.getOrderById(orderId));
		request.setAttribute("orderId", orderId);
		//request.setAttribute("order", orderService.getOrderById(orderId));
		List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
		List<ItemDummy> items = new ArrayList<ItemDummy>();
		
		int totalQty = 0, totalAmount = 0;
		for(OrderItem item : orderItems) {			
			ItemDummy itemDummy = new ItemDummy();
			itemDummy.setOrderId(orderId);
			itemDummy.setQuantity((int)item.getQuantity());
			
			LaundryItem laundryItem = laundryItemService.getLaundryItemById(item.getLaundryItemId());
			if(laundryItem != null) {
				itemDummy.setTotalPrice(item.getQuantity() * laundryItem.getPrice());			
				itemDummy.setLaundryItemId(laundryItem.getItemId());
				itemDummy.setItemName(laundryItem.getName());
				itemDummy.setServiceId(laundryItem.getServiceId());
				itemDummy.setPrice(laundryItem.getPrice());
				
				LaundryService laundryService = laundryServiceService.getLaundryServiceById(laundryItem.getServiceId());
				itemDummy.setServiceName((laundryService != null) ? laundryService.getName() : "");
			}
			
			totalQty += item.getQuantity();
			totalAmount += itemDummy.getTotalPrice();
			
			items.add(itemDummy);
		}
		request.setAttribute("orderItems", items);
		request.setAttribute("totalItems", totalQty);
		request.setAttribute("totalAmount", totalAmount);
		return "viewOrder";
	}
}
