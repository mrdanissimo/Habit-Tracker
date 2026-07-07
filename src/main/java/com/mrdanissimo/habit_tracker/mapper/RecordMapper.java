package com.mrdanissimo.habit_tracker.mapper;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.entity.Record;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {

    public RecordResponse toResponse(Record record) {
        if (record == null) {
            return null;
        }

        RecordResponse response = new RecordResponse();
        response.setId(record.getId());
        response.setDate(record.getDate());

        if (record.getHabit() != null) {
            response.setHabitId(record.getHabit().getId());
        }

        return response;
    }
}