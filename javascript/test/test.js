//File containing the functions that we are testing
require('../server.js');

//database code defined in external file
let server = require('../server');
let app = server.server;

//Built in Node.js assertions
const assert = require("assert");

//Chai library for HTTP requests and more sophisticated assertions
let chai = require('chai');
let chaiHttp = require('chai-http');
let should = chai.should();
chai.use(chaiHttp);

//Import server


//Mocha/Chai test of RESTful Web Service
describe('/GET products', () => {
    it('should GET all the products and check property', (done) => {
        chai.request(app)
            .get('/products')
            .end((err, res) => {
                res.should.have.status(200);
                let data = res.body.data[0];
                data.should.have.a.property("name");
                data.should.have.a.property("image_url");
                res.body.data.should.be.a('array');
                done();
            });
    });
});


describe('/GET Search Products with name dji', () => {
    it('should GET all the Search', (done) => {
        chai.request(app)
            .get('/search/dji')
            .end((err, res) => {
                res.should.have.status(200);
                let data = res.body.data[0];
                data.should.have.a.property("name").includes("dji");
                data.should.have.a.property("image_url");
                res.body.data.should.be.a('array');
                done();
            });
    });
});

describe('/GET Product price check', () => {
    it('should GET all the Search', (done) => {
        chai.request(app)
            .get('/products')
            .end((err, res) => {
                res.should.have.status(200);
                let data = res.body.data[0];

                data.should.have.a.property("name")
                data.should.have.a.property("image_url");
                res.body.data.should.be.a('array');
                chai.request(app)
                    .get('/products/' + data.product_id)
                    .end((err, res) => {
                        res.should.have.status(200);
                        let data = res.body.data[0];
                        data.should.have.a.property("url")
                        data.should.have.a.property("price");
                        res.body.data.should.be.a('array');

                        done();
                    });
            });
    });
});


describe('database', () => {

    //Mocha test for Offset and NumItem.
    describe('#Offset & Limit Check', () => {
        it('should  number of item return ', async () => {
            // //Mock response object for test
            //Call function that we are testing
            try {
                let result = await server.getProducts(5, 0);
                assert.equal(5, result.length);
            }
            catch(ex){
                assert.fail(ex);
            }
        });
    });
});

describe('database', () => {

    //Mocha test for searching.
    describe('#SearchResult Check', () => {
        it('should return search result in the database', async () => {
            // //Mock response object for test
            //Call function that we are testing
            try {
                let result = await server.getSearch("dji",5, 0);
                assert.equal("dji", result[0].name.includes("dji")?"dji":"");
                assert.equal(5, result.length);
            }
            catch(ex){
                assert.fail(ex);
            }
        });
    });
});
