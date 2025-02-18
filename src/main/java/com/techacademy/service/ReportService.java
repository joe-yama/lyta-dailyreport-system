package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report) {
        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report findById(Integer id) {
        Optional<Report> option = reportRepository.findById(id);
        Report report = option.orElse(null);
        return report;
    }

    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

    public boolean isCreatable(Employee employee, LocalDate reportDate) {
        Optional<Report> report =
                reportRepository.findByEmployeeAndReportDate(employee, reportDate);
        return report.map(x -> false).orElse(true);
    }

    public boolean isUpdatable(Employee employee, LocalDate newReportDate, Report originalReport) {
        Optional<Report> report =
                reportRepository.findByEmployeeAndReportDate(employee, newReportDate);

        boolean res = report
                .map(x -> x.getReportDate().equals(originalReport.getReportDate()) ? true : false)
                .orElse(true);
        return res;
    }

    @Transactional
    public ErrorKinds update(Report report) {
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    @Transactional
    public ErrorKinds delete(Integer id) {
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

}
