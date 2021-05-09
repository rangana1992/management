package lk.recruitment_management.asset.applicant.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import lk.recruitment_management.asset.applicant.dao.ApplicantDao;
import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_degree_result.entity.ApplicantDegreeResult;
import lk.recruitment_management.asset.applicant_result.entity.ApplicantResult;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantStatus;
import lk.recruitment_management.asset.applicant_result.entity.enums.StreamLevel;
import lk.recruitment_management.asset.non_relative.entity.NonRelative;
import lk.recruitment_management.asset.interview.entity.Enum.InterviewName;
import lk.recruitment_management.asset.interview.service.InterviewService;
import lk.recruitment_management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment_management.util.interfaces.AbstractService;
import lk.recruitment_management.util.service.DateTimeAgeService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicantService implements AbstractService< Applicant, Integer > {

  private final ApplicantDao applicantDao;
  private final InterviewService interviewService;
  private final DateTimeAgeService dateTimeAgeService;

  @Autowired
  public ApplicantService(ApplicantDao applicantDao, InterviewService interviewService,
                          DateTimeAgeService dateTimeAgeService) {
    this.applicantDao = applicantDao;
    this.interviewService = interviewService;
    this.dateTimeAgeService = dateTimeAgeService;
  }


  public List< Applicant > findAll() {
    return applicantDao.findAll();
  }


  public Applicant findById(Integer id) {
    return applicantDao.getOne(id);
  }


  public Applicant persist(Applicant applicant) {

    return applicantDao.save(applicant);
  }

 public boolean delete(Integer id) {
    applicantDao.deleteById(id);
    return false;
  }


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


  public Applicant findByNic(String nic) {
    return applicantDao.findByNic(nic);
  }

  public Applicant findByEmail(String email) {
    return applicantDao.findByEmail(email);
  }
//todo -> need to applicant gazette

//  public int countByApplicantStatus(ApplicantStatus applicantStatus) {
//    return applicantDao.countByApplicantStatus(applicantStatus);
//  }
//
//  public List< Applicant > findByApplicantStatus(ApplicantStatus applicantStatus) {
//    return applicantDao.findByApplicantStatus(applicantStatus);
//  }

  public List< Applicant > findByCreatedAtIsBetweenAndApplicantStatus(LocalDateTime form,
                                                                                     LocalDateTime to) {
    return applicantDao.findByCreatedAtIsBetween(form, to);
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
      HSSFFont font = workbook.createFont();
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
      message.setCellValue("If applicant has not in your record, result mark as 'PASS' if not mark as 'FAILED' if " +
                               "some one absent please mark as 'ABSENT' ");
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

        HSSFCell nearestPoliceValue = bodyRow.createCell(5);
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

  private final Font mainHeadingFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD | Font.UNDERLINE);
  private final Font secondaryFont = FontFactory.getFont("Arial", 8, BaseColor.BLACK);
  private final Font tableHeader = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
  private final Font tableHeaderOnly = FontFactory.getFont("Arial", 12, BaseColor.BLACK);

  public ByteArrayInputStream createPDF(Integer id) throws DocumentException {
    //todo
//need to find
    List< Applicant > applicants =applicantDao.findAll();
        //findByApplicantStatus(ApplicantStatus.FST);


    ByteArrayOutputStream out = new ByteArrayOutputStream();

    for ( Applicant applicant : applicants ) {
      Document document = new Document(PageSize.A4, 15, 15, 45, 30);
      PdfWriter.getInstance(document, out);
      document.open();
//page size
      Rectangle page = document.getPageSize();
      // add title to the form
      document.addTitle("Sri Lanka Police Department - Recruitment Division");
      Paragraph header = new Paragraph();
      // We add one empty line
      addEmptyLine(header, 1);
      // Lets write a big header
      header.add(new Paragraph("Applicant Name : " + applicant.getNameInFullName(), mainHeadingFont));
      document.add(header);
// todo need to mentioned gazette also here
 /*
   Paragraph gazzet = new Paragraph();
      // We add one empty line
      addEmptyLine(gazzet, 1);
      // Lets write a big header
      header.add(new Paragraph("Applicant Name : " + applicant.getNameInFullName(), mainHeadingFont));
*/
      PdfPTable applicantDetailTable = new PdfPTable(4);//column amount
      applicantDetailTable.setWidthPercentage(100);
      applicantDetailTable.setSpacingBefore(10f);
      applicantDetailTable.setSpacingAfter(10);
      applicantDetailTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

      PdfPCell code = new PdfPCell(new Paragraph("Applicant Reg No : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(code);
      applicantDetailTable.addCell(code);

      PdfPCell codeValue = new PdfPCell(new Paragraph(applicant.getCode(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(codeValue);

      PdfPCell nic = new PdfPCell(new Paragraph("NIC No : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(nic);
      applicantDetailTable.addCell(nic);

      PdfPCell nicValue = new PdfPCell(new Paragraph(applicant.getNic(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(nicValue);
      applicantDetailTable.addCell(nicValue);

      PdfPCell gender = new PdfPCell(new Paragraph("Gender : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(gender);
      applicantDetailTable.addCell(gender);

      PdfPCell genderValue = new PdfPCell(new Paragraph(applicant.getGender().getGender(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(genderValue);
      applicantDetailTable.addCell(genderValue);

      PdfPCell civilStatus = new PdfPCell(new Paragraph("Civil Status : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(civilStatus);
      applicantDetailTable.addCell(civilStatus);

      PdfPCell civilStatusValue = new PdfPCell(new Paragraph(applicant.getCivilStatus().getCivilStatus(),
                                                             tableHeaderOnly));
      pdfCellHeaderCommonStyle(civilStatusValue);
      applicantDetailTable.addCell(civilStatusValue);

      PdfPCell nationality = new PdfPCell(new Paragraph("Nationality : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(nationality);
      applicantDetailTable.addCell(nationality);

      PdfPCell nationalityValue = new PdfPCell(new Paragraph(applicant.getNationality().getNationality(),
                                                             tableHeaderOnly));
      pdfCellHeaderCommonStyle(nationalityValue);
      applicantDetailTable.addCell(nationalityValue);

      PdfPCell dob = new PdfPCell(new Paragraph("Date Of Birth : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(dob);
      applicantDetailTable.addCell(dob);

      PdfPCell dobValue = new PdfPCell(new Paragraph(applicant.getDateOfBirth().toString(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(dobValue);
      applicantDetailTable.addCell(dobValue);

      PdfPCell age = new PdfPCell(new Paragraph("Age : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(age);
      applicantDetailTable.addCell(age);

      PdfPCell ageValue = new PdfPCell(new Paragraph(dateTimeAgeService.getDateDifference(applicant.getDateOfBirth(),
                                                                                          LocalDate.now()),
                                                     tableHeaderOnly));
      pdfCellHeaderCommonStyle(ageValue);
      applicantDetailTable.addCell(ageValue);
//todo : need to take apply rank from applicant_interview

//      PdfPCell rank = new PdfPCell(new Paragraph("Apply Rank : ", tableHeaderOnly));
//      pdfCellHeaderCommonStyle(rank);
//      applicantDetailTable.addCell(rank);
//
//      PdfPCell rankValue = new PdfPCell(new Paragraph(applicant.getApplyingRank().getApplyingRank(),
//      tableHeaderOnly));
//      pdfCellHeaderCommonStyle(rankValue);
//      applicantDetailTable.addCell(rankValue);

      PdfPCell mobile = new PdfPCell(new Paragraph("Mobile No : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(mobile);
      applicantDetailTable.addCell(mobile);

      PdfPCell mobileValue = new PdfPCell(new Paragraph(applicant.getMobile(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(mobileValue);
      applicantDetailTable.addCell(mobileValue);

      PdfPCell land = new PdfPCell(new Paragraph("Land No : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(land);
      applicantDetailTable.addCell(land);

      PdfPCell landValue = new PdfPCell(new Paragraph(applicant.getLand(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(landValue);
      applicantDetailTable.addCell(landValue);

      PdfPCell email = new PdfPCell(new Paragraph("Email : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(email);
      applicantDetailTable.addCell(email);

      PdfPCell emailValue = new PdfPCell(new Paragraph(applicant.getEmail(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(emailValue);
      applicantDetailTable.addCell(emailValue);

      PdfPCell address = new PdfPCell(new Paragraph("Address : ", tableHeaderOnly));
      pdfCellHeaderCommonStyle(address);
      applicantDetailTable.addCell(address);

      PdfPCell addressValue = new PdfPCell(new Paragraph(applicant.getAddress(), tableHeaderOnly));
      pdfCellHeaderCommonStyle(addressValue);
      applicantDetailTable.addCell(addressValue);

      document.add(applicantDetailTable);

      Paragraph heightWeightChest = new Paragraph();
      heightWeightChest.add(new Paragraph("Chest (cm) : " + applicant.getChest() + "  Weight (cm) : " + applicant.getWeight() + "  Height (cm) : " + applicant.getHeight(), mainHeadingFont));
      document.add(heightWeightChest);

      List< ApplicantResult > applicantResultOL =
          applicant.getApplicantResults().stream().filter(x -> x.getStreamLevel().equals(StreamLevel.OL)).collect(Collectors.toList());

      PdfPTable OLResults = null;//column amount
      PdfPCell subject = null;
      PdfPCell result = null;
      PdfPCell attempt = null;
      PdfPCell index = null;
      if ( !applicantResultOL.isEmpty() ) {
        OLResults = new PdfPTable(4);
        OLResults.setWidthPercentage(100);
        OLResults.setSpacingBefore(10f);
        OLResults.setSpacingAfter(10);
        OLResults.setTotalWidth((page.getWidth() - document.leftMargin() - document.rightMargin()) / 2);


        subject = new PdfPCell(new Paragraph("Subject", tableHeaderOnly));
        pdfCellHeaderCommonStyle(subject);
        OLResults.addCell(subject);

        result = new PdfPCell(new Paragraph("Result", tableHeaderOnly));
        pdfCellHeaderCommonStyle(result);
        OLResults.addCell(result);

        attempt = new PdfPCell(new Paragraph("Attempt", tableHeaderOnly));
        pdfCellHeaderCommonStyle(attempt);
        OLResults.addCell(attempt);

        index = new PdfPCell(new Paragraph("Index ", tableHeaderOnly));
        pdfCellHeaderCommonStyle(index);
        OLResults.addCell(index);


        for ( ApplicantResult value : applicantResultOL ) {
          PdfPCell subjectNameValue = new PdfPCell(new Paragraph(value.getSubjectName(), tableHeader));
          pdfCellBodyCommonStyle(subjectNameValue);
          OLResults.addCell(subjectNameValue);

          PdfPCell resultValue =
              new PdfPCell(new Paragraph(value.getSubjectResult().getSubjectResult(), tableHeader));
          pdfCellBodyCommonStyle(resultValue);
          OLResults.addCell(resultValue);

          PdfPCell attemptValue =
              new PdfPCell(new Paragraph(value.getAttempt().getAttempt(), tableHeader));
          pdfCellBodyCommonStyle(attemptValue);
          OLResults.addCell(attemptValue);

          PdfPCell indexValue =
              new PdfPCell(new Paragraph(value.getIndexNumber(), tableHeader));
          pdfCellBodyCommonStyle(indexValue);
          OLResults.addCell(indexValue);
        }

        document.add(OLResults);
      }


      List< ApplicantResult > applicantResultAL =
          applicant.getApplicantResults().stream().filter(x -> x.getStreamLevel().equals(StreamLevel.AL)).collect(Collectors.toList());

      if ( !applicantResultAL.isEmpty() ) {
        PdfPTable ALResults = new PdfPTable(4);//column amount
        ALResults.setWidthPercentage(100);
        ALResults.setSpacingBefore(10f);
        ALResults.setSpacingAfter(10);
        ALResults.setTotalWidth((page.getWidth() - document.leftMargin() - document.rightMargin()) / 2);

        ALResults.addCell(subject);
        ALResults.addCell(result);
        ALResults.addCell(attempt);
        ALResults.addCell(index);

        for ( ApplicantResult applicantResult : applicantResultAL ) {
          PdfPCell subjectNameValue = new PdfPCell(new Paragraph(applicantResult.getSubjectName(), tableHeader));
          pdfCellBodyCommonStyle(subjectNameValue);
          ALResults.addCell(subjectNameValue);

          PdfPCell resultValue =
              new PdfPCell(new Paragraph(applicantResult.getSubjectResult().getSubjectResult(), tableHeader));
          pdfCellBodyCommonStyle(resultValue);
          ALResults.addCell(resultValue);

          PdfPCell attemptValue =
              new PdfPCell(new Paragraph(applicantResult.getAttempt().getAttempt(), tableHeader));
          pdfCellBodyCommonStyle(attemptValue);
          ALResults.addCell(attemptValue);

          PdfPCell indexValue =
              new PdfPCell(new Paragraph(applicantResult.getIndexNumber(), tableHeader));
          pdfCellBodyCommonStyle(indexValue);
          ALResults.addCell(indexValue);
        }
        document.add(ALResults);
      }

      if ( !applicant.getApplicantDegreeResults().isEmpty() ) {
        PdfPTable applicantDegreeResults = new PdfPTable(3);//column amount
        applicantDegreeResults.setWidthPercentage(100);
        applicantDegreeResults.setSpacingBefore(10f);
        applicantDegreeResults.setSpacingAfter(10);
        applicantDegreeResults.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());


        PdfPCell degreeName = new PdfPCell(new Paragraph("Degree", tableHeaderOnly));
        pdfCellHeaderCommonStyle(degreeName);
        applicantDegreeResults.addCell(degreeName);

        PdfPCell universityName = new PdfPCell(new Paragraph("University Name", tableHeaderOnly));
        pdfCellHeaderCommonStyle(universityName);
        applicantDegreeResults.addCell(universityName);

        PdfPCell year = new PdfPCell(new Paragraph("Year", tableHeaderOnly));
        pdfCellHeaderCommonStyle(year);
        applicantDegreeResults.addCell(year);


        for ( ApplicantDegreeResult value : applicant.getApplicantDegreeResults() ) {
          PdfPCell degreeNameValue = new PdfPCell(new Paragraph(value.getDegreeName(), tableHeader));
          pdfCellBodyCommonStyle(degreeNameValue);
          applicantDegreeResults.addCell(degreeNameValue);

          PdfPCell universityNameValue =
              new PdfPCell(new Paragraph(value.getUniversityName(), tableHeader));
          pdfCellBodyCommonStyle(universityName);
          applicantDegreeResults.addCell(universityName);

          PdfPCell yearValue =
              new PdfPCell(new Paragraph(value.getYear(), tableHeader));
          pdfCellBodyCommonStyle(yearValue);
          applicantDegreeResults.addCell(yearValue);
        }

        document.add(applicantDegreeResults);
      }

      if ( !applicant.getNonRelatives().isEmpty() ) {
        PdfPTable nonRelatives = new PdfPTable(4);//column amount
        nonRelatives.setWidthPercentage(100);
        nonRelatives.setSpacingBefore(10f);
        nonRelatives.setSpacingAfter(10);
        nonRelatives.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());


        PdfPCell nonRelativeName = new PdfPCell(new Paragraph("Name", tableHeaderOnly));
        pdfCellHeaderCommonStyle(nonRelativeName);
        nonRelatives.addCell(nonRelativeName);

        PdfPCell nonRelativeMobile = new PdfPCell(new Paragraph("Mobile", tableHeaderOnly));
        pdfCellHeaderCommonStyle(nonRelativeMobile);
        nonRelatives.addCell(nonRelativeMobile);

        PdfPCell nonRelativeLand = new PdfPCell(new Paragraph("Land", tableHeaderOnly));
        pdfCellHeaderCommonStyle(nonRelativeLand);
        nonRelatives.addCell(nonRelativeLand);

        PdfPCell nonRelativeAddress = new PdfPCell(new Paragraph("Address", tableHeaderOnly));
        pdfCellHeaderCommonStyle(nonRelativeAddress);
        nonRelatives.addCell(nonRelativeAddress);


        for ( NonRelative value : applicant.getNonRelatives() ) {
          PdfPCell nonRelativeNameValue = new PdfPCell(new Paragraph(value.getName(), tableHeader));
          pdfCellBodyCommonStyle(nonRelativeNameValue);
          nonRelatives.addCell(nonRelativeNameValue);

          PdfPCell nonRelativeMobileValue =
              new PdfPCell(new Paragraph(value.getMobile(), tableHeader));
          pdfCellBodyCommonStyle(nonRelativeMobileValue);
          nonRelatives.addCell(nonRelativeMobileValue);

          PdfPCell nonRelativeLandValue =
              new PdfPCell(new Paragraph(value.getLand(), tableHeader));
          pdfCellBodyCommonStyle(nonRelativeLandValue);
          nonRelatives.addCell(nonRelativeLandValue);

          PdfPCell nonRelativeAddressValue =
              new PdfPCell(new Paragraph(value.getAddress(), tableHeader));
          pdfCellBodyCommonStyle(nonRelativeAddressValue);
          nonRelatives.addCell(nonRelativeAddressValue);
        }

        document.add(nonRelatives);
      }

      firstInterviewParameter(document);
      document.close();
    }


    return new ByteArrayInputStream(out.toByteArray());
  }

  private void firstInterviewParameter(Document document) throws DocumentException {
    //take interview parameter from db and create table
    // index parameterName result max remark
    PdfPTable firstInterviewTable = new PdfPTable(5);
    firstInterviewTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());

    List< InterviewParameter > interviewParameters =
        interviewService.findByInterviewName(InterviewName.FIRST).getInterviewParameters();


    PdfPCell indexCell = new PdfPCell(new Phrase("Index ", secondaryFont));
    pdfCellHeaderCommonStyle(indexCell);
    firstInterviewTable.addCell(indexCell);

    PdfPCell parameterNameCell = new PdfPCell(new Phrase("Parameter Name ", secondaryFont));
    pdfCellHeaderCommonStyle(parameterNameCell);
    firstInterviewTable.addCell(parameterNameCell);

    PdfPCell resultCell = new PdfPCell(new Phrase("Result ", secondaryFont));
    pdfCellHeaderCommonStyle(resultCell);
    firstInterviewTable.addCell(resultCell);

    PdfPCell maxCell = new PdfPCell(new Phrase("Max ", secondaryFont));
    pdfCellHeaderCommonStyle(maxCell);
    firstInterviewTable.addCell(maxCell);

    PdfPCell remarkCell = new PdfPCell(new Phrase("Remark ", secondaryFont));
    pdfCellHeaderCommonStyle(remarkCell);
    firstInterviewTable.addCell(remarkCell);

    //adds parameters to here
    for ( int i = 0; i < interviewParameters.size(); i++ ) {
      PdfPCell index = new PdfPCell(new Paragraph(Integer.toString(i + 1), tableHeader));
      pdfCellBodyCommonStyle(index);
      firstInterviewTable.addCell(index);

      PdfPCell parameterName = new PdfPCell(new Paragraph(interviewParameters.get(i).getName(), tableHeader));
      pdfCellBodyCommonStyle(parameterName);
      firstInterviewTable.addCell(parameterName);

      PdfPCell result = new PdfPCell(new Paragraph(" ", tableHeader));
      pdfCellBodyCommonStyle(result);
      firstInterviewTable.addCell(result);

      PdfPCell max = new PdfPCell(new Paragraph(interviewParameters.get(i).getMax(), tableHeader));
      pdfCellBodyCommonStyle(max);
      firstInterviewTable.addCell(max);

      PdfPCell remark = new PdfPCell(new Paragraph(" ", tableHeader));
      pdfCellBodyCommonStyle(remark);
      firstInterviewTable.addCell(remark);

    }

    document.add(firstInterviewTable);
  }

  private void pdfCellHeaderCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorderColor(BaseColor.BLACK);
    pdfPCell.setPaddingLeft(10);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setBackgroundColor(BaseColor.DARK_GRAY);
    pdfPCell.setExtraParagraphSpace(5f);
  }

  private void pdfCellBodyCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorderColor(BaseColor.BLACK);
    pdfPCell.setPaddingLeft(10);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setBackgroundColor(BaseColor.WHITE);
    pdfPCell.setExtraParagraphSpace(5f);
  }

  private void commonStyleForPdfPCellLastOne(PdfPCell pdfPCell) {
    pdfPCell.setBorder(0);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    pdfPCell.setBorderColor(BaseColor.WHITE);
  }

  private void commonStyleForParagraphTwo(Paragraph paragraph) {
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setIndentationLeft(50);
    paragraph.setIndentationRight(50);
  }

  private void addEmptyLine(Paragraph paragraph, int number) {
    for ( int i = 0; i < number; i++ ) {
      paragraph.add(new Paragraph(" "));
    }
  }

}
