import java.util.regex.Matcher;  
import java.util.regex.Pattern; 

public class Test {

	public static void main(String[] args) {
		System.out.println("Hello, World!");

		Pattern p = Pattern.compile("12&12");
		Matcher m = p.matcher("12&12");
		
		System.out.println(m.find());
	}
}
