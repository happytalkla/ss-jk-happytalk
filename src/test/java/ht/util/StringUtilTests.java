package ht.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootConfiguration
@ActiveProfiles({"local"})
public class StringUtilTests {

	@Test
	public void getMaskedString() {

		assertThat(StringUtil.getMaskedName("김성운")).isEqualTo("김*운");
		assertThat(StringUtil.getMaskedName("김성")).isEqualTo("김*");

		assertThat(StringUtil.getMaskedString("{\"v\":\"0.1\",\"balloons\":[{\"sections\":[{\"type\":\"text\",\"data\":\"0101231234\"}]}]}"))
		.isEqualTo("{\"v\":\"0.1\",\"balloons\":[{\"sections\":[{\"type\":\"text\",\"data\":\"###########\"}]}]}");
		assertThat(StringUtil.getMaskedString("{\"v\":\"0.1\",\"balloons\":[{\"sections\":[{\"type\":\"text\",\"data\":\"010-123-1234\"}]}]}"))
		.isEqualTo("{\"v\":\"0.1\",\"balloons\":[{\"sections\":[{\"type\":\"text\",\"data\":\"###-####-####\"}]}]}");

		//		assertThat(StringUtil.getMaskedString("010-123-1234")).isEqualTo("###-####-####");
		//		assertThat(StringUtil.getMaskedString("010/123/1234")).isEqualTo("***/****/****");
		//		assertThat(StringUtil.getMaskedString("010*123*1234")).isEqualTo("*************");
		//		assertThat(StringUtil.getMaskedString("010+123+1234")).isEqualTo("***+****+****");
		//		assertThat(StringUtil.getMaskedString("010~123~1234")).isEqualTo("***~****~****");
		//		assertThat(StringUtil.getMaskedString("801010-2042833")).isEqualTo("******-*******");
		//		assertThat(StringUtil.getMaskedString("010 2313 302399")).isEqualTo("*** **** ****99");
		//		assertThat(StringUtil.getMaskedString("010     2313   -  3023")).isEqualTo("***     ****   -  ****");
		//		assertThat(StringUtil.getMaskedString("8010102042833")).isEqualTo("*************");
		//		assertThat(StringUtil.getMaskedString("0000008010102042833000000")).isEqualTo("00*************2833000000");
	}

	@Test
	public void testReplaceHtmlTagToAsciiCode() {

		String result = StringUtil.replaceHtmlTagToAsciiCode("&lt;div&gt;");
		assertThat(result).isEqualTo("<div>");
	}
}
