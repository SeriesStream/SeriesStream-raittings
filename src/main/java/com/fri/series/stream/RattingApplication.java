package com.fri.series.stream;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import com.kumuluz.ee.discovery.annotations.RegisterService;

@ApplicationPath("v1")
@RegisterService
public class RattingApplication extends Application {
}
