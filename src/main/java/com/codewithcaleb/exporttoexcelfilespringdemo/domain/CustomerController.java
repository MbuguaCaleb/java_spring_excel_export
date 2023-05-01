package com.codewithcaleb.exporttoexcelfilespringdemo.domain;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    @GetMapping("/export-to-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey ="Content-Disposition";
        String headerValue="attachment; filename=Customers_Information.xlsx";
        response.setHeader(headerKey,headerValue);
        customerService.exportCustomerToExcel(response);
    }

}
