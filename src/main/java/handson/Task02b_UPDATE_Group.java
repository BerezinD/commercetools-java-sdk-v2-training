package handson;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import handson.impl.ApiPrefixHelper;
import handson.impl.ClientService;
import handson.impl.CustomerService;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;

/**
 * Configure sphere client and get project information.
 * <p>
 * See:
 *  TODO dev.properties
 *  TODO {@link ClientService#createApiClient(String prefix)}
 */
public class Task02b_UPDATE_Group {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        Logger logger = LoggerFactory.getLogger(Task02b_UPDATE_Group.class.getName());
        final ProjectApiRoot client = createApiClient(apiClientPrefix);
        CustomerService customerService = new CustomerService(client);

        // TODO:
        //  GET a customer
        //  GET a customer group
        //  ASSIGN the customer to the customer group
        //
        ApiHttpResponse<Customer> customerApiHttpResponse = customerService
                .getCustomerByKey("customer-JB-Adam")
                .thenCombineAsync(
                        customerService.getCustomerGroupByKey(""),
                        customerService::assignCustomerToCustomerGroup
                )
                .thenComposeAsync(CompletableFuture::toCompletableFuture)
                .exceptionally(throwable -> {
                    logger.info(throwable.getLocalizedMessage());
                    return null;
                })
                .toCompletableFuture().get();

        logger.info("Customer assigned to group: {}", customerApiHttpResponse.getBody().getEmail());
        client.close();
    }
}

