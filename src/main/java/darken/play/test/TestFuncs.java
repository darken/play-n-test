package darken.play.test;

import static play.mvc.Http.Status.UNAUTHORIZED;
import play.mvc.HandlerRef;
import play.mvc.Result;

/**
 * Test functions.
 * 
 * @author darken
 * 
 */
public class TestFuncs {

	public static void testUnauthorized(HandlerRef actionReference,
			String doesNotContain) {
		Tester<Result> tester = ResultTester.res(UNAUTHORIZED)
				.contains(TestConsts.unauthorized)
				.doesNotContain(doesNotContain);
		Faker.callAction(actionReference).test(tester);
	}

	public static void testAuthorized(HandlerRef actionReference,
			final String contains) {
		Tester<Result> tester = ResultTester.res().contains(contains)
				.doesNotContain(TestConsts.unauthorized);
		Faker.runFakeApp().callLoggedIn(actionReference).test(tester);
	}

	public static void testAuthorizedMatches(HandlerRef actionReference,
			final String regex, final String contentType) {
		Tester<Result> tester = ResultTester.resContent(contentType)
				.matches(regex).doesNotContain(TestConsts.unauthorized);
		Faker.runFakeApp().callLoggedIn(actionReference).test(tester);
	}

}
