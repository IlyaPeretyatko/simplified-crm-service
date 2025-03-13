package ru.peretyatko.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RangeDate {

    private LocalDateTime start;

    private LocalDateTime end;

    public RangeDate(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

}
