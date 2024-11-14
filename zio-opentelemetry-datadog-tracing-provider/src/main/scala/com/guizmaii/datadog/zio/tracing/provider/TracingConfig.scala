package com.guizmaii.datadog.zio.tracing.provider

import zio.{ZLayer, Config, ConfigProvider}
import zio.Config._

sealed trait TracingConfig extends Product with Serializable
object TracingConfig {
  case object Opentelemetry extends TracingConfig
  case object Disabled      extends TracingConfig

  val config: Config[TracingConfig] =
    boolean("ZIO_OPENTELEMETRY_DATADOG_ENABLED")
      .withDefault(false)
      .map(enabled => if (enabled) Opentelemetry else Disabled)

  /**
   * Provides a way to enable or disable the OpenTelemetry Tracing via the `ZIO_OPENTELEMETRY_DATADOG_ENABLED` environment variable.
   */
  def fromSystemEnv: ZLayer[Any, Throwable, TracingConfig] =
    ZLayer.fromZIO(ConfigProvider.envProvider.load(config))
}
