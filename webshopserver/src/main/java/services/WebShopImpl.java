package services;

import java.util.UUID;
import java.util.logging.Logger;

import db.WebShopDB;
import io.grpc.stub.StreamObserver;
import services.WebShopGrpc.WebShop;
import services.Webshop.Availability;
import services.Webshop.Costs;
import services.Webshop.ListProductsParams;
import services.Webshop.Order;
import services.Webshop.Order.Status;
import services.Webshop.OrderId;
import services.Webshop.Payment;
import services.Webshop.Product;
import services.Webshop.ProductId;

/**
 * WebShop service implementation for gRPC.
 * @author Tobias Freundorfer
 *
 */
public class WebShopImpl implements WebShop
{
	/**
	 * The logging instance.
	 */
	private static final Logger logger = Logger.getLogger(WebShopImpl.class.getName());

	/**
	 * The demo database.
	 */
	private WebShopDB db = null;

	/**
	 * Creates a new instance of the WebShop Service Implementation.
	 */
	public WebShopImpl()
	{
		this.db = WebShopDB.getInstance();
	}

	/**
	 * Lists all products within the given limit (max replied products).
	 */
	public void listProducts(ListProductsParams request, StreamObserver<Product> responseObserver)
	{
		logger.info("### Received request for listProducts with limit " + request.getLimit() + ".");
		
		// The amount of products requested
		int limit = -1;
		if (request.getLimit() > this.db.getProducts().size())
		{
			limit = this.db.getProducts().size();
		} else
		{
			limit = request.getLimit();
		}

		for (int i = 0; i < limit; i++)
		{
			logger.info("### Responding with product id = " + this.db.getProducts().get(i).getId() + " | name = "
					+ this.db.getProducts().get(i).getName());
			responseObserver.onNext(this.db.getProducts().get(i));
		}
		responseObserver.onCompleted();
		
		logger.info("### Sent response.");
	}

	/**
	 * Checks if the given product (identified by its productId) is available.
	 */
	public void checkAvailability(ProductId request, StreamObserver<Availability> responseObserver)
	{
		// TODO: untested
		logger.info("### Received request for availability for product = " + request.getId());
		
		boolean productAvailable = false;
		for (int i = 0; i < this.db.getProducts().size(); i++)
		{
			if (this.db.getProducts().get(i).getId().equals(request.getId()))
			{
				productAvailable = true;
				break;
			}
		}
		responseObserver.onNext(Availability.newBuilder().setAvailable(productAvailable).build());
		responseObserver.onCompleted();
		
		logger.info("### Sent response with availability " + productAvailable);
	}

	/**
	 * Stores the given order details.
	 */
	public void storeOrderDetails(Order request, StreamObserver<OrderId> responseObserver)
	{
		// TODO: untested
		logger.info("### Received request for storeOrderDetails.");

		String id = UUID.randomUUID().toString();
		Order newOrder = Order.newBuilder().setId(id).addAllProducts(request.getProductsList())
				.setStatus(request.getStatus()).build();
		this.db.getOrders().add(newOrder);
		
		OrderId oi = OrderId.newBuilder().setId(id).build();
		responseObserver.onNext(oi);
		responseObserver.onCompleted();
		
		logger.info("### Sent response for id = " + id + " | orders database size = " + this.db.getOrders().size());
	}

	/**
	 * Returns order information for the requested orderId.
	 */
	public void getOrderDetails(OrderId request, StreamObserver<Order> responseObserver)
	{
		// TODO: untested
		logger.info("### Received request for getOrderDetails with orderId = " + request.getId());
		Order requestedOrder = null;
		
		for(int i=0;i<this.db.getOrders().size();i++){
			if(this.db.getOrders().get(i).getId().equals(request.getId())){
				requestedOrder = this.db.getOrders().get(i);
				break;
			}
		}
		if(requestedOrder == null){
			logger.warning("### Corresponding order for orderId = " + request.getId() + " could not be found.");
			// TODO: Evaluate how to return nothing appropriate!
			responseObserver.onNext(null);
		}
		else{
			responseObserver.onNext(requestedOrder);
		}
		responseObserver.onCompleted();
		
		logger.info("### Sent response.");
	}

	/**
	 * Cancels an order.
	 */
	public void cancelOrder(OrderId request, StreamObserver<Order> responseObserver)
	{
		// TODO untested
		logger.info("### Received request for cancelOrder with orderId = " + request.getId());

		Order requestedOrder = null;
		for(int i=0;i<this.db.getOrders().size();i++){
			if(this.db.getOrders().get(i).getId().equals(request.getId())){
				String statusBefore = this.db.getOrders().get(i).getStatus().toString();
				this.db.getOrders().get(i).toBuilder().setStatus(Status.CANCELED);
				logger.info("### Set status from " + statusBefore + " to " + this.db.getOrders().get(i).getStatus().toString());
				responseObserver.onNext(requestedOrder);
				break;
			}
		}
		
		if(requestedOrder == null){
			logger.warning("### Corresponding order for orderId = " + request.getId() + " could not be found.");
			// TODO: Evaluate how to return nothing appropriate!
			responseObserver.onNext(null);
		}
		responseObserver.onCompleted();
		
		logger.info("### Sent response.");

	}

	public void calcTransactionCosts(OrderId request, StreamObserver<Costs> responseObserver)
	{
		// TODO Auto-generated method stub

	}

	public void conductPayment(Payment request, StreamObserver<Order> responseObserver)
	{
		// TODO Auto-generated method stub

	}

	public void calcShipmentCosts(OrderId request, StreamObserver<Costs> responseObserver)
	{
		// TODO Auto-generated method stub

	}

	public void shipProducts(OrderId request, StreamObserver<Order> responseObserver)
	{
		// TODO Auto-generated method stub

	}
}