package org.scaladebugger.api.dsl.monitors

import com.sun.jdi.event.MonitorContendedEnteredEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.monitors.MonitorContendedEnteredProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param monitorContendedEnteredProfile The profile to wrap
 */
class MonitorContendedEnteredDSLWrapper private[dsl] (
  private val monitorContendedEnteredProfile: MonitorContendedEnteredProfile
) {
  /** Represents a MonitorContendedEntered event and any associated data. */
  type MonitorContendedEnteredEventAndData = (MonitorContendedEnteredEvent, Seq[JDIEventDataResult])

  /** @see MonitorContendedEnteredProfile#tryGetOrCreateMonitorContendedEnteredRequest(JDIArgument*) */
  def onMonitorContendedEntered(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnteredEvent]] =
    monitorContendedEnteredProfile.tryGetOrCreateMonitorContendedEnteredRequest(extraArguments: _*)

  /** @see MonitorContendedEnteredProfile#getOrCreateMonitorContendedEnteredRequest(JDIArgument*) */
  def onUnsafeMonitorContendedEntered(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnteredEvent] =
    monitorContendedEnteredProfile.getOrCreateMonitorContendedEnteredRequest(extraArguments: _*)

  /** @see MonitorContendedEnteredProfile#getOrCreateMonitorContendedEnteredRequestWithData(JDIArgument*) */
  def onUnsafeMonitorContendedEnteredWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[MonitorContendedEnteredEventAndData] =
    monitorContendedEnteredProfile.getOrCreateMonitorContendedEnteredRequestWithData(
      extraArguments: _*
    )

  /** @see MonitorContendedEnteredProfile#tryGetOrCreateMonitorContendedEnteredRequestWithData(JDIArgument*) */
  def onMonitorContendedEnteredWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[MonitorContendedEnteredEventAndData]] =
    monitorContendedEnteredProfile.tryGetOrCreateMonitorContendedEnteredRequestWithData(
      extraArguments: _*
    )
}
