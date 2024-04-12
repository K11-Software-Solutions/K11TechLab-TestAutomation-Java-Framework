package com.k11.automation.webservices.baseclasses.filters;

import io.restassured.builder.ResponseBuilder;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.webservices.baseclasses.BaseWebServiceTest;
import org.hamcrest.Matchers;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Makes Rest Assured parallel friendly.
 */
public class ParallelRequestFilter implements OrderedFilter {

    public static final ThreadLocal<HashMap<String, Response>> THREAD_RESPONSE = ThreadLocal.withInitial(HashMap::new);
    private boolean log;

    public ParallelRequestFilter(boolean log)
    {
        this.log = log;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

        long currentThreadId = Thread.currentThread().getId();
        String methodName = BaseWebServiceTest.METHOD_THREAD.get().get(currentThreadId);
        Log.LOGGER.info(MessageFormat.format("Filter Request for threadId {0}",currentThreadId));

        logRequest(requestSpec);

        Response response = ctx.next(requestSpec, responseSpec);
        THREAD_RESPONSE.get().put(methodName, response);

        logResponse(methodName);
        Log.LOGGER.info(MessageFormat.format("Filter Request DONE for threadId = {0} and method {1}", currentThreadId, methodName));
        return THREAD_RESPONSE.get().get(methodName);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * Prints the Request is log is true.
     */
    private void logRequest(FilterableRequestSpecification requestSpec)
    {
        if(!log) {
            return;
        }

        Log.LOGGER.info("Logging Request");
        String loggedMessage = RequestPrinter.print(requestSpec, requestSpec.getMethod(), requestSpec.getURI(), LogDetail.ALL, System.out, true);
        Log.info(MessageFormat.format("Web Service Request : {0}" ,loggedMessage), true);
        Log.LOGGER.info("Logging Request Done");
    }

    /**
     * Prints response.
     * @param methodName the method name
     * @return
     */
    private Response logResponse(String methodName)
    {

        Response response = THREAD_RESPONSE.get().get(methodName);
        if(!log) {
            return response;
        }

        final int statusCode = response.statusCode();
        String responseMessage = "";
        if (Matchers.any(Integer.class).matches(statusCode)) {

            responseMessage = ResponsePrinter.print(response, response, System.out, LogDetail.ALL, true);
            final byte[] responseBody = response.asByteArray();
            response = cloneResponseIfNeeded(response, responseBody);
        }
        Log.info(MessageFormat.format("Web Service Response : {0}", responseMessage), true);
        return response;
    }

    /*
     * If body expectations are defined we need to return a new Response otherwise the stream
     * has been closed due to the logging.
     */
    private Response cloneResponseIfNeeded(Response response, byte[] responseAsString) {
        Log.LOGGER.info("Cloning Response");
        if (responseAsString != null && response instanceof RestAssuredResponseImpl && !((RestAssuredResponseImpl) response).getHasExpectations()) {
            final Response build = new ResponseBuilder().clone(response).setBody(responseAsString).build();
            ((RestAssuredResponseImpl) build).setHasExpectations(true);
            return build;
        }
        Log.LOGGER.info("Cloning Response Done");
        return response;
    }
}
