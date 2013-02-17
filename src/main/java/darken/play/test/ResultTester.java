package darken.play.test;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.redirectLocation;
import static play.test.Helpers.status;
import play.mvc.Result;

public abstract class ResultTester implements Tester<Result> {

	protected ResultTester previous;

	protected ResultTester next;

	public abstract void test(Result result);

	public static ResultTester res() {
		return res(OK);
	}

	public static ResultTester res(final int status) {
		return res(status, TestConsts.textHtml, TestConsts.charset);
	}

	public static ResultTester res(final int status, final String contentType,
			final String charset) {
		return new ResultTester() {
			@Override
			public void test(Result result) {
				assertThat(result).isNotNull();
				assertThat(status(result)).isEqualTo(status);
				assertThat(contentType(result)).isEqualTo(contentType);
				assertThat(charset(result)).isEqualTo(charset);
			}
		};
	}

	public static ResultTester resContent(String contentType) {
		return res(OK, contentType, TestConsts.charset);
	}

	public static ResultTester resRedirect() {
		return res(SEE_OTHER, null, null);
	}

	public final ResultTester redirect(final String location) {
		final ResultTester prev = this;
		return new ResultTester() {
			@Override
			public void test(Result result) {
				prev.test(result);
				assertThat(redirectLocation(result)).isEqualTo(location);
				assertThat(contentAsString(result)).hasSize(0);
			}
		};
	}

	public final ResultTester contains(final String contains) {
		final ResultTester prev = this;
		return new ResultTester() {
			@Override
			public void test(Result result) {
				prev.test(result);
				assertThat(contentAsString(result)).contains(contains);
			}
		};
	}

	public final ResultTester matches(final String regex) {
		final ResultTester prev = this;
		return new ResultTester() {
			@Override
			public void test(Result result) {
				prev.test(result);
				assertThat(contentAsString(result)).matches(regex);
			}
		};
	}

	public final ResultTester doesNotContain(final String notContain) {
		final ResultTester prev = this;
		return new ResultTester() {
			@Override
			public void test(Result result) {
				prev.test(result);
				assertThat(contentAsString(result)).doesNotContain(notContain);
			}
		};
	}

}
