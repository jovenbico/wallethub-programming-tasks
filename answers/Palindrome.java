
public class Palindrome {

	public static void main(String[] args) {
		Palindrome palindrome = new Palindrome();

		String pFormat = "result %b, expected %b \n";

		System.out.printf(pFormat, palindrome.isValid("1221"), Boolean.TRUE);
		System.out.printf(pFormat, palindrome.isValid("1121"), Boolean.FALSE);
		System.out.printf(pFormat, palindrome.isValid("abab"), Boolean.FALSE);
		System.out.printf(pFormat, palindrome.isValid("abba"), Boolean.TRUE);
		System.out.printf(pFormat, palindrome.isValid("jovennevoj"), Boolean.TRUE);
	}

	private boolean isValid(String string) {
		boolean result = false;

		if (string == null)
			return result;

		// string matches the reverse of string
		result = (new StringBuffer(string).reverse().toString()).equals(string);

		return result;
	}
}
