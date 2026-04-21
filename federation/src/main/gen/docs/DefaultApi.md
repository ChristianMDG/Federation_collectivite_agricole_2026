# DefaultApi

All URIs are relative to *https://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**collectivitiesPost**](DefaultApi.md#collectivitiesPost) | **POST** /collectivities | Create list of collectivities |
| [**membersPost**](DefaultApi.md#membersPost) | **POST** /members | Create list of members |


<a id="collectivitiesPost"></a>
# **collectivitiesPost**
> List&lt;Collectivity&gt; collectivitiesPost(createCollectivity)

Create list of collectivities

During collectivity request body creation, only members ID are provided to identify members.  But the response content includes members information, not only their IDs. 

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost:8080");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    List<CreateCollectivity> createCollectivity = Arrays.asList(); // List<CreateCollectivity> | 
    try {
      List<Collectivity> result = apiInstance.collectivitiesPost(createCollectivity);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#collectivitiesPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **createCollectivity** | [**List&lt;CreateCollectivity&gt;**](CreateCollectivity.md)|  | [optional] |

### Return type

[**List&lt;Collectivity&gt;**](Collectivity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | List of created collectivities |  -  |
| **400** | Collectivity without federation approval or structure missing. |  -  |
| **404** | Member not found. |  -  |

<a id="membersPost"></a>
# **membersPost**
> List&lt;Member&gt; membersPost(createMember)

Create list of members

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost:8080");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    List<CreateMember> createMember = Arrays.asList(); // List<CreateMember> | 
    try {
      List<Member> result = apiInstance.membersPost(createMember);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#membersPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **createMember** | [**List&lt;CreateMember&gt;**](CreateMember.md)|  | [optional] |

### Return type

[**List&lt;Member&gt;**](Member.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | List of created members |  -  |
| **400** | The following case must return a bad request exception :  - Member with bad referees or without proper payment. - Membership dues not paid or registration fee not paid.  |  -  |
| **404** | Either collectivity or member not found. |  -  |

