package lk.recruitment.management.asset.applicant.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lk.recruitment.management.asset.applicant.dao.ApplicantDao;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplyingRank;
import lk.recruitment.management.util.interfaces.AbstractService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig( cacheNames = "applicant" )
public class ApplicantService implements AbstractService< Applicant, Integer > {

  private final ApplicantDao applicantDao;

  @Autowired
  public ApplicantService(ApplicantDao applicantDao) {
    this.applicantDao = applicantDao;
  }

  @Cacheable
  public List< Applicant > findAll() {
    return applicantDao.findAll();
  }

  @Cacheable
  public Applicant findById(Integer id) {
    return applicantDao.getOne(id);
  }

  @Caching( evict = {@CacheEvict( value = "applicant", allEntries = true )},
      put = {@CachePut( value = "applicant", key = "#applicant.id" )} )
  @Transactional
  public Applicant persist(Applicant applicant) {
    if ( applicant.getId() == null ) {
      applicant.setApplicantStatus(ApplicantStatus.P);
    }
    return applicantDao.save(applicant);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    applicantDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< Applicant > search(Applicant applicant) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Applicant > applicantExample = Example.of(applicant, matcher);
    return applicantDao.findAll(applicantExample);
  }

  public boolean isApplicantPresent(Applicant applicant) {
    return applicantDao.equals(applicant);
  }


  public Applicant lastApplicant() {
    return applicantDao.findFirstByOrderByIdDesc();
  }

  @Cacheable
  public Applicant findByNic(String nic) {
    return applicantDao.findByNic(nic);
  }

  public Applicant findByEmail(String email) {
    return applicantDao.findByEmail(email);
  }

  public int countByApplicantStatus(ApplicantStatus applicantStatus) {
    return applicantDao.countByApplicantStatus(applicantStatus);
  }

  public List< Applicant > findByApplicantStatus(ApplicantStatus applicantStatus) {
    return applicantDao.findByApplicantStatus(applicantStatus);
  }

  public List< Applicant > findByCreatedAtIsBetweenAndApplyingRankAndApplicantStatus(LocalDateTime form,
                                                                                     LocalDateTime to,
                                                                                     ApplyingRank applyingRank,
                                                                                     ApplicantStatus applicantStatus) {
    return applicantDao.findByCreatedAtIsBetweenAndApplyingRankAndApplicantStatus(form, to, applyingRank,
                                                                                  applicantStatus);
  }

  public boolean createExcel(List< Applicant > applicants, ServletContext context,
                              HttpServletRequest request, HttpServletResponse response, String sheetName) {
    System.out.println("hey im here");
    String filePath = context.getRealPath("/resources/report");
    File file = new File(filePath);
    boolean exists = new File(filePath).exists();
    if ( !exists ) {
      new File(filePath).mkdirs();
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(file + "/" + sheetName + ".xls");
      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet workSheet = workbook.createSheet(sheetName);
      workSheet.setDefaultColumnWidth(30);

//header style
      HSSFCellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
      headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
      headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
      headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
      headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
      Font font = workbook.createFont();
      font.setColor(HSSFColor.BLUE_GREY.index);
      headerCellStyle.setFont(font);

      //cell style
      HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
      bodyCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
      bodyCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
      bodyCellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
      bodyCellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
      bodyCellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);


      HSSFRow headerRow = workSheet.createRow(0);

      HSSFCell index = headerRow.createCell(0);
      index.setCellValue("Index");
      index.setCellStyle(headerCellStyle);

      HSSFCell registerNumber = headerRow.createCell(1);
      registerNumber.setCellValue("Register Number");
      registerNumber.setCellStyle(headerCellStyle);

      HSSFCell fullName = headerRow.createCell(2);
      fullName.setCellValue("Full Name");
      fullName.setCellStyle(headerCellStyle);

      HSSFCell nic = headerRow.createCell(3);
      nic.setCellValue("NIC");
      nic.setCellStyle(headerCellStyle);

      HSSFCell address = headerRow.createCell(4);
      address.setCellValue("Address");
      address.setCellStyle(headerCellStyle);

      HSSFCell nearestPolice = headerRow.createCell(5);
      nearestPolice.setCellValue("Nearest Police Station");
      nearestPolice.setCellStyle(headerCellStyle);

      HSSFCell result = headerRow.createCell(6);
      result.setCellValue("Result");
      result.setCellStyle(headerCellStyle);

      HSSFCell message = headerRow.createCell(7);
      message.setCellValue("If applicant has not in your record, result mark as 'Pass' if not mark as 'Failed' ");
      message.setCellStyle(headerCellStyle);


      int count = 1;

      for ( Applicant applicant : applicants ) {
        HSSFRow bodyRow = workSheet.createRow(count);


        HSSFCell nameValue = bodyRow.createCell(0);
        nameValue.setCellValue(count);
        nameValue.setCellStyle(bodyCellStyle);


        HSSFCell registerNumberValue = bodyRow.createCell(1);
        registerNumberValue.setCellValue(applicant.getCode());
        registerNumberValue.setCellStyle(bodyCellStyle);

        HSSFCell fullNameValue = bodyRow.createCell(2);
        fullNameValue.setCellValue(applicant.getNameInFullName());
        fullNameValue.setCellStyle(bodyCellStyle);

        HSSFCell nicValue = bodyRow.createCell(3);
       nicValue.setCellValue(applicant.getNic());
        nicValue.setCellStyle(bodyCellStyle);

        HSSFCell addressValue = bodyRow.createCell(4);
        addressValue.setCellValue(applicant.getAddress());
        addressValue.setCellStyle(bodyCellStyle);

        HSSFCell nearestPoliceValue = bodyRow.createCell(6);
        nearestPoliceValue.setCellValue(applicant.getGramaNiladhari().getPoliceStation().getName());
        nearestPoliceValue.setCellStyle(bodyCellStyle);

        HSSFCell resultValue = bodyRow.createCell(7);
        resultValue.setCellStyle(bodyCellStyle);

        count++;
      }

      workbook.write(outputStream);
      outputStream.flush();
      outputStream.close();
      return true;

    } catch ( Exception e ) {
      return false;
    }
  }

  public boolean createPdf(List< Applicant > employees, ServletContext context,
                           HttpServletRequest request, HttpServletResponse response, String title) {
    Document document = new Document(PageSize.A4, 15, 15, 45, 30);
    try {
      String filePath = context.getRealPath("/resources/report");
      File file = new File(filePath);
      boolean exists = new File(filePath).exists();
      if ( !exists ) {
        new File(filePath).mkdirs();
      }
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file + "/" + "applicants" + ".pdf"));
      document.open();
      com.itextpdf.text.Font mainFont = FontFactory.getFont("Arial", 10, BaseColor.BLACK);


      Paragraph paragraph = new Paragraph("Eligible Applicant for "+title, mainFont);
      paragraph.setAlignment(Element.ALIGN_CENTER);
      paragraph.setIndentationLeft(50);
      paragraph.setIndentationRight(50);
      paragraph.setSpacingAfter(10);
      document.add(paragraph);

      PdfPTable table = new PdfPTable(4);//column amount
      table.setWidthPercentage(100);
      table.setSpacingBefore(10f);
      table.setSpacingAfter(10);

      com.itextpdf.text.Font tableHeader = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
      com.itextpdf.text.Font tableBody = FontFactory.getFont("Arial", 9, BaseColor.BLACK);

      float[] columnWidths = {2f, 2f, 2f, 2f};
      table.setWidths(columnWidths);

      PdfPCell name = new PdfPCell(new Paragraph("name", tableHeader));
      name.setBorderColor(BaseColor.BLACK);
      name.setPaddingLeft(10);
      name.setHorizontalAlignment(Element.ALIGN_CENTER);
      name.setVerticalAlignment(Element.ALIGN_CENTER);
      name.setBackgroundColor(BaseColor.DARK_GRAY);
      name.setExtraParagraphSpace(5f);
      table.addCell(name);

      PdfPCell email = new PdfPCell(new Paragraph("email", tableHeader));
      email.setBorderColor(BaseColor.BLACK);
      email.setPaddingLeft(10);
      email.setHorizontalAlignment(Element.ALIGN_CENTER);
      email.setVerticalAlignment(Element.ALIGN_CENTER);
      email.setBackgroundColor(BaseColor.DARK_GRAY);
      email.setExtraParagraphSpace(5f);
      table.addCell(email);

      PdfPCell mobile = new PdfPCell(new Paragraph("mobile", tableHeader));
      mobile.setBorderColor(BaseColor.BLACK);
      mobile.setPaddingLeft(10);
      mobile.setHorizontalAlignment(Element.ALIGN_CENTER);
      mobile.setVerticalAlignment(Element.ALIGN_CENTER);
      mobile.setBackgroundColor(BaseColor.DARK_GRAY);
      mobile.setExtraParagraphSpace(5f);
      table.addCell(mobile);

      PdfPCell address = new PdfPCell(new Paragraph("address", tableHeader));
      address.setBorderColor(BaseColor.BLACK);
      address.setPaddingLeft(10);
      address.setHorizontalAlignment(Element.ALIGN_CENTER);
      address.setVerticalAlignment(Element.ALIGN_CENTER);
      address.setBackgroundColor(BaseColor.DARK_GRAY);
      address.setExtraParagraphSpace(5f);
      table.addCell(address);

      for ( Applicant employee : employees ) {
        PdfPCell nameValue = new PdfPCell(new Paragraph(employee.getNameInFullName(), tableHeader));
        nameValue.setBorderColor(BaseColor.BLACK);
        nameValue.setPaddingLeft(10);
        nameValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameValue.setVerticalAlignment(Element.ALIGN_CENTER);
        nameValue.setBackgroundColor(BaseColor.WHITE);
        nameValue.setExtraParagraphSpace(5f);
        table.addCell(nameValue);

        PdfPCell emailValue = new PdfPCell(new Paragraph(employee.getEmail(), tableHeader));
        emailValue.setBorderColor(BaseColor.BLACK);
        emailValue.setPaddingLeft(10);
        emailValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailValue.setVerticalAlignment(Element.ALIGN_CENTER);
        emailValue.setBackgroundColor(BaseColor.WHITE);
        emailValue.setExtraParagraphSpace(5f);
        table.addCell(emailValue);

        PdfPCell mobileValue = new PdfPCell(new Paragraph(employee.getMobile(), tableHeader));
        mobileValue.setBorderColor(BaseColor.BLACK);
        mobileValue.setPaddingLeft(10);
        mobileValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        mobileValue.setVerticalAlignment(Element.ALIGN_CENTER);
        mobileValue.setBackgroundColor(BaseColor.WHITE);
        mobileValue.setExtraParagraphSpace(5f);
        table.addCell(mobileValue);

        PdfPCell addressValue = new PdfPCell(new Paragraph(employee.getAddress(), tableHeader));
        addressValue.setBorderColor(BaseColor.BLACK);
        addressValue.setPaddingLeft(10);
        addressValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        addressValue.setVerticalAlignment(Element.ALIGN_CENTER);
        addressValue.setBackgroundColor(BaseColor.WHITE);
        addressValue.setExtraParagraphSpace(5f);
        table.addCell(addressValue);
      }

      document.add(table);
      document.close();
      writer.close();
      return true;


    } catch ( Exception e ) {
      return false;
    }
  }

}
