package io.udash.bindings.inputs

import io.udash.bindings.modifiers.Binding
import io.udash.properties.single.Property
import org.scalajs.dom._

import scala.concurrent.duration.Duration

/** Template of binding for text inputs. */

private[bindings] abstract class TextInputsModifier(property: Property[String], debounce: Duration, onInputElementEvent: String => Unit) extends Binding {
  def elementValue(t: Element): String
  def setElementValue(t: Element, v: String): Unit
  def setElementKeyUp(t: Element, callback: KeyboardEvent => Unit): Unit
  def setElementOnChange(t: Element, callback: Event => Unit): Unit
  def setElementOnInput(t: Element, callback: Event => Unit): Unit
  def setElementOnPaste(t: Element, callback: Event => Unit): Unit

  private def updatePropertyValueForElement(element: Element): Unit = {
    val value = elementValue(element)
    if (property.get != value) {
      property.set(value)
      onInputElementEvent(value)
    }
  }

  override def applyTo(t: Element): Unit = {
    if (property.get != null) setElementValue(t, property.get)

    propertyListeners += property.listen { value =>
      if (elementValue(t) != value) setElementValue(t, value)
    }

    var propertyUpdateHandler: Int = 0
    val callback =
      if (debounce.toMillis > 0)
        (_: Event) => {
          if (propertyUpdateHandler != 0) window.clearTimeout(propertyUpdateHandler)
          propertyUpdateHandler = window.setTimeout(() => {
            updatePropertyValueForElement(t)
          }, debounce.toMillis.toDouble)
        }
      else
        (_: Event) => updatePropertyValueForElement(t)
    setElementKeyUp(t, callback)
    setElementOnChange(t, callback)
    setElementOnInput(t, callback)
    setElementOnPaste(t, callback)
  }
}
