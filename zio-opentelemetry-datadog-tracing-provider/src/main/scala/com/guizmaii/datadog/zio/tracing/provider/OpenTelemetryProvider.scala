package com.guizmaii.datadog.zio.tracing.provider

import zio.telemetry.opentelemetry.OpenTelemetry
import zio.telemetry.opentelemetry.tracing.Tracing
import zio.{ULayer, ZIO, ZLayer}

object OpenTelemetryProvider {

  /**
   * Useful for tests
   */
  def noOp: ULayer[Tracing] =
    ZLayer.make[Tracing](
      OpenTelemetry.noop,
      OpenTelemetry.contextZIO,
      OpenTelemetry.tracing("no-op"),
    )

  /**
   * Provides a zio-opentelemetry `Tracing` instance using the OpenTelemetry `Tracer` configured by the Datadog APM Agent
   *
   * Don't forget to enable the OpenTelemetry tracing in the Datadog APM Agent.
   * You can enable it by setting the `DD_TRACE_OTEL_ENABLED` environment variable to `true`
   * or by setting the `dd.trace.otel.enabled` property to `true`.
   *
   * See related Datadog documentation:
   * - https://docs.datadoghq.com/tracing/trace_collection/custom_instrumentation/java/otel/
   *
   * See zio-opentelemetry documentation:
   * - https://zio.dev/zio-telemetry/opentelemetry/#usage-with-opentelemetry-automatic-instrumentation
   *
   * Exposes the same parameters as `OpenTelemetry.tracing`
   */
  def autoInstrumentation(
    instrumentationScopeName: String,
    instrumentationVersion: Option[String] = None,
    schemaUrl: Option[String] = None,
    logAnnotated: Boolean = false,
  ): ZLayer[OpenTelemetryConfig, Throwable, Tracing] =
    ZLayer.fromZIO {
      for {
        config <- ZIO.service[OpenTelemetryConfig]
        _      <- ZIO.logInfo(s"OpenTelemetry Tracing config: $config")
      } yield config match {
        case OpenTelemetryConfig.Disabled      => noOp
        case OpenTelemetryConfig.Opentelemetry =>
          ZLayer.make[Tracing](
            OpenTelemetry.global,
            OpenTelemetry.contextJVM,
            OpenTelemetry.tracing(
              instrumentationScopeName = instrumentationScopeName,
              instrumentationVersion = instrumentationVersion,
              schemaUrl = schemaUrl,
              logAnnotated = logAnnotated,
            ),
          )
      }
    }.flatten
}
