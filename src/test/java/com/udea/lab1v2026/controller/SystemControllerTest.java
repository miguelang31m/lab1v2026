package com.udea.lab1v2026.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SystemControllerTest {
    @Test
    void healthReturnsUp() {
        SystemController controller = new SystemController("2.0.0");
        assertEquals("UP", controller.health().getBody().get("status"));
    }

    @Test
    void versionReturnsConfiguredVersion() {
        SystemController controller = new SystemController("2.0.0");
        assertEquals("2.0.0", controller.version().getBody().get("version"));
    }
}
