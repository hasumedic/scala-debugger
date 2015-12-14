package test

import com.sun.jdi.request.ExceptionRequest
import org.senkbeil.debugger.api.lowlevel.exceptions.{ExceptionRequestInfo, ExceptionManager}
import org.senkbeil.debugger.api.lowlevel.requests.JDIRequestArgument

import scala.util.Try

/**
 * Test exception manager that merely invokes the provided exception manager
 * underneath to make it easier to mock.
 *
 * @param exceptionManager The underlying exception manager used to execute
 *                          all methods
 */
class TestExceptionManager(
  private val exceptionManager: ExceptionManager
) extends ExceptionManager {
  override def removeCatchallExceptionRequest(): Boolean =
    exceptionManager.removeCatchallExceptionRequest()
  override def createExceptionRequestWithId(requestId: String, exceptionName: String, notifyCaught: Boolean, notifyUncaught: Boolean, extraArguments: JDIRequestArgument*): Try[String] =
    exceptionManager.createExceptionRequestWithId(requestId, exceptionName, notifyCaught, notifyUncaught, extraArguments: _*)
  override def hasCatchallExceptionRequest: Boolean =
    exceptionManager.hasCatchallExceptionRequest
  override def exceptionRequestList: Seq[ExceptionRequestInfo] =
    exceptionManager.exceptionRequestList
  override def createCatchallExceptionRequestWithId(requestId: String, notifyCaught: Boolean, notifyUncaught: Boolean, extraArguments: JDIRequestArgument*): Try[String] =
    exceptionManager.createCatchallExceptionRequestWithId(requestId, notifyCaught, notifyUncaught, extraArguments: _*)
  override def exceptionRequestListById: Seq[String] =
    exceptionManager.exceptionRequestListById
  override def getCatchallExceptionRequest: Option[ExceptionRequest] =
    exceptionManager.getCatchallExceptionRequest
  override def hasExceptionRequestWithId(requestId: String): Boolean =
    exceptionManager.hasExceptionRequestWithId(requestId)
  override def removeExceptionRequest(exceptionName: String): Boolean =
    exceptionManager.removeExceptionRequest(exceptionName)
  override def getExceptionRequestWithId(requestId: String): Option[Seq[ExceptionRequest]] =
    exceptionManager.getExceptionRequestWithId(requestId)
  override def getCatchallExceptionRequestId: Option[String] =
    exceptionManager.getCatchallExceptionRequestId
  override def hasExceptionRequest(exceptionName: String): Boolean =
    exceptionManager.hasExceptionRequest(exceptionName)
  override def getExceptionRequest(exceptionName: String): Option[Seq[ExceptionRequest]] =
    exceptionManager.getExceptionRequest(exceptionName)
  override def removeExceptionRequestWithId(requestId: String): Boolean =
    exceptionManager.removeExceptionRequestWithId(requestId)
}
