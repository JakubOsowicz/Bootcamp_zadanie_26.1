package pl.osowicz.task_manager.testClasses;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DateTimeProvider {

    public LocalDateTime getLocalTimeNow() {
        return LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTimeNowTruncatedToSeconds() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
