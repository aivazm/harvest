package com.am.harvest.service;

import org.springframework.core.io.ByteArrayResource;

import java.io.File;

public interface ReportService {
    ByteArrayResource getBidReportExcel();
    ByteArrayResource getDealReportExcel();
}
