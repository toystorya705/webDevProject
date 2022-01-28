package advanceWebDev;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

/**Argos Scraper Class*/
public class ArgosProductsData extends WebScraper {


    int count = 0;
    int pageCount = 1;
    boolean checkNextButton = false;
    int productCount = 0;
    volatile private boolean runThread = false;
    private String searchTerm;
    private int timeDelay;
    private ProductDao productListData;
    private List<WebElement> productList;

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    /** run thread start scraping*/
    @Override
    public void run() {

        runThread = true;
        while (runThread) {
            //We need an options class to run headless - not needed if we want default options
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(false);
            //Create instance of web driver - this must be on the path.
            WebDriver driver = new ChromeDriver(options);
            //Navigate Chrome to page.

            driver.get("https://www.argos.co.uk/search/drone/?clickOrigin=searchbar:search:term:" + searchTerm);

            driver.findElement(By.cssSelector("button[class=\"button right\"]")).click();// click cookie button
            productList = driver.findElements(By.className("sm-4"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            int timeCount = 10;
            while (timeCount > 0) {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                js.executeScript("window.scrollBy(0,Math.round(document. body. scrollHeight)/10)");// To Scroll the window till end to get data
                timeCount--;
            }

                productCount += productList.size();
                System.out.println(productCount);


                System.out.println("page count   " + pageCount);

                if (checkNextButton != true) {// If next button is false
                    pageCount++;
                    for (int i = 0; i < productList.size(); i++) {

                        try {

                            WebElement productPrice = productList.get(i).findElement(By.cssSelector("strong"));
                            WebElement productTitle = productList.get(i).findElement(By.cssSelector("a[class=\"ProductCardstyles__Title-h52kot-12 PQnCV\"]"));
                            WebElement imageLink = productList.get(i).findElement(By.className("fCXimS")).findElement(By.tagName("img"));

/**
 * Cleaning&excluding the irrelevant data
 * */
                            String name = productTitle.getText().replaceAll("[!@#$%/&:*()_+=|<>?{},''\\\\[\\\\]~-]", " ").replaceAll("\"", " ").toLowerCase();
                            String[] nameModify = name.split(" ");
                            if (!name.contains("combo") && !name.contains("modifli") && !name.contains("arm") && !name.contains("braces") && !name.contains("skin") && !name.contains("kit") && !name.contains("backpack") && !name.contains("propellers") && !name.contains("pad") && !name.contains("masterclass") && !name.contains("classes") && !name.contains("handbook") && !name.contains("lens") && !name.contains("lander") && !name.contains("bag") && !name.contains("incase") && !name.contains("replacement") && !name.contains("part") && !name.contains("guard") && !name.contains("stick") && !name.contains("extension") && !name.contains("part") && !name.contains("set") && !name.contains("case") && !name.contains("osmo") && !name.contains("pocket") && !name.contains("accessory") && !name.contains("cable") && !name.contains("accessories") && !name.contains("intelligent") && !name.contains("replace") && !name.contains("spare") && !name.contains("trainers") && !name.contains("trainer") && !name.contains("goggles") && !name.contains("goggle") && !name.contains("mobile") && !name.contains("extra remote") && !name.contains("remote controller") && !name.contains("service") && !name.contains("1550t") && !name.contains("battery charging") && !name.contains("megaphone") && !name.contains("fpv motion controller") && !name.contains("motion controller") && !name.contains("walle") && !name.contains("hskrc") && !name.contains("ing") && !name.contains("ball") && !name.contains("card") && !name.contains("care") && !name.contains("refresh") && !name.contains("tiktok") && !name.contains("lee") && !name.contains("eagle") && !name.contains("eye") && !name.contains("snap") && !name.contains("robomaster") && !name.contains("helicopter") && !name.contains("immersionrc") && !name.contains("cover") && !name.contains("shell") && !name.contains("battery") && !name.contains("000611") && !name.contains("label") && !name.contains("labels") && !name.contains("user")&& !name.contains("lock")&& !name.contains("dji rc")) {
                                String nameModifRemoveWords = (nameModify.length >= 4 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] + " " + nameModify[3] : nameModify.length == 3 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] : nameModify.length == 2 ? nameModify[0] + " " + nameModify[1] : nameModify[0]).replaceAll("camera", "").replaceAll("4k", "").replaceAll("/", "").replaceAll("1080p", "").replaceAll("gps", "").replaceAll("any", "").replaceAll("standard", "").replaceAll("2k", "").replaceAll("fly", "").replaceAll("eu", "").replaceAll("brand", "").replaceAll("  ", " ").replaceAll("unit", "").replaceAll("only", "");
                                if (nameModifRemoveWords.contains("dji") || nameModifRemoveWords.contains("mavic"))
                                    nameModifRemoveWords = nameModifRemoveWords.replaceAll("drone", "").replaceAll("zoom", "").replaceAll("enterprise", "").replaceAll("new", "").replaceAll("ultralight", "").replaceAll("unactivated", "").replaceAll("more", "");

                                System.out.println(nameModifRemoveWords);
                                System.out.println((int) Math.round(Double.parseDouble(productPrice.getText().replaceAll("[!@#$%&*()_+=|<>?{}£,''\\\\[\\\\]~-]", ""))));
                                System.out.println(productTitle.getAttribute("href"));// Get link from title
                                System.out.println(imageLink.getAttribute("src"));
                                ProductData product = new ProductData();
                                product.setName(nameModifRemoveWords);
                                product.setImageUrl((imageLink.getAttribute("src")));

                                ProductData productMapped = productListData.findProduct(product);

                                ProductComparedUrl productCompared = new ProductComparedUrl();
                                productCompared.setProduct(productMapped);
                                productCompared.setPrice((int) Math.round(Double.parseDouble(productPrice.getText().replaceAll("[!@#$%&*()_+=|<>?{}£,''\\\\[\\\\]~-]", ""))));
                                productCompared.setUrl(productTitle.getAttribute("href"));

                                productListData.saveProductCompared(productCompared);
                            }

                        } catch (Exception e) {
                            System.out.println("Error gets while finding product of index " + e);
                        }
//
                    }

                    //Exit driver and close Chrome
                    driver.quit();
                    try {
                        sleep(timeDelay);
                        // driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("This is product count   " + ++productCount);
                }



            stopScraping();


        }
    }


    public void setProductDao(ProductDao productListData) {
        this.productListData = productListData;
    }

    public ProductDao getProductDao() {
        return productListData;
    }

    public List<WebElement> getProductList() {
        return productList;
    }

    public void stopScraping() {
        runThread = false;
    }
}
