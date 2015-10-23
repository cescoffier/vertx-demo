

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.StaticHandler
import io.vertx.groovy.ext.web.handler.sockjs.SockJSHandler

def router = Router.router(vertx)

def options = [
        outboundPermitteds:[
                [
                        address:"data"
                ]
        ]
]

router.route("/eventbus/*")
        .handler(SockJSHandler.create(vertx).bridge(options))
router.route("/assets/*")
        .handler(StaticHandler.create("assets"))

def httpServer = vertx.createHttpServer()
httpServer.requestHandler(router.&accept).listen(8080)