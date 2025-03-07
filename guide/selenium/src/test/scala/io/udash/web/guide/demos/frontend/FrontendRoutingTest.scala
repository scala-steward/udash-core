package io.udash.web.guide.demos.frontend

import io.udash.web.SeleniumTest

class FrontendRoutingTest extends SeleniumTest {
  override protected final val url = "/frontend/routing"

  "FrontendRouting view" should {
    "contain demo element" in {
      val link = findElementById("url-demo-link")
        link.getText should be("/frontend/routing")
    }

    "change URL without view redraw" in {
      val link = findElementById("url-demo-link")
      val input = findElementById("url-demo-input")

      val apple = findElementById("url-demo-link-apple")
      val orange = findElementById("url-demo-link-orange")
      val chocolate = findElementById("url-demo-link-chocolate")
      val pizza = findElementById("url-demo-link-pizza")

      link.getText should be("/frontend/routing")

      input.clear()
      input.sendKeys("It should not disappear... Selenium")

      apple.click()
      Thread.sleep(500) // wait for scroll
      eventually {
        driver.getCurrentUrl should endWith("/frontend/routing/apple")
        link.getText should be("/frontend/routing/apple")
      }

      orange.click()
      eventually {
        driver.getCurrentUrl should endWith("/frontend/routing/orange")
        link.getText should be("/frontend/routing/orange")
      }

      chocolate.click()
      eventually {
        driver.getCurrentUrl should endWith("/frontend/routing/chocolate")
        link.getText should be("/frontend/routing/chocolate")
      }

      pizza.click()
      eventually {
        driver.getCurrentUrl should endWith("/frontend/routing/pizza")
        link.getText should be("/frontend/routing/pizza")
      }

      input.getDomProperty("value") should be("It should not disappear... Selenium")
    }

    "change URL basing on input without view redraw" in {
      val link = findElementById("url-demo-link")
      val input = findElementById("url-demo-input")

      val linkChanger = findElementById("url-demo-link-input")
      val init = findElementById("url-demo-link-init")

      init.getText should be("/frontend/routing")
      link.getText should be("/frontend/routing")

      input.clear()
      input.sendKeys("It should not disappear... Selenium")

      for (s <- Seq("test", "test with space", "hash#hash")) {
        linkChanger.clear()
        linkChanger.sendKeys(s)
        eventually {
          val escaped = s"/frontend/routing/${s.replaceAll(" ", "%20").replaceAll("#", "%23")}"
          val unescaped = s"/frontend/routing/$s"
          init.getText should be("/frontend/routing")
          link.getText should matchPattern {
            case str: String if str == escaped || str == unescaped =>
          }
        }
      }

      init.getText should be("/frontend/routing")
      input.getDomProperty("value") should be("It should not disappear... Selenium")
    }
  }
}
