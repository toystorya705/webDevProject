package advanceWebDev;

import org.openqa.selenium.WebElement;

import java.util.List;

/** Handles all abstract methods and extends thread */
public abstract class WebScraper extends Thread {

    public abstract void setSearchTerm(String searchTerm);

    public abstract void setTimeDelay(int timeDelay);

    public abstract void setProductDao(ProductDao productListData);

    public abstract ProductDao getProductDao();

    public abstract List<WebElement> getProductList();

    public abstract void stopScraping();


}
