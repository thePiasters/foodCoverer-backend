package com.foodCoverer.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class VerifyServiceTest {

    @InjectMocks
    VerifyService verifyService;

    @Test
    void shouldNotVerifyInvalidUserToken() throws GeneralSecurityException, IOException {

        GoogleIdToken googleIdToken = verifyService.verifyUserToken("eyJhbGciOiJSUzI1NiIsImtpZCI6ImNhMDA2MjBjNWFhN2JlOf" +
                                                                    "rtuhNhNmYzYzY4NDA2ZTQ1ZTkzYjNjYWIiLCJ0eXAiOiJKV1QifQ." +
                                                                    "eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiMjA5M" +
                                                                    "Dc3MzE4NTAtZ3NncWdyYTQzOWhsdG84bTA2amtzcTdtdWpkMmwzZzIu" +
                                                                    "YXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyMDkwNzc" +
                                                                    "zMTg1MC1nc2dxZ3JhNDM5aGx0bzhtMDZqa3NxN211amQybDNnMi5hcHB" +
                                                                    "zLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExNjc0NzgxOTMzM" +
                                                                    "jIzNDEyMTk1MyIsImVtYWlsIjoia2FzaWFwaWFzdGFAZ21haWwuY29tIiwi" +
                                                                    "ZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJtbDFSODR" +
                                                                    "kMFdRNnlnYlozTmR1cDlBIiwibmFtZSI6IkthdGFyenluYSBQaWFzdG" +
                                                                    "EiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNv" +
                                                                    "bnRlbnQuY29tL2EtLfjrufyth2lLeU93dlRzaE5LR3RSNmM2Y" +
                                                                    "0VTdmRhLWNtNmZVbW9LOUlCWW9SPXM5Ni1jIiwiZ2l2ZW5fbmFt" +
                                                                    "ZSI6IkthdGFyenluYSIsImZhbWlseV9uYW1lIjoiUGlhc3RhIiw" +
                                                                    "ibG9jYWxlIjoicGwiLCJpYXQiOjE2NDI2OTg4NTgsImV4cCI6M" +
                                                                    "TY0MjcwMjQ1OCwianRpIjoiZjBiOTk1OWI3NjM1NjgyOWU2YTY" +
                                                                    "yY2MwZWVkZTY3MWY3YmJlMGM5ZSJ9.eDNtxOErPWRHRBa015yeW" +
                                                                    "-1e7gO823wCMkz5yUjXzfq55m-OMH6gFb-Pc_z26Gl6ry--ZqoDYHlPa" +
                                                                    "92CQ6RqsCVh7sIwKF01nh-nyy6leYOhcGqF9rcjnNwNsKfyagVHUht-ziRxU" +
                                                                    "VBvfnXB1DaZXck20wulNRck-mzCCr2k2Z4v9SdoBrM4qQv9i-" +
                                                                    "mED36WNabyXiU3GHtRUHE5ck7OZzk5tvWQ17LG6sSQErh_zfwC4_nVT5DV4iez" +
                                                                    "yHp2D733VsDhVLIbvW0iC6K_Lv3IlI0xmPhqbhLItDQyE5aHMW" +
                                                                    "PmANPgf4wk7bk09wUfF0jjKhTO3m74a2iKAxAN05rTqA");
        assertNull(googleIdToken);
    }
}