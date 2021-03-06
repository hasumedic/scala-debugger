package org.scaladebugger.api.profiles.swappable.vm
//import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.vm.VMDeathRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.vm.VMDeathProfile

import scala.util.Try

/**
 * Represents a swappable profile for vm death events that redirects the
 * invocation to another profile.
 */
trait SwappableVMDeathProfile extends VMDeathProfile {
  this: SwappableDebugProfileManagement =>

  override def tryGetOrCreateVMDeathRequestWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[VMDeathEventAndData]] = {
    withCurrentProfile.tryGetOrCreateVMDeathRequestWithData(extraArguments: _*)
  }

  override def isVMDeathRequestWithArgsPending(
    extraArguments: JDIArgument*
  ): Boolean = {
    withCurrentProfile.isVMDeathRequestWithArgsPending(extraArguments: _*)
  }

  override def removeVMDeathRequestWithArgs(
    extraArguments: JDIArgument*
  ): Option[VMDeathRequestInfo] = {
    withCurrentProfile.removeVMDeathRequestWithArgs(extraArguments: _*)
  }

  override def removeAllVMDeathRequests(): Seq[VMDeathRequestInfo] = {
    withCurrentProfile.removeAllVMDeathRequests()
  }

  override def vmDeathRequests: Seq[VMDeathRequestInfo] = {
    withCurrentProfile.vmDeathRequests
  }
}
