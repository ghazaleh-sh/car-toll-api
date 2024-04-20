package ir.co.sadad.cartollapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.co.sadad.cartollapi.dtos.*;
import ir.co.sadad.cartollapi.service.InquiryService;
import ir.co.sadad.cartollapi.service.NajiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static ir.co.sadad.cartollapi.service.util.Constants.SSN;
import static ir.co.sadad.cartollapi.service.util.Constants.USER_AGENT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${v1API}/naji-api")
@Tag(description = "مستندات سرویس های خلافی خودرو", name = "Car Toll Resources")
public class TollResource {
}
