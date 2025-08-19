package ht.controller.hk;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ht.config.CustomProperty;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/server/files")
@Slf4j
public class FileServerController {

	@Resource
	private CustomProperty customProperty;
	@GetMapping
	@ResponseBody
	public ResponseEntity<List<String>> list(
			@RequestParam("serverNumber") String serverNumber,
			@RequestParam("directoryName") String directoryName)
	{
		File directory = new File(customProperty.getStoragePath() + File.separator + directoryName);
		if (!directory.exists() || !directory.isDirectory()) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}

		File[] list = directory.listFiles();
		if (list != null && list.length > 0) {
			return new ResponseEntity<>(Arrays.stream(list).map(s -> s.getName()).collect(Collectors.toList()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
	}

	@GetMapping(value = "/{fileName:.+}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> get(
			@PathVariable("fileName") @NotEmpty String fileName,
			@RequestParam("serverNumber") String serverNumber,
			@RequestParam("directoryName") String directoryName)
					throws Exception
	{
		fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
		log.debug("GET FILE, WEB SERVER {}, DIR: {}, FILE NAME: {}", serverNumber, directoryName, fileName);

		File directory = new File(customProperty.getStoragePath() + File.separator + directoryName);
		if (!directory.exists() || !directory.isDirectory()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		FileSystemResource file = new FileSystemResource(directory.getAbsolutePath() + File.separator + fileName);

		if (file.exists() && file.isFile()) {
			byte[] media = IOUtils.toByteArray(file.getInputStream());
			return new ResponseEntity<>(media, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/{fileName:.+}")
	@ResponseBody
	public ResponseEntity<String> delete(
			@PathVariable("fileName") @NotEmpty String fileName,
			@RequestParam("serverNumber") String serverNumber,
			@RequestParam("directoryName") String directoryName)
					throws Exception
	{
		fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
		log.debug("DELETE FILE, WEB SERVER {}, DIR: {}, FILE NAME: {}", serverNumber, directoryName, fileName);

		File directory = new File(customProperty.getStoragePath() + File.separator + directoryName);
		if (!directory.exists() || !directory.isDirectory()) {
			log.warn("NO DIRECTORY: {}", directory.getAbsoluteFile());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		File file = new File(directory, fileName);
		if (file.exists() && file.isFile()) {
			File backupDirectory = new File(customProperty.getStoragePath() + File.separator + "bak");
			if (!backupDirectory.exists()) {
				backupDirectory.mkdir();
			}
			File backupFile = new File(backupDirectory, file.getName());
			FileUtils.moveFile(file, backupFile);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			log.warn("NO FILE: {}", file.getAbsoluteFile());
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
