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

public class JavaServerDefaultApiTest {

    private final DefaultApi api = new DefaultApi();
    private final String BASE_PATH_JAVA_SERVER = "http://ec2-35-91-241-164.us-west-2.compute.amazonaws.com:8080/albumstore";

    @Before
    public void setup() {
        ApiClient client = new ApiClient();
        client.setBasePath(BASE_PATH_JAVA_SERVER);
        api.setApiClient(client);
    }

    @Test
    public void getAlbumByKeyTest() throws Exception {
        String albumID = "1";  // replace with the desired albumID
        AlbumInfo response = api.getAlbumByKey(albumID);
        Assert.assertNotNull("Response should not be null.", response);
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
