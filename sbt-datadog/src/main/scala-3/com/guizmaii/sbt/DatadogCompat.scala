package com.guizmaii.sbt

import com.typesafe.sbt.packager.PluginCompat
import java.io.File
import xsbti.FileConverter

private[sbt] object DatadogCompat {
  def toFileRefsMapping(mappings: Seq[(File, String)], conv: FileConverter): Seq[(PluginCompat.FileRef, String)] =
    PluginCompat.toFileRefsMapping(mappings)(using conv)
}
