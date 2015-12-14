package org.senkbeil.debugger.api.lowlevel.monitors

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import org.senkbeil.debugger.api.lowlevel.requests.JDIRequestArgument
import test.TestMonitorContendedEnterManager

import scala.util.Success

class MonitorContendedEnterManagerSpec extends FunSpec with Matchers
  with OneInstancePerTest with MockFactory
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockMonitorContendedEnterManager = mock[MonitorContendedEnterManager]
  private val testMonitorContendedEnterManager = new TestMonitorContendedEnterManager(
    mockMonitorContendedEnterManager
  ) {
    override protected def newRequestId(): String = TestRequestId
  }

  describe("MonitorContendedEnterManager") {
    describe("#createMonitorContendedEnterRequest") {
      it("should invoke createMonitorContendedEnterRequestWithId") {
        val expected = Success(TestRequestId)
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMonitorContendedEnterManager.createMonitorContendedEnterRequestWithId _)
          .expects(TestRequestId, testExtraArguments)
          .returning(expected).once()

        val actual = testMonitorContendedEnterManager.createMonitorContendedEnterRequest(
          testExtraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createMonitorContendedEnterRequestFromInfo") {
      it("should invoke createMonitorContendedEnterRequestWithId") {
        val expected = Success(TestRequestId)
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMonitorContendedEnterManager.createMonitorContendedEnterRequestWithId _)
          .expects(TestRequestId, testExtraArguments)
          .returning(expected).once()

        val info = MonitorContendedEnterRequestInfo(
          TestRequestId,
          testExtraArguments
        )
        val actual = testMonitorContendedEnterManager.createMonitorContendedEnterRequestFromInfo(info)

        actual should be(expected)
      }
    }
  }
}
