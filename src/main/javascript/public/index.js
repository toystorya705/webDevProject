let app = new Vue({
    el: "#app",
    data: {

        product: [],
        searchedProductCount: 0,
        pageOffsetNumberCount: 0,
        totalProductCount: 0,
        activeBtn: '',
        showProduct: true,
        filterName: '',
        filter: '',
        sort: '',
        messageCheckout: "",
        order: {
            name: "",
            phone: "",

        },


        cart: []
    },
    methods: {

        search: async function (pageIndex, productIdClick) {
            try {
                //Call REST API with search query
                sessionStorage.getItem("searchTerm", this.filter);
                let response;
                console.log("dddd  " + productIdClick)
                if (pageIndex === null) {
                    this.showProduct=true;

                    response = await axios.get('/search/' + this.filter);
                    this.product = response.data.data;
                } else if (productIdClick != undefined) {
                    this.showProductDisplay();
                    response = await axios.get('/products/' + productIdClick);
                    console.log( response.data);
                    this.product = response.data.data;
                } else {
                    this.showProduct=true;
                    response = await axios.get('/search/' + this.filter + "?num_items=12&offset=" + (pageIndex * 12 - 12));
                    console.log(pageIndex * 10 - 10)
                    this.product = response.data.data;
                }
                this.activeBtn = pageIndex;

                sessionStorage.setItem("searchTerm", this.filter);
                /* Put returned products into data structure.
                    The rendering will be done automatically by Vue. */
                this.totalProductCount = response.data.totalProdCount;
                this.searchedProductCount = response.data.searchedProductCount;
                this.pageOffsetNumberCount = Math.round(response.data.searchedProductCount / 10)

                console.log(response.data);
            } catch (err) {
                console.error(err);
            }
        },
        checkWebsiteName: function (url) {
            if (url.toLowerCase().includes("amazon"))
                return "Amazon"
            else if (url.toLowerCase().includes("ebay"))
                return "Ebay"
            else if (url.toLowerCase().includes("wex"))
                return "Wex Photos"
            else if (url.toLowerCase().includes("argos"))
                return "Argos"
            else if (url.toLowerCase().includes("very"))
                return "Very"
        },
        getWebsiteImageUrl: function (url="") {
            console.log(url +"jhbfuebrhfuey");
            if (url.toLowerCase().includes("amazon"))
                return "http://media.corporate-ir.net/media_files/IROL/17/176060/Oct18/Amazon%20logo.PNG"
            else if (url.toLowerCase().includes("ebay"))
                return "https://mpng.subpng.com/20180825/juk/kisspng-logo-brand-product-design-business-ebay-profeta-profeta-health-5b80f167182059.9195618015351770630988.jpg"
            else if (url.toLowerCase().includes("wex"))
                return "https://www.wexphotovideo.com/globalassets/brand-logos/wex-photo-video.png"
            else if (url.toLowerCase().includes("argos"))
                return "http://assets.stickpng.com/images/5a1c3e6af65d84088faf144e.png"
            else if (url.toLowerCase().includes("very"))
                return "https://clarendonmanagement.co.uk/wp-content/uploads/2018/09/very.png"
        },

        //  This function decrease the value of stock by 1 every time user click Button

        showProductDisplay() {
            this.showProduct = this.showProduct ? false : true

        },
        filterClick() {


            this.sort = "asec"


        },
        defaultClick() {

            this.sort = ""

        },


    },
    computed: {// This disables the button at 0 stock

        getproduct() {

            var product = this.product.filter((product) => {
                return product.name.toLowerCase().includes(this.filter.toLowerCase());
            });

            if (this.filterName == "name") {

                if (this.sort == 'dsec') {

                    return product.sort((a, b) => (b.name > a.name ? 1 : -1));

                } else if (this.sort == 'asec') {

                    return product.sort((a, b) => (b.name < a.name ? 1 : -1));
                }
            } else if (this.filterName == "price") {
                if (this.sort == "dsec") {
                    return product.sort(function (a, b) {
                        return b.price - a.price
                    });

                } else if (this.sort == 'asec') {
                    return product.sort(function (a, b) {
                        return a.price - b.price
                    });

                }
            } else {
                return product;
            }

        },

    },


});
