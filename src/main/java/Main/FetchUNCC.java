package Main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.awt.image.BufferedImage;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.Screenshot;
import org.openqa.selenium.WebDriver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.By;
import javax.imageio.ImageIO;
import java.io.File;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.AShot;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.concurrent.TimeUnit;

public class FetchUNCC {
    public static String[] openInfo(String location) throws IOException, InterruptedException {
        String dineOnCampusUrl = "https://api.dineoncampus.com/v1/locations/status?site_id=5751fd2790975b60e0489226&platform=0";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dineOnCampusUrl))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JSONTokener tokener = new JSONTokener(response.body());
        JSONArray locations = new JSONObject(tokener).getJSONArray("locations");
        boolean found = false;
        int index = 0;
        for (; index < locations.length(); index++) {
            JSONObject obj = locations.getJSONObject(index);
            if (obj.getString("name").equals(location)) {
                String label = obj.getJSONObject("status").getString("label");
                String message = obj.getJSONObject("status").getString("message");
                return new String[] {label, message};
            }
        }
        return new String[] {"", ""};
    }

    public void screenshot() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(3L);
        System.out.println(
                "\n********************************\n***   Fetching UNCC Pages   ***\n********************************\n");
        System.setProperty("webdriver.gecko.driver", "chromedriver");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        ChromeOptions soviOptions = new ChromeOptions().addArguments("--headless=new");
        String soviLabel = FetchUNCC.openInfo("SoVi")[0].toLowerCase();
        if(soviLabel.equals("open")) {
            WebDriver sovi = new ChromeDriver(soviOptions);
            String soviurl = "https://app.safespace.io/api/display/live-occupancy/15da3cfa?view=percent";
            sovi.get(soviurl);
            sovi.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
            Screenshot s = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(sovi);
            ImageIO.write(s.getImage(), "PNG", new File("./img/sovi.png"));
            sovi.close();
            sovi.quit();
        }

        ChromeOptions crownOptions = new ChromeOptions().addArguments("--headless=new");
        String crownLabel = FetchUNCC.openInfo("Social 704")[0].toLowerCase();
        if(crownLabel.equals("open")) {
            WebDriver crown = new ChromeDriver(crownOptions);
            String crownurl = "https://app.safespace.io/api/display/live-occupancy/7a9c0a24?view=percent";
            crown.get(crownurl);
            crown.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
            Screenshot c = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(crown);
            ImageIO.write(c.getImage(), "PNG", new File("./img/crown.png"));
            crown.close();
            crown.quit();
        }

        ChromeOptions parkingOptions = new ChromeOptions().addArguments("--headless=new").addArguments("--window-size=1920,1080");
        WebDriver parking = new ChromeDriver(parkingOptions);
        String parkingurl = "https://parkingavailability.charlotte.edu/";
        parking.get(parkingurl);
        parking.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
        WebElement list = parking.findElement(By.className("mat-list"));
        Screenshot p = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(parking);
        BufferedImage listScreenshot = p.getImage().getSubimage(list.getLocation().getX(), list.getLocation().getY(),
                list.getSize().getWidth() / 2, (int) (list.getSize().getHeight() / 1.7));
        ImageIO.write(listScreenshot, "PNG", new File("./img/parking.png"));
        parking.navigate().refresh();
        parking.close();
        parking.quit();

        listScreenshot.flush();
        System.out.println(
                "\n********************************\n*** Done Fetching UNCC Pages ***\n********************************\n");
    }
}
