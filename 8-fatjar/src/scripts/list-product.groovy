import io.vertx.groovy.ext.jdbc.JDBCClient
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.registry.CommandRegistry

def builder = CommandBuilder.command("product-list")
builder.processHandler( { process ->
  JDBCClient client = JDBCClient.createShared(vertx,
          [
                  url: "jdbc:hsqldb:file:db/whiskies",
                  driver_class: "org.hsqldb.jdbcDriver"
          ],
          "My-Whisky-Collection")

  client.getConnection({ ar ->
    if (ar.failed()) {
      process.write "Cannot connect to the database " + ar.cause()
      process.end()
    } else {
      def connection = ar.result()
      process.write "Querying database... \n"
      connection.query("SELECT * FROM Whisky", {
        result ->
          result.result().get("results").each { it -> process.write("${it}\n")}
          process.end()
      });
    }
  })
});

def registry = CommandRegistry.get(vertx)
registry.registerCommand(builder.build())

