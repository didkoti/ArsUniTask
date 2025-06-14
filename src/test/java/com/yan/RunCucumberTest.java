package com.yan;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.yan.steps",
    plugin = {"pretty", "html:target/cucumber-html-report"}
)
public class RunCucumberTest {
}
