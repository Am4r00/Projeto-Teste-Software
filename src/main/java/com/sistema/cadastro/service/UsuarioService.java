package com.sistema.cadastro.service;
import com.sistema.cadastro.produto.Usuario;
import com.sistema.cadastro.external.CepClient;
import com.sistema.cadastro.external.CepResponse;
import com.sistema.cadastro.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CepClient cepClient;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario criar(Usuario usuario) {
        validarUnicidade(usuario);

        if (usuario.getCep() != null && !usuario.getCep().isEmpty()) {
            preencherEnderecoPorCep(usuario);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // Validação de unicidade apenas se email ou CPF foram alterados
        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail()) ||
                !usuarioExistente.getCpf().equals(usuarioAtualizado.getCpf())) {
            validarUnicidade(usuarioAtualizado, id);
        }


        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setCpf(usuarioAtualizado.getCpf());

        if (usuarioAtualizado.getCep() != null && !usuarioAtualizado.getCep().isEmpty()) {
            usuarioExistente.setCep(usuarioAtualizado.getCep());
            preencherEnderecoPorCep(usuarioExistente);
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private void validarUnicidade(Usuario usuario) {
        validarUnicidade(usuario, null);
    }

    private void validarUnicidade(Usuario usuario, Long idExcluido) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
                if (!u.getId().equals(idExcluido)) {
                    throw new RuntimeException("Email já cadastrado: " + usuario.getEmail());
                }
            });
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            usuarioRepository.findByCpf(usuario.getCpf()).ifPresent(u -> {
                if (!u.getId().equals(idExcluido)) {
                    throw new RuntimeException("CPF já cadastrado: " + usuario.getCpf());
                }
            });
        }
    }

    private void preencherEnderecoPorCep(Usuario usuario) {
        try {
            CepResponse cepResponse = cepClient.buscarCep(usuario.getCep())
                    .block(); // Transforma Mono em bloqueante

            if (cepResponse != null) {
                usuario.setLogradouro(cepResponse.getLogradouro());
                usuario.setCidade(cepResponse.getLocalidade());
                usuario.setEstado(cepResponse.getUf());
                log.info("Endereço preenchido para CEP: {}", usuario.getCep());
            }
        } catch (Exception e) {
            log.warn("Erro ao buscar CEP {}: {}", usuario.getCep(), e.getMessage());
            throw new RuntimeException("CEP inválido", e);
            // Não impede a criação do usuário mesmo se CEP falhar
        }
    }
}