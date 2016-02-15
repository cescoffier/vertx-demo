var EventBus = require('vertx3-eventbus-client');

var eb = new EventBus('http://localhost:8080/eventbus/');

eb.onopen = function () {
    setInterval(function () {
        eb.send("data", {
            "message": "bonjour",
            "from": "node producer"
        });
    }, 2000);
};