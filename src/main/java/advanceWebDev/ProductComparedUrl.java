package advanceWebDev;

import javax.persistence.*;
/** ProductComparedUrl Class for product url and price, have foreign key product_id from ProductData Class */
@Table(name = "compared_url", indexes = {
        @Index(name = "product_id", columnList = "product_id")
})
@Entity
public class ProductComparedUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductData product;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "price", length = 1000)
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public ProductData getProduct() {
        return product;
    }

    public void setProduct(ProductData product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}