# zio-opentelemetry-datadog-tracing-provider

## Two words about Datadog

### Terminology

- **Datadog APM Agent**: is the Java agent added by `sbt-datadog` in your project.    
  Its job is to monitor your application and send traces to the Datadog Agent.   
  See also: https://github.com/DataDog/dd-trace-java   
- **Datadog Agent**: is the agent that receives traces from the Datadog APM Agent and sends them to the Datadog backend.    
  It must be deployed somewhere in your infrastructure so that your application can send traces to it.    
  See also: https://github.com/DataDog/datadog-agent   

### The DataDog APM Agent magic

The Datadog APM agent you configured with `sbt-datadog` adds a pre-configured OpenTelemetry `Tracer` to your project.    
This OpenTelemetry `Tracer` is used to send traces to the Datadog Agent deployed in your infrastructure.   
The zio-opentelemtry `Tracing` is a kind of wrapper around OpenTelemetry `Tracer`.   
This is this `Tracing` abstraction that you'll need to use in your ZIO project.

This library is here to help you get a configured `Tracing` instance in your ZIO project.

## zio-opentelemetry-datadog-tracing-provider

### Installation

In your build.sbt file:
```scala
libraryDependencies += "com.guizmaii" %% "zio-opentelemetry-datadog-tracing-provider" % "x.x.x"
```
(To find the latest vesrion, see the [releases](https://github.com/guizmaii-opensource/sbt-datadog/releases))

### Documentation

This library provides you two things:
1. The [OpenTelemetryProvider](zio-opentelemetry-datadog-tracing-provider/src/main/scala/com/guizmaii/datadog/zio/tracing/provider/OpenTelemetryProvider.scala) object to help you easily get a zio-opentelemetry `Tracing` instance configured to send traces to Datadog via the Datadog APM you configured with the sbt plugin.
2. The [OpenTelemetryConfig](zio-opentelemetry-datadog-tracing-provider/src/main/scala/com/guizmaii/datadog/zio/tracing/provider/OpenTelemetryConfig.scala) object to help you easily enable or disable opentelemetry via an environment variable: `ZIO_OPENTELEMETRY_DATADOG_ENABLED`.

### OpenTelemetryProvider

#### `OpenTelemetryProvider.autoInstrumentation` layer

This layer is the one you'll want to use in your app in production.    
It'll provide you a `Tracing` instance that'll automatically send the traces to Datadog via the Datadog APM you configured with the sbt plugin.

You have an example of how to use it in [examples/my-traced-zio-project-example/src/main/scala/org/example/datadog/otlp/Main](examples/my-traced-zio-project-example/src/main/scala/org/example/datadog/otlp/Main.scala).

#### `OpenTelemetryProvider.noOp` layer

This layer is useful when you want to disable tracing in your app.   
It's for example useful in your tests.   
The `Tracing` instance that it'll provide does nothing. 

## The "Traced Service Pattern"

We all know the ZIO "service pattern".      
Well, I "extended" it with what I call the "traced service pattern".    
I give an example of this pattern in [examples/my-traced-zio-project-example/src/main/scala/org/example/datadog/otlp/MyService](examples/my-traced-zio-project-example/src/main/scala/org/example/datadog/otlp/MyService.scala).
