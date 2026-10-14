package com.udea.lab1v2026;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Lab1v2026ApplicationTests {
    @Test
    void mainClassExists() {
        assertDoesNotThrow(() -> Class.forName("com.udea.lab1v2026.Lab1v2026Application"));
    }
}
