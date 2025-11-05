package com.sistema.cadastro.service;

import com.sistema.cadastro.entity.Usuario;
import com.sistema.cadastro.external.CepClient;
import com.sistema.cadastro.external.CepResponse;
import com.sistema.cadastro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CepClient cepClient;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setCpf("12345678901");
        usuario.setCep("01310100");
    }

    @Test
    void testCriarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        var cepResponse = new CepResponse();
        cepResponse.setCep("01310100");
        cepResponse.setLogradouro("Avenida Paulista");
        cepResponse.setLocalidade("São Paulo");
        cepResponse.setUf("SP");
        when(cepClient.buscarCep(anyString())).thenReturn(reactor.core.publisher.Mono.just(cepResponse));

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.criar(usuario);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCriarUsuarioEmailDuplicado() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () -> usuarioService.criar(usuario));
    }

    @Test
    void testCriarUsuarioCpfDuplicado() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(true);
        when(usuarioRepository.findByCpf(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () -> usuarioService.criar(usuario));
    }

    @Test
    void testListarTodos() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        var resultado = usuarioService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    void testAtualizarUsuario() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("João Santos");
        usuarioAtualizado.setEmail("joao@example.com");
        usuarioAtualizado.setCpf("12345678901");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioService.atualizar(1L, usuarioAtualizado);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testDeletarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.deletar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletarUsuarioNaoEncontrado() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.deletar(999L));
    }

    @Test
    void testErroAoSalvarUsuario() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        var cepResponse = new CepResponse();
        cepResponse.setCep("01310100");
        when(cepClient.buscarCep(anyString())).thenReturn(Mono.just(cepResponse));

        when(usuarioRepository.save(any())).thenThrow(new RuntimeException("Erro no banco de dados"));

        assertThrows(RuntimeException.class, () -> usuarioService.criar(usuario));
    }

    @Test
    void testAtualizarUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.atualizar(999L, usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        var resultado = usuarioService.buscarPorId(999L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testListarTodosVazio() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        var resultado = usuarioService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testCriarUsuarioComCamposNulos() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setEmail(null);
        usuarioInvalido.setCpf(null);
        usuarioInvalido.setNome(null);
        usuarioInvalido.setCep("00000000");

        when(cepClient.buscarCep(anyString()))
                .thenReturn(Mono.error(new RuntimeException("CEP inválido")));

        assertThrows(RuntimeException.class, () -> usuarioService.criar(usuarioInvalido));
    }

    @Test
    void testDeletarComErroNoBanco() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Erro de banco")).when(usuarioRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> usuarioService.deletar(1L));
    }
}
