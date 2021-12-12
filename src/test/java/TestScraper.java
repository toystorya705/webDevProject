//package eu.davidgamez.clock.test;

//Import the class that we are going to test
//import eu.davidgamez.clock.Clock
//JUnit 5 imports

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import advanceWebDev.*;

import java.util.List;

@DisplayName("Test")
public class TestScraper {
    static SessionFactory sessionFactory;

    @BeforeAll
    static void initAll() {
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
            //Ouput result
            System.out.println("Session factory built.");
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
            ex.printStackTrace();
        }
    }

    @BeforeEach
    void init() {
    }


    @Test
    @DisplayName("Save Product Name Test")
    void saveProductNameTest() {

        //Create an instance of the class that we want to test
        ProductDao productDado = new ProductDao();
        String randomName = String.valueOf(Math.random());
        ProductData productData = new ProductData();
        productDado.setSessionFactory(sessionFactory);
        productData.setName(randomName);


        productDado.saveProduct(productData);

        //Check that product exists in database
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Get hats with random name
        List<ProductData> productList = session.createQuery("from ProductData where name='" + randomName + "'").getResultList();

        if (productList.size() != 1) {//One result should be stored
            session.delete(productList.get(0));
            session.getTransaction().commit();
            fail("product not successfully stored. product list size: " + productList.size());
        }

        session.delete(productList.get(0));


        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();



    }


    @Test
    @DisplayName("Save Product Price Test")
    void saveProductPriceTest() {

        //Create an instance of the class that we want to test
        ProductDao productDado = new ProductDao();
        String randomName = String.valueOf(Math.random());
        ProductData productData = new ProductData();
        productDado.setSessionFactory(sessionFactory);
        productData.setName(randomName);
        productData.setImageUrl("");


        ProductData productMapped = productDado.findProduct(productData);

        ProductComparedUrl productCompared = new ProductComparedUrl();
        productCompared.setProduct(productMapped);
        String randomUrl = String.valueOf(Math.random());
        productCompared.setUrl(randomUrl);


        productDado.saveProductCompared(productCompared);

        //Check that product exists in database
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Get hats with random name
        List<ProductComparedUrl> productList = session.createQuery("from ProductComparedUrl where url='" + productCompared.getUrl() + "'").getResultList();
        if (productList.size() != 1) {//One result should be stored
            session.delete(productList.get(0));
            session.getTransaction().commit();
            fail("product not successfully stored. product list size: " + productList.size());

        }

        //Delete product from database
        session.delete(productList.get(0));

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();




    }

//
//    @Test
//    @DisplayName("Amazon Scraper")
//    void amazonScraperTest() {
//
//        //Create an instance of the class that we want to test
//        AmazonProductsData scraper = new AmazonProductsData();
//        scraper.setSearchTerm("dji");
//        scraper.setTimeDelay(500);
//        scraper.run();
//
//        assert (scraper.getProductList().size() > 0);
//        scraper.stopScraping();
//
//    }
//    @Test
//    @DisplayName("Argos Scraper")
//    void argosScraperTest() {
//
//        //Create an instance of the class that we want to test
//        ArgosProductsData scraper = new ArgosProductsData();
//        scraper.setSearchTerm("dji");
//        scraper.setTimeDelay(500);
//        scraper.run();
//
//        assert (scraper.getProductList().size() > 0);
//        scraper.stopScraping();
//
//    }
//
//    @Test
//    @DisplayName("Ebay Scraper")
//    void ebayScraperTest() {
//
//        //Create an instance of the class that we want to test
//        EbayProductsData scraper = new EbayProductsData();
//        scraper.setSearchTerm("dji");
//        scraper.setTimeDelay(500);
//        scraper.run();
//
//        assert (scraper.getProductList().size() > 0);
//        scraper.stopScraping();
//
//    }
//
//
//    @Test
//    @DisplayName("Very Scraper")
//    void veryScraperTest() {
//
//        //Create an instance of the class that we want to test
//        VeryProductData scraper = new VeryProductData();
//        scraper.setSearchTerm("dji");
//        scraper.setTimeDelay(500);
//        scraper.run();
//
//        assert (scraper.getProductList().size() > 0);
//        scraper.stopScraping();
//
//    }
//
//    @Test
//    @DisplayName("Wex Photos Scraper")
//    void wexScraperTest() {
//
//        //Create an instance of the class that we want to test
//        WexProductsData scraper = new WexProductsData();
//        scraper.setSearchTerm("dji");
//        scraper.setTimeDelay(500);
//        scraper.run();
//
//        assert (scraper.getProductList().size() > 0);
//        scraper.stopScraping();
//
//    }


    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
