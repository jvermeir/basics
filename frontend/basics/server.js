const express = require('express');
const path = require('path');
const app = express();
const PORT = process.env.PORT || 8081;

app.use(express.static(path.join(__dirname, 'build')));

var httpProxy = require('http-proxy');
var apiProxy = httpProxy.createProxyServer();
var backend = 'http://localhost:8080',
    frontend = 'http://localhost:8081';

app.use((req, res, next) => {
    console.log(`Received ${req.method} request to ${req.url}`);
    next();
});

app.all("/api/customers", function(req, res) {
    console.log(`api: ${req.method} request to ${req.url}`);
    apiProxy.web(req, res, {target: backend});
});

app.all("/api/customers/*", function(req, res) {
    console.log(`api: ${req.method} request to ${req.url}`);
    apiProxy.web(req, res, {target: backend});
});

app.all("/*", function(req, res) {
    console.log(`all: ${req.method} request to ${req.url}`);
    apiProxy.web(req, res, {target: frontend});
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
