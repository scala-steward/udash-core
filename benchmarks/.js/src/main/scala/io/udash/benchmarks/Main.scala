package io.udash.benchmarks

import io.udash.benchmarks.css.CssStylesApply
import io.udash.benchmarks.i18n.StaticTranslationBinding
import io.udash.benchmarks.properties._
import japgolly.scalajs.benchmark.engine.EngineOptions
import japgolly.scalajs.benchmark.gui.BenchmarkGUI
import org.scalajs.dom.document

object Main {
  def main(args: Array[String]): Unit = {
    BenchmarkGUI.renderMenu(document.getElementById("body"), engineOptions = EngineOptions.default.copy(iterations = 3))(
      SinglePropertyListeners.suite,
      ModelPropertyListeners.suite,
      ModelPropertyWithSeqListeners.suite,
      TransformedSeqPropertyListeners.suite,
      FilteredSeqPropertyListeners.suite,
      ReversedSeqPropertyListeners.suite,
      ZippedSeqPropertyListeners.suite,

      StaticTranslationBinding.suite,

      CssStylesApply.suite,

      PropertyParameters.suite
    )
  }
}
