package advanceWebDev;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
/** Ebay Scraper Class*/
public class EbayProductsData extends WebScraper {

    int pageCount = 1;
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
            options.setHeadless(true);
            //Create instance of web driver - this must be on the path.
            WebDriver driver = new ChromeDriver(options);


            driver.get("https://www.ebay.co.uk/sch/i.html?_from=R40&_nkw=" + searchTerm + "&_sacat=0&LH_ItemCondition=1000&LH_BIN=1&LH_PrefLoc=1&rt=nc&_ipg=200");

            productList = driver.findElements(By.className("s-item--watch-at-corner"));

                productCount += productList.size();
                System.out.println(productCount);


                System.out.println("page count   " + pageCount);
                pageCount++;


                for (int i = 0; i < productList.size(); i++) {

                    try {


                        WebElement productPrice = productList.get(i).findElement(By.cssSelector("span[class=\"s-item__price\"]"));
                        WebElement productTitle = productList.get(i).findElement(By.cssSelector("a[class=\"s-item__link\"]"));
                        WebElement imageLink = productList.get(i).findElement(By.cssSelector(("img[class=\"s-item__image-img\"]")));


                        String name = productTitle.getText().replaceAll("[!@#$%/&:*()_+=|<>?{},''\\\\[\\\\]~-]", " ").replaceAll("\"", " ").toLowerCase();
                        String[] nameModify = name.split(" ");
                        if (!name.contains("combo") && !name.contains("modifli") && !name.contains("arm") && !name.contains("braces") && !name.contains("skin") && !name.contains("kit") && !name.contains("backpack") && !name.contains("propellers") && !name.contains("pad") && !name.contains("masterclass") && !name.contains("classes") && !name.contains("handbook") && !name.contains("lens") && !name.contains("lander") && !name.contains("bag") && !name.contains("incase") && !name.contains("replacement") && !name.contains("part") && !name.contains("guard") && !name.contains("stick") && !name.contains("extension") && !name.contains("part") && !name.contains("set") && !name.contains("case") && !name.contains("osmo") && !name.contains("pocket") && !name.contains("accessory") && !name.contains("cable") && !name.contains("accessories") && !name.contains("intelligent") && !name.contains("replace") && !name.contains("spare") && !name.contains("trainers") && !name.contains("trainer") && !name.contains("goggles") && !name.contains("goggle") && !name.contains("mobile") && !name.contains("extra remote") && !name.contains("remote controller") && !name.contains("service")&& !name.contains("1550t")&& !name.contains("battery charging")&& !name.contains("megaphone")&& !name.contains("fpv motion controller")&& !name.contains("motion controller")&& !name.contains("walle")&& !name.contains("hskrc")&& !name.contains("ing")&& !name.contains("ball")&& !name.contains("card")&& !name.contains("care")&& !name.contains("refresh")&& !name.contains("tiktok")&& !name.contains("lee")&& !name.contains("eagle")&& !name.contains("eye")&& !name.contains("snap")&& !name.contains("robomaster")&& !name.contains("helicopter")&& !name.contains("immersionrc")&& !name.contains("cover")&& !name.contains("shell")&& !name.contains("battery")&& !name.contains("000611")&& !name.contains("label")&& !name.contains("labels")&& !name.contains("user")) {

                            ProductData product = new ProductData();
                            String nameModifRemoveWords = (nameModify.length >= 4 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] + " " + nameModify[3] : nameModify.length == 3 ? nameModify[0] + " " + nameModify[1] + " " + nameModify[2] : nameModify.length == 2 ? nameModify[0] + " " + nameModify[1] : nameModify[0]).replaceAll("camera", "").replaceAll("4k", "").replaceAll("/", "").replaceAll("1080p", "").replaceAll("gps", "").replaceAll("any", "").replaceAll("standard", "").replaceAll("2k", "").replaceAll("fly", "").replaceAll("eu", "").replaceAll("brand", "").replaceAll("  ", " ").replaceAll("unit", "").replaceAll("only", "");
                            if(nameModifRemoveWords.contains("dji")||nameModifRemoveWords.contains("mavic"))
                                nameModifRemoveWords = nameModifRemoveWords.replaceAll("drone", "").replaceAll("zoom", "").replaceAll("enterprise", "").replaceAll("new","").replaceAll("ultralight","").replaceAll("unactivated","").replaceAll("more","");

                            System.out.println(nameModifRemoveWords);
                            System.out.println((int) Math.round(Double.parseDouble(productPrice.getText().replaceAll("[!@#$%&*()_+=|<>?{}??,''\\\\[\\\\]~-]", ""))));
                            System.out.println(productTitle.getAttribute("href"));// Get link from title
                            System.out.println(imageLink.getAttribute("src"));

                            product.setName(nameModifRemoveWords);
                            product.setImageUrl((imageLink.getAttribute("src")));

                            ProductData productMapped = productListData.findProduct(product);

                            ProductComparedUrl productCompared = new ProductComparedUrl();
                            productCompared.setProduct(productMapped);
                            productCompared.setPrice((int) Math.round(Double.parseDouble(productPrice.getText().replaceAll("[!@#$%&*()_+=|<>?{}??,''\\\\[\\\\]~-]", ""))));
                            productCompared.setUrl(productTitle.getAttribute("href"));

                            productListData.saveProductCompared(productCompared);

                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }


                System.out.println("This is product count   " + ++productCount);



            try {
                sleep(timeDelay);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //Exit driver and close Chrome
            driver.quit();
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
