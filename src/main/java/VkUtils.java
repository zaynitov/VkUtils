import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoAlbumFull;
import com.vk.api.sdk.objects.photos.responses.GetAlbumsResponse;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


@Slf4j
public class VkUtils {
    private static String code = "3a518f2ea0acb87780";
    private static Long add = 1L;

    public static void downloadPhotos() throws ClientException, ApiException, IOException {
        Properties properties = getProperties();
        Integer appId = Integer.valueOf(properties.getProperty("application.id"));
        String clientSecret = properties.getProperty("client.secret");
        Integer ownerHaratsGroup = Integer.valueOf(properties.getProperty("owner.harats.group"));

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        String REDIRECT_URI = "";
        UserAuthResponse authResponse = vk.oauth()
                .userAuthorizationCodeFlow(appId, clientSecret, REDIRECT_URI, code)
                .execute();
        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        GetAlbumsResponse photosGetQuery = vk.photos().getAlbums(actor).ownerId(ownerHaratsGroup).execute();
        List<String> albumIds = photosGetQuery.getItems().stream().map(PhotoAlbumFull::getId).map(Object::toString).collect(Collectors.toList());
        System.out.println("{} albums in group"+ albumIds.size());
        for (int i = 0; i < albumIds.size(); i++) {
            if (i<100) continue;
            System.out.println("{} albums in process"+ i);
            GetResponse execute = vk.photos().get(actor).ownerId(ownerHaratsGroup).albumId(albumIds.get(i)).execute();
            List<Photo> items = execute.getItems();
            for (Photo item : items) {
                String maxPhotoUrl = getMaxPhoto(item);
                URL url = new URL(maxPhotoUrl);
                BufferedImage img = ImageIO.read(url);
                File file = new File("D:\\photos\\" + add++ + ".jpg");
                ImageIO.write(img, "jpg", file);
            }
            if (i>300) break;
        }
    }

    private static Properties getProperties() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
        Properties property = new Properties();
        property.load(fis);
        return property;
    }

    private static String getMaxPhoto(Photo item) {
        if (item.getPhoto2560() != null) {
            return item.getPhoto2560();
        }
        if (item.getPhoto1280() != null) {
            return item.getPhoto1280();
        }
        if (item.getPhoto807() != null) {
            return item.getPhoto807();
        }
        return "";
    }
}
