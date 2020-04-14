package ua.kazo.dentalacademy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.kazo.dentalacademy.repository.ProgramRepository;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ProgramRepository programRepository;

    @GetMapping("/exist/{programId}")
    public boolean editFolder(@PathVariable Long programId) {
        return programRepository.existsById(programId);
    }

}
