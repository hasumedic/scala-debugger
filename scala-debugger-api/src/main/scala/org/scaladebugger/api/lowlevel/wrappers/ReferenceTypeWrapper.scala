package org.scaladebugger.api.lowlevel.wrappers
import acyclic.file

import org.scaladebugger.api.utils.Logging
import com.sun.jdi.{Value, Field, ReferenceType}
import scala.collection.JavaConverters._

import scala.util.Try

/**
 * Represents a wrapper around a reference type, providing additional methods.
 *
 * @param _referenceType The reference type to wrap
 */
class ReferenceTypeWrapper(private val _referenceType: ReferenceType)
  extends Logging
{
  require(_referenceType != null, "Reference type cannot be null!")

  /**
   * Retrieves the static fields and their values.
   *
   * @return The list of static fields and their respective values
   */
  def staticFieldsAndValues: Seq[(Field, Value)] =
    staticFields
      .map(field => (field, Try(_referenceType.getValue(field))))
      .filter(_._2.isSuccess)
      .map(t => (t._1, t._2.get))

  /**
   * Retrieves the static fields.
   *
   * @return The list of static fields
   */
  def staticFields: Seq[Field] =
    Try(_referenceType.allFields()).map(_.asScala)
      .getOrElse(Nil)
      .filter(_.isStatic)
}
