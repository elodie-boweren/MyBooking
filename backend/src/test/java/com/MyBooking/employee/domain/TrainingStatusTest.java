package com.MyBooking.employee.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainingStatusTest {
    
    @Test
    void testTrainingStatusEnumValues() {
        // Test that all expected enum values exist
        TrainingStatus[] values = TrainingStatus.values();
        assertEquals(2, values.length);
        
        // Test specific enum values
        assertTrue(TrainingStatus.ASSIGNED != null);
        assertTrue(TrainingStatus.COMPLETED != null);
    }
    
    @Test
    void testTrainingStatusValueOf() {
        // Test valueOf method
        assertEquals(TrainingStatus.ASSIGNED, TrainingStatus.valueOf("ASSIGNED"));
        assertEquals(TrainingStatus.COMPLETED, TrainingStatus.valueOf("COMPLETED"));
    }
    
    @Test
    void testTrainingStatusToString() {
        // Test toString method
        assertEquals("ASSIGNED", TrainingStatus.ASSIGNED.toString());
        assertEquals("COMPLETED", TrainingStatus.COMPLETED.toString());
    }
    
    @Test
    void testTrainingStatusOrdinal() {
        // Test ordinal values
        assertEquals(0, TrainingStatus.ASSIGNED.ordinal());
        assertEquals(1, TrainingStatus.COMPLETED.ordinal());
    }
}
