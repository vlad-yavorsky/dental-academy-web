package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.program.ProgramCreateDto;
import ua.kazo.dentalacademy.dto.program.ProgramUpdateDto;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;
import ua.kazo.dentalacademy.mapper.FolderMapper;
import ua.kazo.dentalacademy.mapper.OfferingMapper;
import ua.kazo.dentalacademy.mapper.ProgramMapper;
import ua.kazo.dentalacademy.service.FolderService;
import ua.kazo.dentalacademy.service.OfferingService;
import ua.kazo.dentalacademy.service.ProgramService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProgramController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final OfferingService offeringService;
    private final OfferingMapper offeringMapper;
    private final FolderService folderService;
    private final FolderMapper folderMapper;

    /* ---------------------------------------------- PROGRAMS ---------------------------------------------- */

    @GetMapping("/programs")
    public String programs(final ModelMap model,
                           @RequestParam(defaultValue = "0") final int page,
                           @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByCategory(ProgramCategory.STANDARD, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.PROGRAMS, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/program/programs";
    }

    /* ---------------------------------------------- BONUSES ---------------------------------------------- */

    @GetMapping("/bonuses")
    public String bonuses(final ModelMap model,
                          @RequestParam(defaultValue = "0") final int page,
                          @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Program> pageResult = programService.findAllByCategory(ProgramCategory.BONUS, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.BONUSES, programMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/bonus/bonuses";
    }

    /* ---------------------------------------------- ADD PROGRAM ---------------------------------------------- */

    private void validateProgramName(final Program program, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (programService.existsByName(program.getName())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        } else {
            if (programService.existsByNameAndIdNot(program.getName(), program.getId())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        }
    }

    private String loadProgramAddPage(final ProgramCreateDto programCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programCreateDto);
        return "admin/program/program-add";
    }

    @GetMapping("/program/add")
    public String addProgram(final ModelMap model, final @RequestParam(required = false) ProgramCategory category) {
        ProgramCreateDto programCreateDto = new ProgramCreateDto();
        programCreateDto.setCategory(category);
        return loadProgramAddPage(programCreateDto, model);
    }

    @PostMapping("/program/add")
    public String addProgram(@ModelAttribute(ModelMapConstants.PROGRAM) @Valid final ProgramCreateDto programCreateDto,
                             final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Program program = programMapper.toEntity(programCreateDto);

        validateProgramName(program, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadProgramAddPage(programCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.program.add");

        Program savedProgram = programService.save(program);
        return "redirect:/admin/program/edit/" + savedProgram.getId();
    }

    /* ---------------------------------------------- EDIT PROGRAM ---------------------------------------------- */

    private String loadProgramEditPage(final ProgramUpdateDto programUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.PROGRAM, programUpdateDto);
        model.addAttribute(ModelMapConstants.FOLDERS, folderMapper.toResponseDto(folderService.findAllByProgramIdOrderByOrdering(programUpdateDto.getId())));
        model.addAttribute(ModelMapConstants.OFFERINGS, offeringMapper.toResponseDto(offeringService.findAllByProgramId(programUpdateDto.getId())));
        return "admin/program/program-edit";
    }

    @GetMapping("/program/edit/{id}")
    public String editProgram(@PathVariable Long id, final ModelMap model) {
        return loadProgramEditPage(programMapper.toUpdateDto(programService.findById(id)), model);
    }

    @PostMapping("/program/edit/{id}")
    public String editProgram(@ModelAttribute(ModelMapConstants.PROGRAM) @Valid final ProgramUpdateDto programUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Program program = programMapper.toEntity(programUpdateDto);

        validateProgramName(program, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadProgramEditPage(programUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.program.edit");

        Program savedProgram = programService.save(program);
        return loadProgramEditPage(programMapper.toUpdateDto(savedProgram), model);
    }

}
