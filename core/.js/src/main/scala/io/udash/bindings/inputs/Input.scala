package io.udash.bindings.inputs

import io.udash._
import org.scalajs.dom.html.{Input => JSInput}
import org.scalajs.dom.{Element, Event, KeyboardEvent}
import scalatags.JsDom.all._

import scala.concurrent.duration.{Duration, DurationInt}

/** Abstraction for HTML input tags. */
private[bindings] abstract class Input(inputType: String) {
  /**
   * @param value                       Property to bind.
   * @param debounce                    Property update timeout after input changes.
   * @param onInputElementEventReceived Callback that's executed when `Input` element receives one of following events:
   *                                    `Input`, `Change`, `KeyUp`, `Paste` and element value is different than property value.
   * @param inputModifiers              Additional modifiers. Don't use modifiers on value, onchange and onkeyup attributes
   *                                    as they are used internally to sync property value with element value.
   * @return HTML input with bound Property, applied modifiers and nested options.
   */
  def apply(
    value: Property[String] = Property(""),
    debounce: Duration = 20 millis,
    onInputElementEventReceived: String => Unit = _ => ()
  )(
    inputModifiers: Modifier*
  ): InputBinding[JSInput] =
    new InputBinding[JSInput] {
      private val element = input(
        inputModifiers, tpe := inputType,
        nestedInterceptor(new InputModifier(value, debounce, onInputElementEventReceived))
      ).render

      override def render: JSInput = element
    }

  private class InputModifier(
    property: Property[String],
    debounce: Duration,
    onInputElementEventReceived: String => Unit = _ => ()
  ) extends TextInputsModifier(property, debounce, onInputElementEventReceived) {

    override def elementValue(t: Element): String =
      t.asInstanceOf[JSInput].value

    override def setElementValue(t: Element, v: String): Unit =
      t.asInstanceOf[JSInput].value = if (v != null) v else ""

    override def setElementKeyUp(t: Element, callback: KeyboardEvent => Unit): Unit =
      t.asInstanceOf[JSInput].onkeyup = callback

    override def setElementOnChange(t: Element, callback: Event => Unit): Unit =
      t.asInstanceOf[JSInput].onchange = callback

    override def setElementOnInput(t: Element, callback: Event => Unit): Unit =
      t.asInstanceOf[JSInput].oninput = callback

    override def setElementOnPaste(t: Element, callback: Event => Unit): Unit =
      t.asInstanceOf[JSInput].onpaste = callback
  }
}

/** Simple text input. */
object TextInput extends Input("text")

/** Password text input. */
object PasswordInput extends Input("password")

/** Number input. (HTML5) */
object NumberInput extends Input("number")

/** Datetime input (Doesn't work on firefox - falls back to text input). (HTML5) */
object DateTimeLocalInput extends Input("datetime-local")

/** Date input. (HTML5) */
object DateInput extends Input("date")

/** Time input. (HTML5) */
object TimeInput extends Input("time")

