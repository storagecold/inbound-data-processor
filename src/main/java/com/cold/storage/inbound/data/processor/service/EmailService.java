package com.cold.storage.inbound.data.processor.service;

@org.springframework.stereotype.Service
public class EmailService {
/*
    @Value("${emailNotification.uri}")
    public String emailNotifUri;

    @Value("${spring.email.supportEmail}")
    private List<String> supportEmail ;

    private WebClient webClient;

    @PostConstruct
    private void initRestClients() {

        webClient = WebClient.builder()
                .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(org.springframework.http.HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(Constants.Header.CALLERAPP,Constants.Header.CALLERAPP_NAME).build();
    }

    public void sendMail(EmailRequest emailRequest)  {
        try {
            log.with($ -> $.addMessage("Sending Email for File : " + emailRequest.getFileName())).info();

            if (Objects.nonNull(emailRequest)){
                log.with($ -> $.addMessage("Email sent for File : " + emailRequest.getFileName())).info();
                String response = webClient.post().uri(emailNotifUri)
                        .body(Mono.just(emailRequest), EmailRequest.class).retrieve().bodyToMono(String.class).block();
            } else {
                log.with($ -> $.addMessage("Email not sent for File : " + emailRequest.getFileName())).info();
            }

        } catch (Exception ex) {
                log.with($ -> $.addMessage("Failed to send email after 3 retries for file : " + emailRequest.getFileName()+ ex.getMessage()))
                        .error(EligibilityLogStatus.SENTFAILURE);
            }
    }

    public void generateEmailRequest(String fileName, String subject, String emailMessage) {
        EmailRequest emailRequest = new EmailRequest();

        emailRequest.setFileName(fileName);
        emailRequest.setSubject(subject);
        emailRequest.setMessage(emailMessage);
        emailRequest.setToEmails(supportEmail);
        sendMail(emailRequest);

    }*/
}
