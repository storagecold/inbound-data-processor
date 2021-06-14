


package com.cold.storage.inbound.data.processor.repository;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/*@Component
public class CustomerProfileRepoImpl implements CustomerProfileRepo {

    private final static String PROFILE_DEFINITION_ENDPOINT = "/definition?submitterid=%s";
    private final static String PROFILE_FULL_DETAIL_ENDPOINT = "/fulldetail?submitterid=%s&profileversion=%s";
    private final static String DEST_APP_NAME = "profile-api";
    private final static String CALLER_APP_NAME = EligibilityApps.FILE_TRANSFORMATION_API.getAppName();

    private final Map<String, SubmitterDefinition> submitterDefinitionCache = new ConcurrentHashMap<>();
    private final Map<String, SubmitterFullDetail> submitterFullDetailCache = new ConcurrentHashMap<>();

    @Value("${profile.api.url}")
    private String baseUrl;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SubmitterDefinition getCustomerProfile(String fileId, String submitterId) {
        SubmitterDefinition submitterDefinition;
        if (submitterDefinitionCache.containsKey(submitterId)) {
            submitterDefinition = submitterDefinitionCache.get(submitterId);
        } else {
            submitterDefinition = loadCustomerProfile(fileId, submitterId);
        }
        log.with($ -> $.addMessage("Submitter Definition Loaded - For submitterId:" + submitterId + ", fileId:" + fileId +
                ", profileVersion:" + submitterDefinition.getProfileVersionNumber() + ", submitterDefinition: " + submitterDefinition)).info(EligibilityLogStatus.PROCESS);
        return submitterDefinition;
    }


    @Override
    public SubmitterFullDetail getSubmitterFullDetail(String fileId, String submitterId) {
        if (submitterFullDetailCache.containsKey(submitterId)) {
            return submitterFullDetailCache.get(submitterId);
        } else {
            return loadSubmitterFullDetail(fileId, submitterId);
        }
    }

    public SubmitterDefinition loadCustomerProfile(String fileId, String submitterId) {
        ResponseEntity<SubmitterDefinition> response;
        SubmitterDefinition submitterDefinition = null;
        log.with($ -> $.addMessage("Loading customer profile - definition for submitterId:" + submitterId + ", fileId:" + fileId)).info(EligibilityLogStatus.COMPLETED);

        HttpHeaders headers = getHeaders(fileId, submitterId);
        try {
            log.with($ -> $.addMessage("CustomerProfileDefinition SubmitterDefinition - Get SubmitterDefinition for submitterId: " + submitterId)).info(EligibilityLogStatus.COMPLETED);
            String requestUrl = String.format(baseUrl + PROFILE_DEFINITION_ENDPOINT, submitterId);
            log.with($ -> $.addMessage("CustomerProfileDefinition SubmitterDefinition: requestUrl= " + requestUrl)).info(EligibilityLogStatus.COMPLETED);
            response = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), SubmitterDefinition.class);
            log.with($ -> $.addMessage("CustomerProfile SubmitterDefinition: " + response.getStatusCode())).info(EligibilityLogStatus.COMPLETED);
        } catch (Exception e) {
            String errMessage = String.format("Profile Api SubmitterDefinition call failed for File Id : %s and Submitter Id : %s",
                    fileId, submitterId);
            log.with($ -> {
                $.addField("fileId ", fileId);
                $.addField("submitterId ", submitterId);
                $.addMessage(errMessage);
            }).
                    error(EligibilityLogStatus.PROCESSFAILURE, e);
            throw new RuntimeException(errMessage, e);
        }
        if (Objects.nonNull(response.getBody())) {
            submitterDefinition = response.getBody();
            submitterDefinitionCache.put(submitterId, submitterDefinition);
        }
        return submitterDefinition;
    }

    public SubmitterFullDetail loadSubmitterFullDetail(String fileId, String submitterId) {
        SubmitterFullDetail submitterFullDetail = null;
        ResponseEntity<SubmitterFullDetail> response;
        HttpHeaders headers = getHeaders(fileId, submitterId);
        try {
            SubmitterDefinition submitterDefinition = getCustomerProfile(fileId, submitterId);
            String requestUrl =
                    String.format(baseUrl + PROFILE_FULL_DETAIL_ENDPOINT, submitterId, submitterDefinition.getProfileVersionNumber());
            response = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), SubmitterFullDetail.class);
            log.with($ -> $.addMessage("SubmitterFullDetail requestUrl=" + requestUrl + " - responseStatusCode=" + response.getStatusCode())).info(EligibilityLogStatus.COMPLETED);
        } catch (Exception ex) {
            String errMessage = String.format("SubmitterFullDetail Profile API call failed for fileId: %s and submitterId: %s", fileId, submitterId);
            log.with($ -> {
                $.addField("fileId ", null);
                $.addField("submitterId ", submitterId);
                $.addMessage(errMessage);
            }).error(EligibilityLogStatus.PROCESSFAILURE, ex);
            throw new RuntimeException(errMessage, ex);
        }

        if (Objects.nonNull(response.getBody())) {
            submitterFullDetail = response.getBody();
            submitterFullDetailCache.put(submitterId, submitterFullDetail);
        }
        return submitterFullDetail;
    }

    private HttpHeaders getHeaders(String fileId, String submitterId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(EligibilityContextField.CALLER_APP, CALLER_APP_NAME);
        headers.add(EligibilityContextField.FILE_ID, fileId);
        headers.add(EligibilityContextField.SUBMITTER_NAME, submitterId);
        headers.add(EligibilityContextField.TRANS_ID, Long.toString(System.currentTimeMillis()));
        headers.add(EligibilityContextField.APP_NAME, DEST_APP_NAME);
        return headers;
    }*/

