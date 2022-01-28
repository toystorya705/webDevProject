package advanceWebDev;
//Hibernate imports
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.List;

/**
 *  Handles saving and updating products with their image, price, url, image url, and name to database. Also prevents duplication
 */
public class ProductDao {
    SessionFactory sessionFactory;

    /** Saves products
     * @param product passing parameter of ProductData Class
     * @return product*/
    public ProductData saveProduct(ProductData product) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Add Product to database - will not be stored until we commit the transaction
        session.saveOrUpdate(product);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("products added to database with ID: " + product.getId());
        return product;
    }
    /** Saves product price and url. Also check if url exits then just updates the price
     * @param productComparedUrl passing parameter of ProductComparedUrl Class
     * @return productComparedUrl*/
    public ProductComparedUrl saveProductCompared(ProductComparedUrl productComparedUrl) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Build query string and search for
        String queryStr = "from ProductComparedUrl where url='" + productComparedUrl.getUrl() + "'";
        List<ProductComparedUrl> productList = session.createQuery(queryStr).getResultList();

        //Found a single Product.
        if (productList.size() == 1) {
            productList.get(0).setPrice(productComparedUrl.getPrice());// Updating the price
        } else
            session.saveOrUpdate(productComparedUrl);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Product added to database with ID: " + productComparedUrl.getId());
        return productComparedUrl;
    }


    /** Finds the product if it doesn't exist then add product to database
     *@param product passing parameter of ProductData Class
     * @return find product*/
    public ProductData findProduct(ProductData product) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();
        //Build query string
        String queryStr = "from ProductData where name='" + product.getName() + "'";
        
        List<ProductData> productList = session.createQuery(queryStr).getResultList();
        //Close the session and release database connection
        session.close();

        if (productList.size() == 1)
            return productList.get(0);
        else
            return saveProduct(product);
    }
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



}
