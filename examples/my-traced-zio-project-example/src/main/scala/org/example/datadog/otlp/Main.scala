package org.example.datadog.otlp

import com.guizmaii.datadog.zio.tracing.provider.{OpenTelemetryConfig, OpenTelemetryProvider}
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    (
      for {
        result <- ZIO.serviceWithZIO[MyService](_.doSomething)
        _      <- ZIO.logInfo(s"Result: $result")
      } yield ()
    ).provide(
      MyService.live,
      OpenTelemetryProvider.autoInstrumentation(
        instrumentationScopeName = "my-app",
        instrumentationVersion = Some("1.0.0"),
      ),
      OpenTelemetryConfig.fromSystemEnv,
    )
}
