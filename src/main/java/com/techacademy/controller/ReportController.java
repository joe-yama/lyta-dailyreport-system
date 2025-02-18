package com.techacademy.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.entity.Employee.Role;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;


@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail, Model model) {
        Employee currentUser = userDetail.getEmployee();
        Role currentUserRole = currentUser.getRole();

        List<Report> reportList;
        if (currentUserRole.equals(Role.ADMIN)) {
            reportList = reportService.findAll();
        } else {
            reportList = reportService.findByEmployee(currentUser);
        }
        model.addAttribute("reportList", reportList);
        model.addAttribute("listSize", reportList.size());

        return "reports/list";
    }

    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report,
            @AuthenticationPrincipal UserDetail userDetail, Model model) {
        model.addAttribute("currentUsername", userDetail.getEmployee().getName());
        return "reports/new";
    }

    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res,
            @AuthenticationPrincipal UserDetail userDetail, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return create(report, userDetail, model);
        }

        Employee employee = userDetail.getEmployee();
        report.setEmployee(employee);

        // すでにその日付・同一社員による日報がある場合にはエラー
        if (!reportService.isCreatable(employee, report.getReportDate())) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return create(report, userDetail, model);
        }

        ErrorKinds result = reportService.save(report);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result),
                    ErrorMessage.getErrorValue(result));
            return create(report, userDetail, model);
        }

        return "redirect:/reports";
    }

    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/update";
    }

    @PostMapping(value = "/{id}/update")
    public String update(@Validated Report report, BindingResult res, Model model) {
        if (res.hasErrors()) {
            return "reports/update";
        }

        Report originalReport = reportService.findById(report.getId());

        if (!reportService.isUpdatable(report.getEmployee(), report.getReportDate(),
                originalReport)) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return edit(originalReport.getId(), model);
        }

        LocalDateTime originalCreatedAt = originalReport.getCreatedAt();
        report.setCreatedAt(originalCreatedAt);

        ErrorKinds result = reportService.update(report);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result),
                    ErrorMessage.getErrorValue(result));
            return "reports/update";
        }

        return "redirect:/reports";
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model) {

        ErrorKinds result = reportService.delete(id);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result),
                    ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }

}
