package com.demo.POM.singleton.driver

import com.demo.POM.singleton.exceptions.UnsupportedDriverTypeException
import groovy.util.logging.Slf4j
import org.openqa.selenium.Platform
import org.openqa.selenium.WebDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

@Slf4j
class SauceLabsDriver extends DriverType {
    private def userName, accessKey
	
	public SauceLabsDriver() {
		super()
	}

	@Override
	WebDriver createDriver() {
        log.info("entering createDriver of %s class", this.class.simpleName)

		if(driver == null) {
            log.info("Requesting ${browser} instance")
            caps = createCapabilities()
		} else {
			return driver
		}

        caps.version = version
        caps.platform = Platform.fromString(platform)

        log.info("creating SauceLabsDriver instance with url: http://${userName}:${accessKey}@${serverAddress}:${serverPort}/wd/hub")
        log.info("and capabilities: ${caps.getVersion()}, ${caps.getPlatform()}")
		driver = new RemoteWebDriver(
                new URL("http://${userName}:${accessKey}@${serverAddress}:${serverPort}/wd/hub"), caps)

        driver
    }

    @Override
    protected DesiredCapabilities createCapabilities() {
        browser = config.seleniumConfigs.sauceLabs.browser
        userName = config.seleniumConfigs.sauceLabs.userName
        accessKey = config.seleniumConfigs.sauceLabs.accessKey
        platform = config.seleniumConfigs.sauceLabs.os
        version = config.seleniumConfigs.sauceLabs.browserVersion
        serverAddress = config.seleniumConfigs.sauceLabs.onDemand.server
        serverPort = config.seleniumConfigs.sauceLabs.onDemand.port

        def capabilities
        if (browser.equalsIgnoreCase('firefox')) {
            capabilities = DesiredCapabilities.firefox()
        } else if (browser.equalsIgnoreCase('chrome')) {
            capabilities = DesiredCapabilities.chrome()
        } else if (browser.contains('internet')) {
            capabilities = DesiredCapabilities.internetExplorer()
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true)
        } else if (browser.equalsIgnoreCase('safari')) {
            capabilities = DesiredCapabilities.safari()
        } else {
            log.error("Unsupported browser type: ${browser}. Throwing exception")
            throw new UnsupportedDriverTypeException("unsupported browser type: ${browser}")
        }
        capabilities
	}

}