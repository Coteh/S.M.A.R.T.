const express = require('express');
const mysql = require('mysql');
var app = express();
var connection = mysql.createConnection({
    host: "localhost",
    user: "james",
    password: "password",
    database: "smart"
});

connection.connect();

app.get("/", function(req, res) {
    res.send("Hello World");
});

app.get("/courses", function(req, res) {
    connection.query("SELECT DISTINCT courseid FROM grades;", function(error, results, fields) {
        if (error) {
            console.log(error);
            return;
        }
        for (var i = 0; i < results.length; i++) {
            var responseStr = results[i].courseid;
            res.write(responseStr + "\n");
        }
        res.end();
    });
});

app.get("/students", function(req, res) {
    connection.query("SELECT * FROM STUDENTS;", function(error, results, fields) {
        if (error) {
            console.log(error);
            return;
        }
        for (var i = 0; i < results.length; i++) {
            var responseStr = results[i].studentid;
            responseStr += "," + results[i].email;
            responseStr += "," + results[i].courseAverage;
            responseStr += "," + results[i].name;
            res.write(responseStr + "\n");
        }
        res.end();
    });
});

app.get("/marks", function(req, res) {

});

app.listen(3000, function() {
    console.log("Listening on port 3000");



    // connection.end();
});
