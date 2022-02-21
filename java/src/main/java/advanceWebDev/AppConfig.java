package advanceWebDev;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.*;


import java.util.ArrayList;

/**
 * Handle Classes Dependencies
 * */
@Configuration
public class AppConfig {
    SessionFactory sessionFactory;
    /**
     *  Adding all scraper classes
     *  */
    @Bean
    public WebScraperManager scraperManager() {
        WebScraperManager scraperManager = new WebScraperManager();

        //Create list of web scrapers and add to scraper manager
        ArrayList<WebScraper> scraperList = new ArrayList();
        scraperList.add(scraper3());
        scraperList.add(scraper2());
        scraperList.add(scraper1());
        scraperList.add(scraper4());
        scraperList.add(scraper5());
        scraperManager.setWebScraperArrayList(scraperList);

        return scraperManager;
    }
    /**
     * Wex Product Class Bean Injecting ProductDao
     * */
    @Bean
    public WexProductsData scraper1() {
        WexProductsData scraper1 = new WexProductsData();
        scraper1.setProductDao(productDao());
        scraper1.setSearchTerm("dji drone");
        scraper1.setTimeDelay(100);
        return scraper1;
    }
    /**
     * Very Product Class Bean Injecting ProductDao
     * */
    @Bean
    public VeryProductData scraper2() {
        VeryProductData scraper2 = new VeryProductData();
        scraper2.setProductDao(productDao());
        scraper2.setSearchTerm("dji drone");
        scraper2.setTimeDelay(4000);
        return scraper2;
    }

    /**
     * Amazon Product Class Bean Injecting ProductDao
     * */
    @Bean
    public AmazonProductsData scraper3() {
        AmazonProductsData scraper1 = new AmazonProductsData();
        scraper1.setProductDao(productDao());
        scraper1.setSearchTerm("dji drone");
        scraper1.setTimeDelay(4000);
        return scraper1;
    }
    /**
     * Wex Product Class Bean Injecting ProductDao
     * */
    @Bean
    public ArgosProductsData scraper4() {
        ArgosProductsData scraper4 = new ArgosProductsData();
        scraper4.setProductDao(productDao());
        scraper4.setSearchTerm("dji drone");
        scraper4.setTimeDelay(4000);
        return scraper4;
    }
    /**
     *  Ebay Product Class Bean Injecting ProductDao
     *  */
    @Bean
    public EbayProductsData scraper5() {
        EbayProductsData scraper4 = new EbayProductsData();
        scraper4.setProductDao(productDao());
        scraper4.setSearchTerm("dji drone");
        scraper4.setTimeDelay(4000);
        return scraper4;
    }

    /**
     * Product Class Bean Injecting Session Factory
     * */
    @Bean
    public ProductDao productDao() {
        ProductDao productDao = new ProductDao();
        productDao.setSessionFactory(sessionFactory());
        return productDao;
    }
    /**
     * Creating Session factory for productDao
     * */
    @Bean
    public SessionFactory sessionFactory() {
        if (sessionFactory == null) {//Build sessionFactory once only
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                //Here we are using a configuration file that specifies Java annotations.
                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                } catch (Exception e) {
                        /* The registry would be destroyed by the SessionFactory,
                            but we had trouble building the SessionFactory, so destroy it manually */
                    System.err.println("Session Factory build failed.");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                //Output result
                System.out.println("Session factory built.11");
            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }


}
