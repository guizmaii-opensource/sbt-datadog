package com.guizmaii.datadog.zio.tracing.provider

import zio.Config._
import zio.{Config, ConfigProvider, ZLayer}

sealed trait OpenTelemetryConfig extends Product with Serializable
object OpenTelemetryConfig {
  case object Opentelemetry extends OpenTelemetryConfig
  case object Disabled      extends OpenTelemetryConfig

  val config: Config[OpenTelemetryConfig] =
    boolean("ZIO_OPENTELEMETRY_DATADOG_ENABLED")
      .withDefault(false)
      .map(enabled => if (enabled) Opentelemetry else Disabled)

  /**
   * Provides a way to enable or disable the OpenTelemetry Tracing via the `ZIO_OPENTELEMETRY_DATADOG_ENABLED` environment variable.
   */
  def fromSystemEnv: ZLayer[Any, Config.Error, OpenTelemetryConfig] =
    ZLayer.fromZIO(ConfigProvider.envProvider.load(config))
}
