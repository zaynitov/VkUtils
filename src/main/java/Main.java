import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws ClientException, ApiException, IOException, URISyntaxException {
        VkUtils.downloadPhotos();
       // FileUtil.findInContent();
        System.out.println("ASD");
    }
}
