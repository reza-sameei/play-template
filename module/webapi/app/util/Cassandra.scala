package xyz.sigmalab.template.play.util

import java.net._

import com.datastax.driver.core._
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.control.NonFatal

case class Cassandra(
    name: String,
    config: Cassandra.Config,
    ec: ExecutionContextExecutor
) {

    import Cassandra.ToFuture

    private val logger: Logger = LoggerFactory getLogger name

    logger info s"Init, ClassOf[${getClass.getName}]"
    logger debug s"${config}"

    private val cluster = {
        Cluster
            .builder
            .addContactPointsWithPorts(
                config.seeds.map{
                    case (host, port) =>
                        new InetSocketAddress(host, port)
                }.asJava
            ).build
    }

    private val session = {
        cluster.connect(config.keyspace)
    }

    def prepare(stmt: String) = {
        if (logger.isDebugEnabled) logger debug s"Preparing '${stmt}'"
        session prepare stmt
    }

    def execute(stmt: Statement): Future[ResultSet] = {
        if (logger.isTraceEnabled()) logger trace s"Execute, ${stmt}, ExecutionContext: ${ec}"
        ToFuture(session executeAsync stmt)(ec)
    }

}

object Cassandra {

    type Row = com.datastax.driver.core.Row
    type ResultSet = com.datastax.driver.core.ResultSet

    case class Config(
        seeds: Seq[(String, Int)],
        keyspace: String,
        username: String,
        password: String
    )

    private[Cassandra] object ToFuture {

        def apply(
            task: ResultSetFuture
        )(
            implicit ec: ExecutionContextExecutor
        ): Future[ResultSet] = {
            val promise = Promise[ResultSet]
            task.addListener(new ImplV1(task, promise, ec), ec)
            promise.future
        }

        class ImplV1 private[ToFuture] (
            task: ResultSetFuture,
            promise: Promise[ResultSet],
            ec: ExecutionContextExecutor
        ) extends Runnable {

            override def run: Unit = try {
                promise success task.get
            } catch {
                case NonFatal(cause) => promise failure cause
            }
        }
    }
}
