package geggg.util
package http

import org.scalatest._
import Matchers._

class QueryMapSpec extends FlatSpec {

  val m = Map("key1" -> "value1", "key2" -> "あいうえお", "3" -> 33)

  trait Default {
    val default = m
  }

//  trait SjisEncode {
//    val sjisMap = QueryMap(m, "Shift_JIS")
//  }

  trait Empty {
    val empty = Map.empty
  }

  "Instance" should "be correct by normal Map" in
    new Default {
    default.isInstanceOf[Map[String, Any]] shouldBe (true)
    default.size shouldEqual (3)
  }

  it should "be correct with empty map" in
    new Empty {
    empty.isInstanceOf[Map[String, Any]] shouldBe (true)
    empty.size shouldEqual (0)
  }

  "Method '([key])'" should "return String value when using existing key" in
    new Default {
    default("key2") shouldEqual ("あいうえお")
  }

  it should "occur NoSuchElementException when using non-existing key" in
    new Default {
    intercept[NoSuchElementException] {
      default("keyA")
    }
  }

  "Method 'get'" should "return Option[String] type when using existing key" in
    new Default {
    default.get("key1") shouldBe a [Option[String]]
  }

  it should "return None when using non-existing key" in
    new Default {
    default.get("keyA") shouldBe (None)
  }

  "Method 'queryString'" should "return 'key=value' format when using existing key" in
    new Default {
    default.queryString("key1") shouldEqual ("key1=value1")
  }

  it should "return 'key=utf8_encoded_value' format when using existing key and multibyte value" in
    new Default {
    default.queryString("key2") shouldEqual ("key2=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A")
  }

  it should "return 'key=' format when using non-existing key" in
    new Default {
    default.queryString("keyA") shouldEqual ("keyA=")
  }

  it should "return 'key=sjis_encoded_value' format when using existing key and multibyte value" in
    new Default {
    default.queryString("key2", "Shift_JIS") shouldEqual ("key2=%82%A0%82%A2%82%A4%82%A6%82%A8")
  }

  "Method 'queries'" should "return 'key=value&keyN=valueN&...' using default encode" in
    new Default {
    default.queries() shouldEqual ("key1=value1&key2=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A&3=33")
  }

  it should "return 'key=value&keyN=valueN&...' using sjis encode" in
    new Default {
    default.queries("Shift_JIS") shouldEqual ("key1=value1&key2=%82%A0%82%A2%82%A4%82%A6%82%A8&3=33")
  }

}
