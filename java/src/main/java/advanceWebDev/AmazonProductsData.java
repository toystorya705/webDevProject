package advanceWebDev;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

import java.util.List;

/**
 * Amazon Scraper Class
 */
public class AmazonProductsData extends WebScraper {

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

    /**
     * run thread start scraping
     */
    @Override
    public void run() {
        runThread = true;


        while (runThread) {
            //We need an options class to run headless - not needed if we want default options
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(false);
            //Create instance of web driver - this must be on the path.
            WebDriver driver = new ChromeDriver(options);

            while (!checkNextButton) {


                driver.get("https://www.amazon.co.uk/s?k=" + searchTerm + "&page=" + pageCount + "&crid=E3RAY21X1FQA&qid=1636037300&sprefix=drone%2Caps%2C65&ref=sr_pg_2");
                if (pageCount == 1)
                    driver.findElement(By.cssSelector("input[class=\"a-button-input celwidget\"]")).click();//click cookie button


                productList = driver.findElements(By.className("s-asin"));

                productCount += productList.size();
                System.out.println(productCount);

                List<WebElement> pageCheck = driver.findElements(By.className("a-disabled"));// Stores the NEXT button div tag

                for (WebElement object : pageCheck) {
                    if (object.getText().equals("Next→")) {// If it finds div tag of class of next button  will set checkNextButton to true
                        System.out.println(object.getText());
                        System.out.println("Working");
                        checkNextButton = true;
                    }
                }


                System.out.println("page count   " + pageCount);
                pageCount++;


                for (int i = 0; i < productList.size(); i++) {

                    try {

                        WebElement productPrice = productList.get(i).findElement(By.cssSelector("span[class=\"a-price-whole\"]"));
                        WebElement productTitle = productList.get(i).findElement(By.cssSelector("a[class=\"a-link-normal a-text-normal\"]"));
                        WebElement imageLink = productList.get(i).findElement(By.cssSelector(("img[class=\"s-image\"]")));
                        /**
                         * Cleaning&excluding the irrelevant data
                         * */

                        String name = productTitle.getText().replaceAll("[!@#$%&:*()_+=|<>?{},''\\\\[\\\\]~-]", " ").replaceAll("\"", " ").toLowerCase();
                        String[] nameModify = name.split(" ");
                        if (!name.contains("combo") && !name.contains("modifli") && !name.contains("arm") && !name.contains("braces") && !name.contains("skin") && !name.contains("kit") && !name.contains("backpack") && !name.contains("propellers") && !name.contains("pad") && !name.contains("masterclass") && !name.contains("classes") && !name.contains("handbook") && !name.contains("lens") && !name.contains("lander") && !name.contains("bag") && !name.contains("incase") && !name.contains("replacement") && !name.contains("part") && !name.contains("guard") && !name.contains("stick") && !name.contains("extension") && !name.contains("part") && !name.contains("set") && !name.contains("case") && !name.contains("osmo") && !name.contains("pocket") && !name.contains("accessory") && !name.contains("cable") && !name.contains("accessories") && !name.contains("intelligent") && !name.contains("replace") && !name.contains("spare") && !name.contains("trainers") && !name.contains("trainer") && !name.contains("goggles") && !name.contains("goggle") && !name.contains("mobile") && !name.contains("extra remote") && !name.contains("remote controller") && !name.contains("service") && !name.contains("1550t") && !name.contains("battery charging") && !name.contains("megaphone") && !name.contains("fpv motion controller") && !name.contains("motion controller") && !name.contains("walle") && !name.contains("hskrc") && !name.contains("ing") && !name.contains("ball") && !name.contains("card") && !name.contains("care") && !name.contains("refresh") && !name.contains("tiktok") && !name.contains("lee") && !name.contains("eagle") && !name.contains("eye") && !name.contains("snap") && !name.contains("robomaster") && !name.contains("helicopter") && !name.contains("immersionrc") && !name.contains("cover") && !name.contains("shell") && !name.contains("battery") && !name.contains("000611") && !name.contains("label") && !name.contains("labels") && !name.contains("user")&& !name.contains("lock")&& !name.contains("dji rc")) {
                            ProductData product = new ProductData();
                            String nameModifRemoveWords = (nameModify.length >= 4 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] + " " + nameModify[3] : nameModify.length == 3 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] : nameModify.length == 2 ? nameModify[0] + " " + nameModify[1] : nameModify[0]).replaceAll("camera", "").replaceAll("4k", "").replaceAll("/", "").replaceAll("1080p", "").replaceAll("gps", "").replaceAll("any", "").replaceAll("standard", "").replaceAll("2k", "").replaceAll("fly", "").replaceAll("eu", "").replaceAll("brand", "").replaceAll("  ", " ").replaceAll("unit", "").replaceAll("only", "");
                            if (nameModifRemoveWords.contains("dji") || nameModifRemoveWords.contains("mavic"))
                                nameModifRemoveWords = nameModifRemoveWords.replaceAll("drone", "").replaceAll("zoom", "").replaceAll("enterprise", "").replaceAll("new", "").replaceAll("ultralight", "").replaceAll("unactivated", "").replaceAll("more", "");
                            System.out.println(nameModifRemoveWords);
                            System.out.println((int) Math.round(Double.parseDouble(productPrice.getText().replace("£", ""))));
                            System.out.println(productTitle.getAttribute("href"));// Get link from title
                            System.out.println(imageLink.getAttribute("src"));

                            product.setName(nameModifRemoveWords);
                            product.setImageUrl((imageLink.getAttribute("src")));
                            ProductData productMapped = productListData.findProduct(product);

                            ProductComparedUrl productCompared = new ProductComparedUrl();
                            productCompared.setProduct(productMapped);
                            productCompared.setPrice((int) Math.round(Double.parseDouble(productPrice.getText().replace("£", ""))));
                            productCompared.setUrl(productTitle.getAttribute("href"));
                            productListData.saveProductCompared(productCompared);

                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }


                System.out.println("This is product count   " + ++productCount);

                try {
                    Thread.sleep(timeDelay);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            stopScraping();
            //Exit driver and close Chrome
            driver.quit();


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