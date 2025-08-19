package ht.util;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"local"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FtpClientTest {

	@Resource
	private HTUtils htUtils;
	private FakeFtpServer fakeFtpServer;

	@Before
	public void setup() throws IOException {

		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount("happy", "happy1234", "/data"));
		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/data"));
		fileSystem.add(new FileEntry("/data/foobar.txt", "요것은 한글 맛"));
		fakeFtpServer.setFileSystem(fileSystem);
		fakeFtpServer.setServerControlPort(9921);

		fakeFtpServer.start();
	}

	@Test
	public void ftpClient() throws IOException {

		// 업로드 테스트
		htUtils.ftpUpload("localhost", fakeFtpServer.getServerControlPort(), "/data/", "upload.txt"
				, "happy", "happy1234"
				, "푸하하\n우히히 메롱\n");

		// 다운로드 테스트
		String ftpUrl = String.format(
				"ftp://happy:happy1234@localhost:%d/upload.txt", fakeFtpServer.getServerControlPort());

		URLConnection urlConnection = new URL(ftpUrl).openConnection();
		InputStream inputStream = urlConnection.getInputStream();
		Files.copy(inputStream, new File("/storage/__FTP__TEST__FILE__").toPath());
		inputStream.close();

		assertThat(new File("/storage/__FTP__TEST__FILE__")).exists();
	}

	@After
	public void teardown() throws IOException {

		if (fakeFtpServer != null) {
			fakeFtpServer.stop();
		}
	}
}
