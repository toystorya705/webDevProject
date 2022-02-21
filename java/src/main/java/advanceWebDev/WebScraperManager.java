package advanceWebDev;

import java.util.ArrayList;
/** Handling calling all the threads*/
public class WebScraperManager {

    private ArrayList<WebScraper> webScraperArrayList;

    public void startScraping() {

        for (WebScraper scraper : webScraperArrayList) {
            scraper.start();
        }
    }

    public void setWebScraperArrayList(ArrayList<WebScraper> webScraperArrayList) {
        this.webScraperArrayList = webScraperArrayList;
    }

}