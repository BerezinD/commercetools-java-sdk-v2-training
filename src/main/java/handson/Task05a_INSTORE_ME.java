package handson;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraftBuilder;
import handson.impl.ApiPrefixHelper;
import handson.impl.ClientService;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;


/**
 *
 */
public class Task05a_INSTORE_ME {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger(Task05a_INSTORE_ME.class.getName());

        // TODO: Create in-store cart with global API client
        //  Provide an API client with global permissions
        //  Provide a customer who is restricted to a store
        //  Note: A global cart creation should fail but an in-store cart should world
        //
        final String globalApiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();
        final String projectKey = getProjectKey(globalApiClientPrefix);
        final ProjectApiRoot client = createApiClient(globalApiClientPrefix);

        try (ProjectApiRoot apiHttpClient = ClientService.projectApiRoot) {
            ApiHttpResponse<Cart> cartResponse = client.withProjectKey(projectKey)
                    .inStoreKeyWithStoreKeyValue("berlin-store")
                    .carts()
                    .post(
                            CartDraftBuilder.of()
                                    .deleteDaysAfterLastModification(90L)
                                    .customerId("7014afc5-210f-4be9-955f-c3707142f325")
                                    .currency("EUR")
                                    .customerEmail("nagesh@dtest.com")
                                    .build()
                    )
                    .execute()
                    .toCompletableFuture().get();
            logger.info("Created in-store cart with a global api client: {}", cartResponse.getBody().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: Create in-store Cart with in-store API client
        //  Update the ApiPrefixHelper with the prefix for Store API Client
        //  Provide an API client with scope limited to a store
        //  Provide a customer with only store permissions
        //  Try creating a global cart with a global customer and check the error message

//        final String storeApiClientPrefix = ApiPrefixHelper.API_STORE_CLIENT_PREFIX.getPrefix();
//        final String projectKey = getProjectKey(storeApiClientPrefix);
//        final String storeKey = getStoreKey(storeApiClientPrefix);
//        final ApiRoot client = createApiClient(storeApiClientPrefix);
//
//
//        logger.info("Created in-store cart with a store api client: "+
//                client.withProjectKey(projectKey)
//                        .inStoreKeyWithStoreKeyValue(storeKey)
//                        .carts()
//                        .post(
//                                CartDraftBuilder.of()
//                                        .deleteDaysAfterLastModification(90L)
//                                        .customerId("82cf41b6-3030-4fb1-86ac-d8be40d34878")
//                                        .currency("EUR")
//                                        .customerEmail("nagesh@test.com")
//                                        .build()
//                        )
//                        .execute()
//                        .toCompletableFuture().get()
//                        .getBody().getId()
//        );

        // TODO
        //  Visit impex to verify that the carts are holding the same information
        //
        // TODO: Create a cart via /me endpoint
        //  Provide API client with SPA for customer with global permissions
        //  Update the ApiPrefixHelper with the prefix for Me(SPA) API Client
        //  You can also create in-store customer-bound cart
        //  Visit impex to inspect the carts created

//        final String meApiClientPrefix = ApiPrefixHelper.API_ME_CLIENT_PREFIX.getPrefix();
//        final String projectKey = getProjectKey(meApiClientPrefix);
//        final ApiRoot meClient = createMeTokenApiClient(meApiClientPrefix);
//        final String customerEmail = getCustomerEmail(meApiClientPrefix);
//
//        logger.info("Get cart for customer via me endpoint: " +
//                meClient
//                        .withProjectKey(projectKey)
//                        //.inStoreKeyWithStoreKeyValue("berlin-store")
//                        .me()
//                        .carts()
//                        .post(
//                                MyCartDraftBuilder.of()
//                                        .currency("EUR")
//                                        .deleteDaysAfterLastModification(90l)
//                                        .customerEmail(customerEmail)
//                                        .build()
//                        )
//                        .execute()
//                        .exceptionally(throwable -> {
//                            logger.info(throwable.getLocalizedMessage().toString());
//                            return null;
//                        })
//                        .toCompletableFuture().get()
//                        .getBody().getId()
//        );

        // TODO: Create in-store customer-bound Cart with in-store-me API client
        //  Update the ApiPrefixHelper with the prefix for Me(SPA) API Client
        //  Provide in-store-me API client with scope for a store and me endpoint
        //  Try creating a global cart without me and check the error message
        //  Visit impex to inspect the carts created

//        final String storeMeApiClientPrefix = ApiPrefixHelper.API_STORE_ME_CLIENT_PREFIX.getPrefix();
//        final String projectKey = getProjectKey(storeMeApiClientPrefix);
//        final ApiRoot storeClient = createStoreMeApiClient(storeMeApiClientPrefix);
//        final String storeKey = getStoreKey(storeMeApiClientPrefix);
//        final String storeCustomerEmail = getCustomerEmail(storeMeApiClientPrefix);
//
//        logger.info("Created in-store cart with a store api client: "+
//                storeClient.withProjectKey(projectKey)
//                        .inStoreKeyWithStoreKeyValue(storeKey)
//                        .me()
//                        .carts()
//                        .post(
//                                MyCartDraftBuilder.of()
//                                        .deleteDaysAfterLastModification(90L)
//                                        .currency("EUR")
//                                        .customerEmail(storeCustomerEmail)
//                                        .build()
//                        )
//                        .execute()
//                        .exceptionally(throwable -> {
//                            logger.info(throwable.getLocalizedMessage().toString());
//                            return null;
//                        })
//                        .toCompletableFuture().get()
//                        .getBody().getId()
//        );
    }
}
