package com.am.harvest.service;

import com.am.harvest.model.Bid;
import com.am.harvest.model.Deal;
import com.am.harvest.repository.BidRepository;
import com.am.harvest.repository.DealRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final BidRepository bidRepository;
    private final DealRepository dealRepository;

    public ReportServiceImpl(BidRepository bidRepository, DealRepository dealRepository) {
        this.bidRepository = bidRepository;
        this.dealRepository = dealRepository;
    }

    @Override
    public ByteArrayResource getBidReportExcel() {
        List<Bid> bidList = (List<Bid>) bidRepository.findAll();
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Bids");

        addHeaders(sheet, Bid.class);
        bidList.forEach(e -> addBidRow(sheet, e));
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }
        File file = new File("Bids.xlsx");
        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new ByteArrayResource(Files.readAllBytes(Paths.get(file.getName())));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при формировании отчета: " + e.getMessage());
        }
    }

    @Override
    public ByteArrayResource getDealReportExcel() {
        List<Deal> dealList = (List<Deal>) dealRepository.findAll();
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Deals");

        addHeaders(sheet, Deal.class);
        dealList.forEach(e -> addDealRow(sheet, e));
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }
        File file = new File("Deals.xlsx");
        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new ByteArrayResource(Files.readAllBytes(Paths.get(file.getName())));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при формировании отчета: " + e.getMessage());
        }
    }

    private void addHeaders(Sheet sheet, Class clazz) {
        Row headers = sheet.createRow(0);
        int colCount = 0;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Cell cell = headers.createCell(colCount);
            cell.setCellValue(field.getName().toUpperCase());
            colCount++;
        }

    }

    private void addBidRow(Sheet sheet, Bid bid) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell id = row.createCell(0);
        id.setCellValue(bid.getId());
        Cell direction = row.createCell(1);
        direction.setCellValue(bid.getDirection().toString());
        Cell product = row.createCell(2);
        product.setCellValue(bid.getProduct());
        Cell quantity = row.createCell(3);
        quantity.setCellValue(bid.getQuantity());
        Cell price = row.createCell(4);
        price.setCellValue(bid.getPrice());
        Cell state = row.createCell(5);
        state.setCellValue(bid.getState().toString());
        Cell date = row.createCell(6);
        date.setCellValue(bid.getDate().toString());
    }

    private void addDealRow(Sheet sheet, Deal deal) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell id = row.createCell(0);
        id.setCellValue(deal.getId());
        Cell product = row.createCell(1);
        product.setCellValue(deal.getProduct());
        Cell quantity = row.createCell(2);
        quantity.setCellValue(deal.getQuantity());
        Cell price = row.createCell(3);
        price.setCellValue(deal.getPrice());
        Cell date = row.createCell(4);
        date.setCellValue(deal.getDate().toString());
    }
}
