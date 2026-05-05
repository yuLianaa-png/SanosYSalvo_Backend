package com.babygoat.pet_service.controlador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.service.PetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetService petService;

    @BeforeEach
    void setUp() {
        // Inyectar el servicio mockeado en el controller
        mockMvc = MockMvcBuilders.standaloneSetup(new petController(petService, null)).build();
    }

    @Test
    void cuandoNoHayParametros_debeTratarComoNullYDevolver204() throws Exception {
        when(petService.buscarPorRazaYColor(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/mascotas/buscar/match"))
                .andExpect(status().isNoContent());

        verify(petService).buscarPorRazaYColor(null, null, null, null);
    }

    @Test
    void normalizaParametros_y_llamaAlServicioConOrdenCorrecto() throws Exception {
        when(petService.buscarPorRazaYColor(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/mascotas/buscar/match")
                .param("raza", "  Labrador  ")
                .param("color", " Negro ")
                .param("estado", " perdida ")
                .param("ubicacion", " Madrid "))
                .andExpect(status().isNoContent());

        ArgumentCaptor<String> capR = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> capC = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> capU = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> capE = ArgumentCaptor.forClass(String.class);

        verify(petService).buscarPorRazaYColor(
                capR.capture(), capC.capture(), capU.capture(), capE.capture());

        org.junit.jupiter.api.Assertions.assertEquals("Labrador", capR.getValue());
        org.junit.jupiter.api.Assertions.assertEquals("Negro", capC.getValue());
        org.junit.jupiter.api.Assertions.assertEquals("Madrid", capU.getValue());
        org.junit.jupiter.api.Assertions.assertEquals("perdida", capE.getValue());
    }
}
