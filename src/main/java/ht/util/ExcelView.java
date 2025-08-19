package ht.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelView extends AbstractExcelPOIView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<Map<String, Object>> titleList = (List<Map<String, Object>>)model.get("titleList");

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) model.get("dataList");

		String sheetName = StringUtil.nvl(model.get("sheetName"),"템플릿양식");

		// Sheet 생성
		Sheet sheet = workbook.createSheet(sheetName);
		Row row = null;
		Cell cell = null;
		int rowCount = 0;
		int cellCount = 0;

		// 첫번째 로우 폰트 설정
		Font headFont = workbook.createFont();
		headFont.setFontHeightInPoints((short) 11);
		headFont.setFontName("돋움");
		headFont.setBold(true);

		// 첫번째 로우 셀 스타일 설정
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headStyle.setFont(headFont);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);

		// 바디 폰트 설정
		Font bodyFont = workbook.createFont();
		bodyFont.setFontHeightInPoints((short) 9);
		bodyFont.setFontName("돋움");

		// 바디 스타일 설정
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setFont(bodyFont);
		bodyStyle.setWrapText(true);
		bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setBorderTop(BorderStyle.THIN);

		CellStyle bodyStyleAlign = workbook.createCellStyle();
		bodyStyleAlign.setFont(bodyFont);
		bodyStyleAlign.setWrapText(true);
		bodyStyleAlign.setAlignment(HorizontalAlignment.CENTER);
		bodyStyleAlign.setVerticalAlignment(VerticalAlignment.CENTER);
		bodyStyleAlign.setBorderBottom(BorderStyle.THIN);
		bodyStyleAlign.setBorderLeft(BorderStyle.THIN);
		bodyStyleAlign.setBorderRight(BorderStyle.THIN);
		bodyStyleAlign.setBorderTop(BorderStyle.THIN);

		// 제목 셀 생성
		row = sheet.createRow(rowCount++);

		for(Map<String,Object> map : titleList) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headStyle);
			cell.setCellValue(String.valueOf(map.get("title")));
		}

		// 데이터 셀 생성
		for(Map<String, Object> map1 : dataList) {
			row = sheet.createRow(rowCount++);
			cellCount = 0;
			for(Map<String,Object> map2 : titleList) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(bodyStyle);
				String data = "";
				String dataName = StringUtil.nvl(map2.get("data"));
				if(!"".equals(dataName)) {
					data = StringUtil.nvl(map1.get(dataName));
				}
				cell.setCellValue(data);
			}
		}

		// 셀 와이드 설정
		int idx = 0;
		for(Map<String,Object> map : titleList) {
			sheet.autoSizeColumn(idx, true);
			if(map.get("width") != null && (int)map.get("width") > 0) {
				// 셀별로 원하는 width 적용
				log.debug(idx + " : " + (int)map.get("width"));
				sheet.setColumnWidth(idx, (int)map.get("width")*100);
			}
			idx++;
		}
	}

	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

}
