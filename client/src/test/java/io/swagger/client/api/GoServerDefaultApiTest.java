package io.swagger.client.api;

 import io.swagger.client.model.AlbumInfo;
 import io.swagger.client.model.AlbumsProfile;
 import io.swagger.client.model.ErrorMsg;
 import io.swagger.client.ApiClient;

 import io.swagger.client.ApiResponse;
 import static org.junit.Assert.assertTrue;

 import java.io.File;
 import io.swagger.client.model.ImageMetaData;
 import org.junit.Test;
 import org.junit.Ignore;
 import org.junit.Before;
 import org.junit.Assert;
 
 
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

public class GoServerDefaultApiTest {

    private final DefaultApi api = new DefaultApi();
    private final String BASE_PATH_GO_SERVER = "http://ec2-35-85-34-139.us-west-2.compute.amazonaws.com:8080/IGORTON/AlbumStore/1.0.0";

    @Before
    public void setup() {
        ApiClient client = new ApiClient();
        client.setBasePath(BASE_PATH_GO_SERVER);
        api.setApiClient(client);
    }

    @Test
    public void getAlbumByKeyTest() throws Exception {
        String albumID = "12345";  // replace with the desired albumID
        ApiResponse<AlbumInfo> response = api.getAlbumByKeyWithHttpInfo(albumID); // Use the method that returns ApiResponse
        int statusCode = response.getStatusCode();
        Assert.assertEquals("Expected status code to be 200 (OK).", 200, statusCode); // You can adjust the expected status code as necessary
    }

    @Test
    public void newAlbumTest() throws Exception {
        File image = new File("/Users/sifanwei/Downloads/nmtb.png");
        AlbumsProfile profile = new AlbumsProfile();  // replace or populate as needed
        ApiResponse<ImageMetaData> response = api.newAlbumWithHttpInfo(image, profile);
    
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 200 || statusCode == 201);
    }
}
