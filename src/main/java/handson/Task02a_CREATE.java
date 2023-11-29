package handson;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import handson.impl.ApiPrefixHelper;
import handson.impl.CustomerService;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;


/**
 * Configure sphere client and get project information.
 */
public class Task02a_CREATE {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        /**
         * TODO:
         * UPDATE the ApiPrefixHelper with your prefix from dev.properties (e.g. "mh-dev-admin.")
         */
        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        Logger logger = LoggerFactory.getLogger(Task02a_CREATE.class.getName());
        final ProjectApiRoot client = createApiClient(apiClientPrefix);
        CustomerService customerService = new CustomerService(client);

        ApiHttpResponse<Customer> existingCustomer = customerService.getCustomerByKey("customer-JB-Adam")
                .toCompletableFuture().get();

        if (existingCustomer != null) {
            logger.info("Customer fetch: {}", existingCustomer.getBody().getEmail());
        } else {
            ApiHttpResponse<Customer> createdCustomer = customerService.createCustomer(
                            "some_email@commercetools.com",
                            "password11!1#",
                            "customer-JB-Adam",
                            "Adam",
                            "Bond",
                            "DE"
                    ).thenComposeAsync(singInResult -> customerService.createEmailVerificationToken(singInResult, 5))
                    .thenComposeAsync(customerService::verifyEmail)
                    .toCompletableFuture().get();

            logger.info("Customer created: {}", createdCustomer.getBody());
        }

        client.close();
    }
}
