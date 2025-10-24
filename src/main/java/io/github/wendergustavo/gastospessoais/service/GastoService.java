package io.github.wendergustavo.gastospessoais.service;


import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.validador.GastoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final GastoValidator gastoValidator;
    private final GastoMapper gastoMapper;

    @Transactional
    public Gasto salvar(Gasto gasto){

        if (gasto.getDataGasto() == null) {
            gasto.setDataGasto(LocalDate.now());
        }
        gastoValidator.validarGasto(gasto);
        return gastoRepository.save(gasto);
    }

    public Optional<Gasto> buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException("Gasto not found.");
        }

        return gastoRepository.findById(id);
    }

    @Transactional
    public void deletar(Gasto gasto){

        if(gasto.getId() == null){
            throw new IllegalArgumentException("Gasto ID must not be null.");
        }

        gastoRepository.delete(gasto);
    }

    @Transactional
    public void atualizar(Gasto gasto){

        if(gasto.getId() == null){
            throw  new IllegalArgumentException("Para atualizar é necessario que o Gasto ja esteja cadastrado.");
        }

        if (gasto.getDataGasto() == null) {
            gasto.setDataGasto(LocalDate.now());
        }

        gastoValidator.validarGasto(gasto);
        gastoRepository.save(gasto);

    }


}
