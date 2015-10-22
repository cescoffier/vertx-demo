
random = Java::JavaUtil::Random.new()

$vertx.set_periodic(1000) { |t|
  value = random.next_int(60)
  $vertx.event_bus().publish("data", {
      'value' => value,
      'id' => "ruby"
  })
}