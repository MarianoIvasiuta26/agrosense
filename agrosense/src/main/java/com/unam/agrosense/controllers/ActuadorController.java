package com.unam.agrosense.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unam.agrosense.model.actuador.ActuadorDto;
import com.unam.agrosense.model.actuador.ActuadorResponseDto;
import com.unam.agrosense.model.actuadorTipoActuador.ActuadorTipoActuadorDto;
import com.unam.agrosense.model.actuadorTipoActuador.ActuadorTipoActuadorResponseDto;
import com.unam.agrosense.model.cambioActuador.CambioActuadorDto;
import com.unam.agrosense.model.cambioActuador.CambioActuadorResponseDto;
import com.unam.agrosense.model.dispositivo.TipoDispositivo;
import com.unam.agrosense.model.tipoActuador.TipoActuador;
import com.unam.agrosense.model.tipoActuador.TipoActuadorResponseDto;
import com.unam.agrosense.services.ActuadorService;
import com.unam.agrosense.services.CambioActuadorService;
import com.unam.agrosense.services.TipoActuadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/actuadores")
public class ActuadorController {

    private final ActuadorService actuadorService;
    private final TipoActuadorService tipoActuadorService;
    private final CambioActuadorService cambioActuadorService;

    public ActuadorController(ActuadorService actuadorService, TipoActuadorService tipoActuadorService, CambioActuadorService cambioActuadorService) {
        this.actuadorService = actuadorService;
        this.tipoActuadorService = tipoActuadorService;
        this.cambioActuadorService = cambioActuadorService;
    }

    //REGISTRAR UN ACTUADOR
//    @PostMapping("/store")
//    public ResponseEntity<ActuadorResponseDto> registrarActuador(@ModelAttribute @RequestBody @Valid ActuadorDto actuadorDto, UriComponentsBuilder uriBuilder){
//
//        ActuadorResponseDto actuadorResponseDto = actuadorService.crearActuador(actuadorDto);
//
//        URI url = uriBuilder.path("/actuadores/{id}").buildAndExpand(actuadorResponseDto.id()).toUri();
//        return ResponseEntity.created(url).body(actuadorResponseDto);
//    }

//    @PostMapping("/store")
//    public ModelAndView registrarActuador(@ModelAttribute @RequestBody @Valid ActuadorDto actuadorDto, UriComponentsBuilder uriBuilder){
//
//        ActuadorResponseDto actuadorResponseDto = actuadorService.crearActuador(actuadorDto);
//
//        ModelAndView mav = new ModelAndView("redirect:/actuadores");
//        mav.addObject("mensaje","Actuador creado");
//
//
//        return mav;
//
//    }


    @PostMapping("/store")
    public String registrarActuador(@ModelAttribute @Valid ActuadorDto actuadorDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            capturarErrores(bindingResult,redirectAttributes);
            return "redirect:/actuadores";
        }

        try {
            actuadorService.crearActuador(actuadorDto);
            redirectAttributes.addFlashAttribute("mensaje", "Actuador registrado exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            System.out.println("Error al registrar el actuador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar el actuador.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/actuadores";
    }


    // ACTUALIZAR UN ACTUADOR
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<ActuadorResponseDto> actualizarActuador(@ModelAttribute @RequestBody @Valid ActuadorDto actuadorDto,
//                                                                  @PathVariable Long id, UriComponentsBuilder uriBuilder) {
//        ActuadorResponseDto actuadorResponseDto = actuadorService.actualizarActuador(id, actuadorDto);
//
//        return ResponseEntity.ok(actuadorResponseDto);
//    }


    @PutMapping("/edit/{id}")
    public String actualizarActuador(@ModelAttribute @RequestBody @Valid ActuadorDto actuadorDto,
                                     @PathVariable Long id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            capturarErrores(bindingResult,redirectAttributes);
            return "redirect:/actuadores";
        }

        try {
            actuadorService.actualizarActuador(id, actuadorDto);

            redirectAttributes.addFlashAttribute("mensaje", "Actuador actualizado exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            System.out.println("Error al actualizar el actuador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el actuador.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/actuadores";

    }

//    @PutMapping("/edit/estado/{id}")
//    public ResponseEntity<ActuadorResponseDto> actualizarEstadoActuador(@PathVariable Long id, @RequestParam String estado) {
//        ActuadorResponseDto actuadorResponseDto = actuadorService.actualizarEstadoActuador(id, estado);
//        return ResponseEntity.ok(actuadorResponseDto);
//    }

    // ELIMINAR UN ACTUADOR
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarActuador(@PathVariable Long id) {
        actuadorService.eliminarActuador(id);
        return ResponseEntity.noContent().build();
    }

    // OBTENER UN ACTUADOR

    @GetMapping("/{id}")
    public ResponseEntity<ActuadorResponseDto> obtenerActuador(@PathVariable Long id) {
        ActuadorResponseDto actuadorResponseDto = actuadorService.obteneractuador(id);
        return ResponseEntity.ok(actuadorResponseDto);
    }

     //OBTENER TODOS LOS ACTUADORES
    @GetMapping
    public String listarActuadores(Model model) {
        List<ActuadorResponseDto> actuadores = actuadorService.obtenerActuadores();
        //List<ActuadorTipoActuadorResponseDto> actuadorTipoActuadores = actuadorService.obtenerActuadoresTipoActuador();

        model.addAttribute("actuadores", actuadores);
        model.addAttribute("tiposDispositivo", TipoDispositivo.values());
        model.addAttribute("tiposActuadores", tipoActuadorService.obtenerTiposActuadores());
        //model.addAttribute("actuadorTipoActuadores", actuadorTipoActuadores);
        return "dispositivos/Actuadores";
    }

    @GetMapping("/{actuadorId}/tipos")
    @ResponseBody
    public List<ActuadorTipoActuadorResponseDto> obtenerTiposDeActuador(@PathVariable Long actuadorId) {
        return actuadorService.obtenerActuadoresTipoActuadorByActuadorId(actuadorId);
    }

    @PutMapping("/cambiar-estado/{actuadorId}/{tipoActuadorId}")
    public ResponseEntity<?>cambiarEstadoActuadorTipoActuador(@PathVariable Long actuadorId, @PathVariable Long tipoActuadorId, @RequestBody Map<String, String> requestBody) {

        try {
            // Obtener el estado desde el cuerpo de la solicitud
            String nuevoEstado = requestBody.get("estado");

            // Validar que el estado no sea nulo
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Estado no puede estar vacío"));
            }

            // Llamar al servicio para cambiar el estado
            actuadorService.cambiarEstadoActuadorTipoActuador(actuadorId, tipoActuadorId, nuevoEstado);

            // Responder con un mensaje de éxito
            return ResponseEntity.ok(Map.of("success", true, "message", "Estado cambiado exitosamente"));

        } catch (Exception e) {
            System.out.println("Error al cambiar el estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al cambiar el estado"));
        }
    }


    @GetMapping("/cantidad")
    public ResponseEntity<Integer> cantidadDeActuadores() {
        return ResponseEntity.ok().body(actuadorService.cantidadDeActuadores());
    }

//    @PutMapping("/nuevo-estado/{id}")
//    public ResponseEntity<CambioActuadorResponseDto> actualizarEstado(@ModelAttribute @PathVariable Long id, @RequestBody String estado) {
//        CambioActuadorDto cambioActuador = actuadorService.modificarEstadoActuador(id, estado);
//
//        CambioActuadorResponseDto cambioActuadorResponseDto = cambioActuadorService.crearCambioActuador(cambioActuador);
//
//        return ResponseEntity.ok(cambioActuadorResponseDto);
//    }


@PostMapping("/agregarTipoActuador")
public String agregarTipoActuador(@ModelAttribute @RequestBody @Valid ActuadorTipoActuadorDto actuadorTipoActuadorDto, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
        capturarErrores(bindingResult,redirectAttributes);
        return "redirect:/actuadores";
    }

    try {
        actuadorService.agregarTipoActuador(actuadorTipoActuadorDto);
        redirectAttributes.addFlashAttribute("mensaje", "Tipo de actuador agregado exitosamente.");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");

    } catch (Exception e) {
        System.out.println("Error al agregar el tipo de actuador: " + e.getMessage());
        redirectAttributes.addFlashAttribute("mensaje", "Error al agregar el tipo de actuador.");
        redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
    }
    return "redirect:/actuadores";
}

    @GetMapping("/{id}/estados")
    public ResponseEntity<List<String>> obtenerEstadosPorTipoActuador(@PathVariable Long id) {
        TipoActuador tipoActuador = tipoActuadorService.obtenerPorId(id);
        if (tipoActuador != null) {
            return ResponseEntity.ok(tipoActuador.getEstados());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void capturarErrores(BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // Captura los mensajes de error y los agrega a los atributos de redirección
        List<String> errores = bindingResult.getFieldErrors().stream()
                .map(error -> String.format("El campo %s %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        redirectAttributes.addFlashAttribute("errores", errores);
        redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
    }


}
