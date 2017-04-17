import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplementaryPairs {

	public static void main(String[] args) {
		ComplementaryPairs complementaryPairs = new ComplementaryPairs();

		System.out.println(complementaryPairs.find(new int[] { 7, 1, 5, 6, 9, 3, 11, -1 }, 10));
		System.out.println(complementaryPairs.find(new int[] { 7, 1, 5, 6, 9, 3, 11, -1 }, 6));
		System.out.println(complementaryPairs.find(new int[] { 7, 1, 5, 6, 9, 3, 11, -1 }, 7));
		System.out.println(complementaryPairs.find(new int[] { 7, 1, 5, 6, 9, 3, 11, -1 }, 14));
	}

	private Map<Integer, Integer> find(int[] array, int k) {
		Map<Integer, Integer> complementaries = new HashMap<>();

		List<Integer> arrInList = Arrays.stream(array).boxed().collect(Collectors.toList());

		for (int num : array) {

			int pair = k - num;
			if (arrInList.contains(pair)) {
				complementaries.merge(pair, num, Integer::sum);
			}

		}

		return complementaries;
	}
}
