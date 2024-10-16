package ru.peretyatko.app.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Period {

    private LocalDateTime start;

    private LocalDateTime end;

    public Period(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

}
