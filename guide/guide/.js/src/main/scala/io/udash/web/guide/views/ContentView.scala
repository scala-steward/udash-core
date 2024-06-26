package io.udash.web.guide.views

import io.udash.*
import io.udash.web.commons.styles.GlobalStyles
import io.udash.web.guide.ContentState
import io.udash.web.guide.components.GuideMenu
import io.udash.web.guide.styles.partials.GuideStyles
import org.scalajs.dom.Element
import scalatags.JsDom.tags2.*

object ContentViewFactory extends StaticViewFactory[ContentState.type](() => new ContentView)

class ContentView extends ViewContainer {
  import io.udash.css.CssView._

  import scalatags.JsDom.all._

  override protected val child: Element = main(GuideStyles.contentWrapper).render

  private val content = main(GuideStyles.main)(
    div(GlobalStyles.body)(
      div(GuideStyles.menuWrapper)(
        GuideMenu().getTemplate
      ),
      child
    )
  )

  override def getTemplate: Modifier = content
}