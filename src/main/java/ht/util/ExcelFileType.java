package ht.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelFileType {

	/**
	 * 엑셀파일을 읽어서 Workbook을 리턴한다.
	 * XLS와 XLSX 확장자를 비교한다.
	 * @param file
	 * @return
	 */
	public static Workbook getWorkBook(MultipartFile file) {
		Workbook wb = null;

		if(file.getOriginalFilename().toUpperCase().endsWith(".XLS")) {
			try {
				wb = new HSSFWorkbook(file.getInputStream());
			}catch(Exception e) {
				HTUtils.batmanNeverDie(e);
				wb = null;
				return null;
			}
		}else if(file.getOriginalFilename().toUpperCase().endsWith(".XLSX")) {
			try {
				wb = new XSSFWorkbook(file.getInputStream());
			}catch(Exception e) {
				HTUtils.batmanNeverDie(e);
				wb = null;
				return null;
			}
		}
		return wb;
	}
}
