package com.guizmaii.sbt

import com.typesafe.sbt.packager.PluginCompat
import java.io.File
import xsbti.FileConverter

private[sbt] object DatadogCompat {
  def toDatadogAgentMapping(agentJar: File, conv: FileConverter): Seq[(PluginCompat.FileRef, String)] =
    PluginCompat.toFileRefsMapping(Seq(agentJar -> "datadog/dd-java-agent.jar"))(conv)
}
