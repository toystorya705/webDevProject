//Import the express and url modules
let express = require('express');
let url = require("url");
const port = process.env.PORT || 3000
//Status codes defined in external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
let app = express();

//Import the mysql module
let mysql = require('mysql');

//Create a connection object with the user details
let connectionPool = mysql.createPool({
    connectionLimit: 5,
    host: "us-cdbr-east-05.cleardb.net",
    user: "b67e51b20bac08",
    password: "7989faea",
    database: "heroku_9c832597e276ce9",
    debug: false
});

mysql://b67e51b20bac08:7989faea@us-cdbr-east-05.cleardb.net/heroku_9c832597e276ce9?reconnect=true

app.use(express.static("public"));

//Set up the application to handle GET requests sent to the user path
app.get('/products/*', handleProductGet);//Subfolders
app.get('/products', handleProductGet);
app.get('/search/*', handleProductGet);

//Start the app listening on port 8080
app.listen(port);


/* Handles GET request sent to products path
   Processes path and query string and calls appropriate functions to
   return the data. */
async function handleProductGet(request, response) {
    //Parse the URL
    let urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    let queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be  undefined if not set.
    let numItems = queries['num_items'];
    let offset = queries['offset'];

    //Split the path of the request into its components
    let pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    let pathEnd = pathArray[pathArray.length - 1];

    //If path ends with 'products' we return all products
    try {
        if (pathEnd === 'products') {
            //Get the total number of products for pagination
            let prodCount = await getProductCount();
            //Get the products
            let products = await getProducts(numItems, offset);

            //Combine into a single object and send back to client
            let returnObj = {
                count: prodCount,
                data: products
            }
            response.json(returnObj);
        }
        else if (pathArray[1] === 'search') {
            //We know it is search
            //path end should contain the search term
            let totalProdCount = await getProductCount();

            let searchResults = await getSearch(pathEnd);
            if(numItems===undefined||offset===undefined) {
                numItems = 12;// Default numitem value
                offset=0
            }

            //Combine into a single object and send back to client
            let returnObj = {
                totalProdCount: totalProdCount,
                data: await getSearch(pathEnd, numItems, offset),
                searchedProductCount:searchResults.length
            }

            response.json(returnObj);
        }
        //If the last part of the path is a valid product id, return data about that product
        let regEx = new RegExp('^[0-9]+$');//RegEx returns true if string is all digits.
        if (regEx.test(pathEnd)) {
            let product ={data: await getProduct(pathEnd)}
            response.json(product);
        }
    } catch (ex) {
        response.status(HTTP_STATUS.NOT_FOUND);
        response.send("{error: '" + JSON.stringify(ex) + "', url: " + request.url + "}");
    }
}
async function getSearch(pathEnd, numItems, offset) {
    let sql = "SELECT * FROM products WHERE name LIKE '%" + (pathEnd).replaceAll("%20", " ") + "%'";

    // Limit the number of results returned, if this has been specified in the query string
    if (numItems !== undefined && offset !== undefined) {
        sql += "ORDER BY product_id LIMIT " + numItems + " OFFSET " + offset;
    }

    //Return promise to run query
    return executeQuery(sql);
}
exports.getSearch=getSearch;


/** Returns a promise to get the total number of products */
async function getProductCount() {
    //Build SQL query
    let sql = "SELECT COUNT(*) FROM products";

    //Execute promise to run query
    let result = await executeQuery(sql);

    //Extract the data we need from the result
    return result[0]["COUNT(*)"];
}



/** Returns all the products with optional pagination */
async function getProducts(numItems, offset) {
    let sql = "SELECT * FROM products ";

    //Limit the number of results returned, if this has been specified in the query string
    if (numItems !== undefined && offset !== undefined) {
        sql += "ORDER BY product_id LIMIT " + numItems + " OFFSET " + offset;
    }

    //Return promise to run query
    return executeQuery(sql);
}
exports.getProducts=getProducts;


/** Returns a promise to get details about a single product */
async function getProduct(prodId) {
    let sql = "SELECT products.product_id,compared_url.id, products.image_url, products.name, compared_url.price, compared_url.url FROM compared_url right  JOIN products ON products.product_id=compared_url.product_id WHERE compared_url.product_id="+prodId+" ORDER BY price ASC";
    return executeQuery(sql);
}






/** Wraps connection pool query in a promise and returns the promise */
async function executeQuery (sql){
    //Wrap query around promise
    let queryPromise = new Promise((resolve, reject) => {
        //Execute the query
        connectionPool.query(sql, function (err, result) {
            //Check for errors
            if (err) {
                //Reject promise if there are errors
                reject(err);
            }
            //Resole promise with data from database.
            resolve(result);
        });
    });

    //Return promise
    return queryPromise;
}

exports.server = app;



