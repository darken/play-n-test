package darken.play.test;

import static play.test.Helpers.POST;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.Map;

import play.mvc.HandlerRef;
import play.mvc.Result;
import play.test.FakeRequest;
import play.test.Helpers;

public abstract class Faker<T> {

	private Faker<?> first;

	private Faker<?> next;

	private Tester<T> tester;

	private Faker(Faker<?> previous) {
		if (previous != null) {
			this.first = previous.first;
		} else {
			this.first = this;
		}
	}

	private void run() {
		first.config();
	}

	public void test(Tester<T> tester) {
		this.tester = tester;
		run();
	}

	protected final void doTest(T t) {
		this.tester.test(t);
	}

	protected abstract void config();

	public final void nextConfig() {
		if (next != null)
			next.config();
	}

	public static Faker<Result> callAction(final HandlerRef actionReference) {
		return new Faker<Result>(null) {
			public void config() {
				Result result = Helpers.callAction(actionReference);
				doTest(result);
			}
		};
	}

	public static Faker<Void> runFakeApp() {
		return new Faker<Void>(null) {
			public void config() {
				running(fakeApplication(), new Runnable() {
					@Override
					public void run() {
						nextConfig();
					}
				});
			}
		};
	}

	@SuppressWarnings("unchecked")
	public Faker<Result> callPostForm(final HandlerRef actionReference,
			final Map<String, String> data) {
		this.next = new Faker<Result>(this) {
			@Override
			public void config() {
				Result result = Helpers.callAction(actionReference,
						new FakeRequest(POST, "").withFormUrlEncodedBody(data));
				doTest(result);
			}
		};
		return (Faker<Result>) this.next;
	}

	public Faker<Result> callLoggedIn(final HandlerRef actionReference) {
		return callWithSession(actionReference, TestConsts.username,
				TestConsts.usernameValue);
	}

	@SuppressWarnings("unchecked")
	public Faker<Result> callWithSession(final HandlerRef actionReference,
			final String name, final String value) {
		this.next = new Faker<Result>(this) {
			@Override
			public void config() {
				Result result = Helpers.callAction(actionReference,
						new FakeRequest().withSession(name, value));
				doTest(result);
			}
		};
		return (Faker<Result>) this.next;
	}

}
