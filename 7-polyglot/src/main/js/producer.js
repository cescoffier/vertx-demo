var Random = java.util.Random;
var random = new Random();

var eb = vertx.eventBus();
vertx.setPeriodic(1000,
    function (v) {
        var val = random.nextInt(60);
        console.log("Javascript -> Publishing " + val);
        eb.publish("data", {
            "value": val,
            "id": "javascript"
        });
    }
);