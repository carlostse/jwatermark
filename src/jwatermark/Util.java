package jwatermark;

public class Util {

	/**
	 * Logging function
	 * @param str
	 */
	public static void log(String str){
		System.out.println(str);
	}

	/**
	 * Logging function
	 * @param str
	 * @param e
	 */
	public static void log(String str, Throwable e){
		System.out.println(str);
		if (e != null)
			e.printStackTrace();
	}

	/**
	 * @param str
	 * @return true if the string is null or empty
	 */
	public static boolean isMissing(String str){
		return str == null || str.trim().length() == 0;
	}

}
