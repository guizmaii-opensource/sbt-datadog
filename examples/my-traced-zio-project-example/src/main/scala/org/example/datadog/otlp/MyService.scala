package org.example.datadog.otlp

import zio.telemetry.opentelemetry.tracing.Tracing
import zio.{Task, URLayer, ZIO, ZLayer}

trait MyService {
  def doSomething: Task[Int]
}

final class MyServiceLive extends MyService {
  override def doSomething: Task[Int] = ZIO.succeed(42)
}

/**
 * I call this the "Traced Service Pattern"
 *
 * The great thing with this pattern is that:
 * 1. if someone add a method in your service, he/she will be forced to implement it here too so that everything keeps being traced
 * 2. it separates the tracing logic from the business logic
 */
final class MyServiceTraced(tracing: Tracing)(delegator: MyService) extends MyService {
  import tracing.aspects._

  override def doSomething: Task[Int] =
    delegator.doSomething @@ span("MyService::doSomething")
}

object MyService {
  val live: URLayer[Tracing, MyService] =
    ZLayer {
      for {
        tracing <- ZIO.service[Tracing]
        live     = new MyServiceLive
      } yield new MyServiceTraced(tracing)(live)
    }
}
