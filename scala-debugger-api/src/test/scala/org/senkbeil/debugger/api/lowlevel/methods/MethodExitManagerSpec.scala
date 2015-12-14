package org.senkbeil.debugger.api.lowlevel.methods

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import org.senkbeil.debugger.api.lowlevel.requests.JDIRequestArgument
import test.TestMethodExitManager

import scala.util.Success

class MethodExitManagerSpec extends FunSpec with Matchers
  with OneInstancePerTest with MockFactory
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockMethodExitManager = mock[MethodExitManager]
  private val testMethodExitManager = new TestMethodExitManager(
    mockMethodExitManager
  ) {
    override protected def newRequestId(): String = TestRequestId
  }

  describe("MethodExitManager") {
    describe("#createMethodExitRequest") {
      it("should invoke createMethodExitRequestWithId") {
        val expected = Success(TestRequestId)
        val testClassName = "some.class.name"
        val testMethodName = "someMethodName"
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMethodExitManager.createMethodExitRequestWithId _)
          .expects(TestRequestId, testClassName, testMethodName, testExtraArguments)
          .returning(expected).once()

        val actual = testMethodExitManager.createMethodExitRequest(
          testClassName,
          testMethodName,
          testExtraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createMethodExitRequestFromInfo") {
      it("should invoke createMethodExitRequestWithId") {
        val expected = Success(TestRequestId)
        val testClassName = "some.class.name"
        val testMethodName = "someMethodName"
        val testExtraArguments = Seq(stub[JDIRequestArgument])

        (mockMethodExitManager.createMethodExitRequestWithId _)
          .expects(TestRequestId, testClassName, testMethodName, testExtraArguments)
          .returning(expected).once()

        val info = MethodExitRequestInfo(
          TestRequestId,
          testClassName,
          testMethodName,
          testExtraArguments
        )
        val actual = testMethodExitManager.createMethodExitRequestFromInfo(info)

        actual should be(expected)
      }
    }
  }
}
