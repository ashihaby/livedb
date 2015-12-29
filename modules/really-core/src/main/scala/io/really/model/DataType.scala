/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.model

import io.really.R
import play.api.libs.json._
import scala.util.{ Failure, Success, Try }

class DataTypeException(msg: String) extends Exception(msg)

trait DataType[T] {

  protected def fmt: Format[T]

  def readJsValue(in: JsValue): JsResult[JsValue] = in.validate(fmt).map(a => fmt.writes(a))

  def readValue(in: JsValue): JsResult[T] = in.validate(fmt)

  def valueAsOpt(in: JsValue): Option[T] = readValue(in).asOpt

  def valueAsOpt(in: JsLookupResult): Option[JsValue] = in.validateOpt[JsValue].get //readValue(in).asOpt

  protected def getNativeValue(in: Object): T = in.asInstanceOf[T]

  def writeJsValue(in: Object): Try[JsValue] =
    try {
      Success(Json.toJson(getNativeValue(in))(fmt))
    } catch {
      case a: ClassCastException =>
        println("[TODO FIX ME] Could not cast the js snippet return:" + a.getMessage);
        Failure(new DataTypeException("input data type does not match the field type:" + a.getMessage))
    }
}

object DataType {

  case object RString extends DataType[String] {
    protected def fmt = implicitly[Format[String]]
  }

  case object RLong extends DataType[Long] {
    protected def fmt = implicitly[Format[Long]]

    protected override def getNativeValue(in: Object): Long = in match {
      case a: java.lang.Integer => a.longValue()
      case b: java.lang.Long => b
      case c: java.lang.Double => c.longValue()
      case _ => super.getNativeValue(in)
    }
  }

  case object RDouble extends DataType[Double] {
    protected def fmt = implicitly[Format[Double]]
  }

  case object RBoolean extends DataType[Boolean] {
    protected def fmt = implicitly[Format[Boolean]]
  }

  case object Reference extends DataType[R] {
    protected def fmt = implicitly[Format[R]]
  }

  //todo: this is how to define a custom type
  //case class CustomType(a: ) extends DataType
}