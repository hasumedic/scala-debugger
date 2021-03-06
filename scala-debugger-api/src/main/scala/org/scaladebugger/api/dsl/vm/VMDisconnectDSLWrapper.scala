package org.scaladebugger.api.dsl.vm

import com.sun.jdi.event.VMDisconnectEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.vm.VMDisconnectProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param vmDisconnectProfile The profile to wrap
 */
class VMDisconnectDSLWrapper private[dsl] (
  private val vmDisconnectProfile: VMDisconnectProfile
) {
  /** Represents a VMDisconnect event and any associated data. */
  type VMDisconnectEventAndData = (VMDisconnectEvent, Seq[JDIEventDataResult])

  /** @see VMDisconnectProfile#tryGetOrCreateVMDisconnectRequest(JDIArgument*) */
  def onVMDisconnect(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDisconnectEvent]] =
    vmDisconnectProfile.tryGetOrCreateVMDisconnectRequest(extraArguments: _*)

  /** @see VMDisconnectProfile#getOrCreateVMDisconnectRequest(JDIArgument*) */
  def onUnsafeVMDisconnect(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDisconnectEvent] =
    vmDisconnectProfile.getOrCreateVMDisconnectRequest(extraArguments: _*)

  /** @see VMDisconnectProfile#getOrCreateVMDisconnectRequestWithData(JDIArgument*) */
  def onUnsafeVMDisconnectWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[VMDisconnectEventAndData] =
    vmDisconnectProfile.getOrCreateVMDisconnectRequestWithData(extraArguments: _*)

  /** @see VMDisconnectProfile#tryGetOrCreateVMDisconnectRequestWithData(JDIArgument*) */
  def onVMDisconnectWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDisconnectEventAndData]] =
    vmDisconnectProfile.tryGetOrCreateVMDisconnectRequestWithData(extraArguments: _*)
}
