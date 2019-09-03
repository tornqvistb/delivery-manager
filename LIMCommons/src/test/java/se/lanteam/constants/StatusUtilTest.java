package se.lanteam.constants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.lanteam.LimCommonsApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LimCommonsApplication.class)
public class StatusUtilTest {
	@Test
	public void testConcatinateArrays() {
		String[] arr1 = { "1", "2" };
		String[] arr2 = { "3", "4" };
		String[] arr3 = StatusUtil.concatenate(arr1, arr2);
		assert (arr3.length == 4);
	}
}
