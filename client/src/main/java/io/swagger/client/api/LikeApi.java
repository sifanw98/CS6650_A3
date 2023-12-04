/*
 * Album Store API
 * CS6650 Fall 2023
 *
 * OpenAPI spec version: 1.1
 * Contact: i.gorton@northeasern.edu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.ErrorMsg;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeApi {
    private ApiClient apiClient;

    public LikeApi() {
        this(Configuration.getDefaultApiClient());
    }

    public LikeApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for review
     * @param likeornot like or dislike album (required)
     * @param albumID albumID (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call reviewCall(String likeornot, String albumID,
                                               final ProgressResponseBody.ProgressListener progressListener,
                                               final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        // Verify that required parameters are not null
        if (likeornot == null || albumID == null) {
            throw new ApiException("Missing required parameters 'likeornot' or 'albumID' when calling review");
        }

        // Construct the path with the given parameters
        String localVarPath = "/albums/review/" + likeornot + "/" + albumID;

        // Prepare the query and collection query parameters
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        // Prepare the header parameters
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        final String[] localVarAccepts = {"application/json"};
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        // No content types required for GET/DELETE requests
        final String[] localVarContentTypes = {};
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        // Add network interceptors for progress listeners if they are provided
        if (progressListener != null || progressRequestListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {}; // Authentication schemes defined for the API
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, null, localVarHeaderParams, null, localVarAuthNames, progressRequestListener);
    }


    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call reviewValidateBeforeCall(String likeornot, String albumID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'likeornot' is set
        if (likeornot == null) {
            throw new ApiException("Missing the required parameter 'likeornot' when calling review(Async)");
        }
        // verify the required parameter 'albumID' is set
        if (albumID == null) {
            throw new ApiException("Missing the required parameter 'albumID' when calling review(Async)");
        }
        
        com.squareup.okhttp.Call call = reviewCall(likeornot, albumID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * 
     * like or dislike album
     * @param likeornot like or dislike album (required)
     * @param albumID albumID (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void review(String likeornot, String albumID) throws ApiException {
        reviewWithHttpInfo(likeornot, albumID);
    }

    /**
     * 
     * like or dislike album
     * @param likeornot like or dislike album (required)
     * @param albumID albumID (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> reviewWithHttpInfo(String likeornot, String albumID) throws ApiException {
        com.squareup.okhttp.Call call = reviewValidateBeforeCall(likeornot, albumID, null, null);
        return apiClient.execute(call);
    }

    /**
     *  (asynchronously)
     * like or dislike album
     * @param likeornot like or dislike album (required)
     * @param albumID albumID (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call reviewAsync(String likeornot, String albumID, final ApiCallback<Void> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = reviewValidateBeforeCall(likeornot, albumID, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
