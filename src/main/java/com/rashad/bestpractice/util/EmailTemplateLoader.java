package com.rashad.bestpractice.util;

import com.rashad.bestpractice.model.request.RegisterRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class EmailTemplateLoader {

    public String loadEmailTemplate(String templateName, String otp, String userName) throws IOException {
        Resource resource = new ClassPathResource("email-templates/" + templateName + ".html");
        InputStream inputStream = Objects.requireNonNull(resource.getInputStream());

        String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        html = html.replace("{{otp}}", otp);
        html = html.replace("{{name}}", userName);

        return html;
    }

    public String loadVenueApplicantEmailTemplate(String templateName, RegisterRequest registerRequest) throws IOException {
        Resource resource = new ClassPathResource("email-templates/" + templateName + ".html");
        InputStream inputStream = Objects.requireNonNull(resource.getInputStream());

        String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        html = html.replace("{{name}}", registerRequest.getName());
        html = html.replace("{{surname}}", registerRequest.getSurname());
        html = html.replace("{{phoneNumber}}", registerRequest.getPhoneNumber());
        html = html.replace("{{email}}", registerRequest.getEmail());
        html = html.replace("{{venueName}}", "Venue 1");
        html = html.replace("{{socialLink}}", "Social Link 1");

        return html;
    }
}