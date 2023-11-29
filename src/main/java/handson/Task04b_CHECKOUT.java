package handson;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.channel.Channel;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderState;
import com.commercetools.api.models.state.State;
import handson.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;


/**
 * Create a cart for a customer, add a product to it, create an order from the cart and change the order state.
 * See:
 */
public class Task04b_CHECKOUT {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        final ProjectApiRoot client = createApiClient(apiClientPrefix);

        CustomerService customerService = new CustomerService(client);
        CartService cartService = new CartService(client);
        OrderService orderService = new OrderService(client);
        PaymentService paymentService = new PaymentService(client);
        Logger logger = LoggerFactory.getLogger(Task04b_CHECKOUT.class.getName());

        // TODO: Fetch a channel if your inventory mode will not be NONE
        //
        Channel channel = client
                .channels()
                .get()
                .withQuery(q -> q.key().is("inventory-channel"))
                .execute()
                .toCompletableFuture().get()
                .getBody().getResults().get(0);

        final State state = client
                .states()
                .withKey("mhOrderPacked")
                .get()
                .execute()
                .toCompletableFuture()
                .get()
                .getBody();

        // TODO: Perform cart operations:
        //      Get Customer, create cart, add products, add inventory mode
        //      add discount codes, perform a recalculation
        // TODO: Convert cart into an order, set order status, set state in custom work
        //
        // TODO: add payment
        // TAKE CARE: Take off payment for second or third try OR change the interfaceID with a timestamp
        //
        // TODO additionally: add custom line items, add shipping method
        //
        Order order = customerService.getCustomerByKey("customer-JB-Adam")
                .thenComposeAsync(cartService::createCart)
                .thenComposeAsync(cartApiHttpResponse -> cartService.addProductToCartBySkusAndChannel(
                        cartApiHttpResponse,
                        channel,
                        "WTP-09"
                ))
                .thenComposeAsync(cartApiHttpResponse -> cartService.addDiscountToCart(cartApiHttpResponse, "BONGO"))
                .thenComposeAsync(cartService::recalculate)
                .thenComposeAsync(cartService::setShipping)
                .thenComposeAsync(cartApiHttpResponse ->
                        paymentService.createPaymentAndAddToCart(
                                cartApiHttpResponse,
                                "psp_name",
                                "psp_credit_card",
                                "psp_pay" + Math.random(),
                                "psp_interaction" + Math.random()
                        ))
                .thenComposeAsync(orderService::createOrder)
                .thenComposeAsync(orderApiHttpResponse -> orderService.changeState(orderApiHttpResponse, OrderState.COMPLETE))
                .thenComposeAsync(orderApiHttpResponse -> orderService.changeWorkflowState(orderApiHttpResponse, state))
                .toCompletableFuture().get()
                .getBody();

        logger.info("Created cart/order ID: {}", order.getId());

        client.close();
    }
}
