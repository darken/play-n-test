package darken.play.test;

import java.lang.reflect.Field;

import play.Logger;

/**
 * Test constants.
 * 
 * @author darken
 * 
 */
public class TestConsts {

	static String textHtml = "text/html";
	static String charset = "utf-8";

	static String username = "username";
	static String usernameValue = "su";

	static String unauthorized = "Unauthorized";

	public static String textHtml() {
		return textHtml;
	}

	public static String charset() {
		return charset;
	}

	public static String username() {
		return username;
	}

	public static String usernameValue() {
		return usernameValue;
	}

	public static String unauthorized() {
		return unauthorized;
	}

	/**
	 * Obtains values from the static fields in the classWithConstants, and
	 * modifies the values of the fields with the same name in this class
	 * constants.
	 * 
	 * @param classWithConstants
	 */
	public static void loadConstants(Class<?> classWithConstants) {
		if (classWithConstants != null) {
			for (Field loadField : classWithConstants.getDeclaredFields()) {
				try {
					String name = loadField.getName();
					Object value = loadField.get(null);

					Field fieldToChange = TestConsts.class
							.getDeclaredField(name);
					fieldToChange.set(null, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					Logger.error(TestConsts.class.getSimpleName()
							+ ".loadConstants - NoSuchFieldException "
							+ e.getMessage());
				}
			}
		}
	}

}
