package org.scaladebugger.api.profiles.traits.info

import com.sun.jdi.ClassLoaderReference

/**
 * Represents the interface for "class loader"-based interaction.
 */
trait ClassLoaderInfoProfile extends ObjectInfoProfile with CommonInfoProfile {
  /**
   * Returns the JDI representation this profile instance wraps.
   *
   * @return The JDI instance
   */
  override def toJdiInstance: ClassLoaderReference

  /**
   * Retrieves all loaded classes defined by this class loader.
   *
   * @return The collection of reference types for the loaded classes
   */
  def definedClasses: Seq[ReferenceTypeInfoProfile]

  /**
   * Retrieves all classes for which this class loader served as the initiating
   * loader.
   *
   * @return The collection of reference types for the initiated classes
   */
  def visibleClasses: Seq[ReferenceTypeInfoProfile]
}
