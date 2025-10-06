package io.github.wendergustavo.gastospessoais.service;


import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository repository;
    private final GastoValidator validator;

    public Gasto salvar(Gasto gasto){


        return repository.save(gasto);
    }

    public Optional<Gasto> buscarPorId(UUID id){

        if(id == null){
            throw new CampoInvalidoException("Gasto not found.");
        }

        return repository.findById(id);
    }

    @Transactional
    public void deletar(Gasto gasto){

        if(gasto.getId() == null){
            throw new IllegalArgumentException("Gasto ID must not be null.");
        }

        gastoRepository.delete(gasto);
    }


}
