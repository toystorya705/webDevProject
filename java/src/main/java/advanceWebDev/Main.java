package advanceWebDev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/** Handle the spring method, calling scraperManager*/
public class Main {

    public static void main(String[] args) {

        /**
         * Uses Spring Annotation configuration to set up and run application
         * */

        //PULL FROM SPRING CONTEXT
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        WebScraperManager scraperManager = (WebScraperManager) context.getBean("scraperManager");

        scraperManager.startScraping();
    }

}
