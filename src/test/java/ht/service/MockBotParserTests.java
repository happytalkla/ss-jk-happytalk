package ht.service;

import ht.service.chatbot.MockChatBotParser;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"local"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MockBotParserTests {

	@Resource
	private MockChatBotParser parser;

	@Test
	public void testParse() {

		parser.parse("MOCK CONTENTS");
	}
}
