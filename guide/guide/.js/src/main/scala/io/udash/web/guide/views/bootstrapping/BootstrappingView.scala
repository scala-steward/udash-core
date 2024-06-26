package io.udash.web.guide.views.bootstrapping

import io.udash.*
import io.udash.web.guide.views.ViewContainer
import io.udash.web.guide.{Context, *}
import org.scalajs.dom.Element
import scalatags.JsDom

case object BootstrappingViewFactory extends StaticViewFactory[BootstrappingState.type](() => new BootstrappingView)

class BootstrappingView extends ViewContainer {
  import Context._
  import JsDom.all._

  override protected val child: Element = div().render

  override def getTemplate: Modifier = div(
    h1("Application bootstrapping"),
    p("In this part of the guide you will read about bootstrapping an Udash application from scratch."),
    p(
      i("This is an advanced topic, if you want to start development as soon as possible, generate the ",
        a(href := IntroState.url)("sample application"), ".")
    ),
    child
  )
}